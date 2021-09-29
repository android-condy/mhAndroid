package com.idic.datamoudle.source.repository

import android.util.Log
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.SPUtils
import com.idic.basecommon.SPKeys
import com.idic.basecommon.utils.FilePath
import com.idic.datamoudle.db.entity.CategoryEntity
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.datamoudle.download.DownloadImpl
import com.idic.datamoudle.download.IDownload
import com.idic.datamoudle.source.ProductDataSource
import com.idic.datamoudle.utils.CompareProduct
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * 文 件 名: ProductReposity
 * 创 建 人: sineom
 * 创建日期: 2019-08-07 15:38
 * 修改时间：
 * 修改备注：
 */

class ProductRepository(
    val productLocalDataSource: ProductDataSource.IProductsFromLocal,
    val productRemoteDataResource: ProductDataSource.IProductsFromRemote
) : ProductDataSource.IProductsFromLocal,
    ProductDataSource.IProductsFromRemote {


    private val categories = ArrayList<CategoryEntity>()

    private val categoryAndProducts = HashMap<String, List<ProductEntity>>()

    private val products = ArrayList<ProductEntity>()

    private var cacheIsDirty = false

    private val downloadImpl: IDownload = DownloadImpl()

    private var delInvalidJob: Job? = null

    override fun loadProductCategory(callback: ProductDataSource.LoadProductCategory) {
        if (categories.isNotEmpty() && !cacheIsDirty) {
            callback.onCategoryLoaded(categories)
            return
        }
        if (!cacheIsDirty || SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
            productLocalDataSource.loadProductCategory(object :
                ProductDataSource.LoadProductCategory {
                override fun onCategoryLoaded(categorice: List<CategoryEntity>) {
                    refreshCategoryCache(categorice)
                    callback.onCategoryLoaded(categorice)
                }

                override fun onCategoryNotAvailabled() {
                    productRemoteDataResource.loadProductCategory(object :
                        ProductDataSource.LoadProductCategory {
                        override fun onCategoryLoaded(categorice: List<CategoryEntity>) {
                            refreshCategoryCache(categorice)
                            callback.onCategoryLoaded(this@ProductRepository.categories)
                        }

                        override fun onCategoryNotAvailabled() {
                            callback.onCategoryNotAvailabled()
                        }
                    })
                }
            })
        } else {
            productRemoteDataResource.loadProductCategory(object :
                ProductDataSource.LoadProductCategory {
                override fun onCategoryLoaded(categorice: List<CategoryEntity>) {
                    refreshCategoryCache(categorice)
                    callback.onCategoryLoaded(this@ProductRepository.categories)
                }

                override fun onCategoryNotAvailabled() {
                    callback.onCategoryNotAvailabled()
                }
            })
        }


    }

    private fun refreshCategoryCache(categories: List<CategoryEntity>) {
        this.categories.clear()
        this.categories.addAll(categories)
        insertCategories(categories)
        cacheIsDirty = false
    }


    override fun getProducts(callback: ProductDataSource.LoadProductsCallback) {
        if (products.isNotEmpty() && !cacheIsDirty) {
            callback.onProductsLoad(products)
            return
        }
        if (!cacheIsDirty || SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
            productLocalDataSource.getProducts(object :
                ProductDataSource.LoadProductsCallback {
                override fun onProductsLoad(products: List<ProductEntity>) {
                    refreshProductsCache(products)
                    callback.onProductsLoad(products)
                }

                override fun onProductNotAvailable() {
                    callback.onProductNotAvailable()
                }
            })
        } else {
            productRemoteDataResource.getProducts(object :
                ProductDataSource.LoadProductsCallback {
                override fun onProductsLoad(products: List<ProductEntity>) {
                    refreshProductsCache(products)
//                    callback.onProductsLoad(products)
                    Log.i("condy","products="+products)
                    downloadImg(products, callback)
                }

                override fun onProductNotAvailable() {
                    callback.onProductNotAvailable()
                }
            })
        }
    }


    private fun refreshProductsCache(products: List<ProductEntity>) {
        this.products.clear()
//        Collections.sort(products, CompareProduct(true))
        this.products.addAll(products)
        insertProducts(products)
        cacheIsDirty = false
    }

    override fun getProductsFromCategory(
        category: String,
        callback: ProductDataSource.LoadProductsCallback
    ) {
        val list = categoryAndProducts[category]
        if (!list.isNullOrEmpty() && !cacheIsDirty) {
            callback.onProductsLoad(list)
            return
        }
        productLocalDataSource.getProductsFromCategory(category, object :
            ProductDataSource.LoadProductsCallback {
            override fun onProductsLoad(products: List<ProductEntity>) {
//                Collections.sort(products, CompareProduct(true))
                categoryAndProducts[category] = products
                callback.onProductsLoad(products)
            }

            override fun onProductNotAvailable() {
                productRemoteDataResource.getProductsFromCategory(category,
                    object : ProductDataSource.LoadProductsCallback {
                        override fun onProductsLoad(products: List<ProductEntity>) {
//                            Collections.sort(products, CompareProduct(true))
                            categoryAndProducts[category] = products
                            productLocalDataSource.insertProducts(products)
                            cacheIsDirty = false
                            downloadImg(products, callback)
//                            callback.onProductsLoad(products)

                        }

                        override fun onProductNotAvailable() {
                            callback.onProductNotAvailable()
                        }
                    })
            }
        })
    }

    private fun downloadImg(
        products: List<ProductEntity>,
        callback: ProductDataSource.LoadProductsCallback
    ) {
        Observable.fromIterable(products)
            .subscribeOn(Schedulers.io())
            .filter { it.productImg.isNotBlank() }
            .map {
                downloadImpl.downloadFile(it.productImg, it.localImg)
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    callback.onProductsLoad(products)
                    delInvalidJob?.cancel()
                    delInvalidJob = GlobalScope.launch {
                        val files = File(FilePath.imgPath)?.listFiles() ?: return@launch
                        val toList = ArrayList(files.toList())
                        products.filter {
                            toList.contains(File(it.localImg))
                        }.forEach {
                            toList.remove(File(it.localImg))
                        }

                        categoryAndProducts.values.forEach { list ->
                            list.filter {
                                toList.contains(File(it.localImg))
                            }.forEach {
                                toList.remove(File(it.localImg))
                            }
                        }

                        toList.forEach {
                            FileUtils.delete(it)
                        }
                    }
                }
            )
    }


    override fun getProduct(deviceWayId: String, callback: ProductDataSource.GetProductCallback) {
        var productEntity: ProductEntity? = null
        if (!products.isNullOrEmpty()) {
            productEntity = products.firstOrNull { it.deviceWayId == deviceWayId }
        } else {
            run breaking@{
                categoryAndProducts.values.forEach continuing@{ list ->
                    productEntity = list.firstOrNull { it.deviceWayId == deviceWayId }
                    //找到了退出查找
                    if (productEntity != null) {
                        return@breaking
                    }
                }
            }

        }
        productEntity?.let {
            callback.onProductLoaded(it)
            return
        }
        callback.onProductNotAvailable()
    }

    override fun getProductFromLimit(
        start: Int,
        count: Int,
        callback: ProductDataSource.LoadProductsCallback
    ) {
        if (products.isNullOrEmpty()) {
            callback.onProductNotAvailable()
        } else {
            val size = products.size
            if (start > size) {
                callback.onProductNotAvailable()
                return
            }
            var endIndex = count
            if (endIndex > size) endIndex = size
            val subList = products.subList(start, endIndex)
            callback.onProductsLoad(subList)
        }
    }

    override fun getProductFromLimitForCategory(
        category: String,
        start: Int,
        count: Int,
        callback: ProductDataSource.LoadProductsCallback
    ) {
        val list = categoryAndProducts[category]
        if (list.isNullOrEmpty()) {
            callback.onProductNotAvailable()
        } else {
            val size = list.size
            if (start > size) {
                callback.onProductNotAvailable()
                return
            }
            var endIndex = start + count
            if (endIndex > size) endIndex = size
            val subList = list.subList(start, endIndex)
            callback.onProductsLoad(subList)
        }
    }

    override fun updateProduct(product: ProductEntity) {
        if (products.isNotEmpty()) {
            val oldProduct = products.firstOrNull { it.deviceWayId == product.deviceWayId }
            oldProduct?.update(product)
//            Collections.sort(products, CompareProduct(true))
        } else {
            val list = categoryAndProducts[product.categoryId]
            if (!list.isNullOrEmpty()) {
                val oldProduct = list.firstOrNull { it.deviceWayId == product.deviceWayId }
                oldProduct?.update(product)
//                Collections.sort(list, CompareProduct(true))
            }
        }
        productLocalDataSource.updateProduct(product)
    }

    override fun refreshProducts() {
        deleteAllProducts()
    }

    override fun deleteAllProducts() {
        categories.clear()
        categoryAndProducts.clear()
        products.clear()
        cacheIsDirty = true
        if (!SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
            productLocalDataSource.deleteAllProducts()
        }
    }

    override fun deleteProduct(deviceWayId: String) {
        if (products.isNotEmpty()) {
            val delProduct = products.firstOrNull { it.deviceWayId == deviceWayId }
            products.remove(delProduct)
        } else {
            run breaking@{
                categoryAndProducts.values.forEach continuing@{ list ->
                    val productEntity = list.firstOrNull { it.deviceWayId == deviceWayId }
                    //找到了退出查找
                    if (productEntity != null) {
                        val list = categoryAndProducts[productEntity.categoryId]
                        if (!list.isNullOrEmpty()) {
                            val delProduct = products.firstOrNull { it.deviceWayId == deviceWayId }
                            products.remove(delProduct)
                            return@breaking
                        }
                    }
                }
            }
        }
        productLocalDataSource.deleteProduct(deviceWayId)
    }

    override fun insertCategories(categories: List<CategoryEntity>) {
        productLocalDataSource.insertCategories(categories)
    }

    override fun insertProducts(products: List<ProductEntity>) {
        productLocalDataSource.insertProducts(products)
    }

    override fun getProductsFromProductId(productID: String): List<ProductEntity> {
        return productLocalDataSource.getProductsFromProductId(productID)
    }


    companion object {
        private var INSTANCE: ProductRepository? = null

        @JvmStatic
        fun getInstance(
            productsLocalDataSource: ProductDataSource.IProductsFromLocal,
            productsRemoteDataSource: ProductDataSource.IProductsFromRemote
        ) =
            INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: ProductRepository(
                            productsLocalDataSource,
                            productsRemoteDataSource
                        )
                            .also { INSTANCE = it }
                }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}