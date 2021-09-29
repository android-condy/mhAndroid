package com.idic.datamoudle.source.local

import com.blankj.utilcode.util.SPUtils
import com.idic.basecommon.AppExecutors
import com.idic.basecommon.SPKeys
import com.idic.basecommon.transactIO
import com.idic.basecommon.transcatMain
import com.idic.datamoudle.db.dao.CategoryDao
import com.idic.datamoudle.db.dao.ProductsDao
import com.idic.datamoudle.db.entity.CategoryEntity
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.datamoudle.source.ProductDataSource

/**
 * 文 件 名: ProductLocalDataSource
 * 创 建 人: sineom
 * 创建日期: 2019-08-07 13:43
 * 修改时间：
 * 修改备注：
 */

internal class ProductLocalDataSource private constructor(
    val appExecutors: AppExecutors,
    val productsDao: ProductsDao,
    val categoryDao: CategoryDao? = null
) : ProductDataSource.IProductsFromLocal {
    override fun getProductsFromProductId(productID: String): List<ProductEntity> {
        return productsDao.getTheSameProductPay(productID)
    }

    override fun loadProductCategory(callback: ProductDataSource.LoadProductCategory) {
        appExecutors.transactIO {
            val allProducts = categoryDao?.getCategories()
            appExecutors.transcatMain {
                if (allProducts.isNullOrEmpty()) {
                    callback.onCategoryNotAvailabled()
                } else {
                    callback.onCategoryLoaded(allProducts)
                }
            }
        }
    }

    override fun getProducts(callback: ProductDataSource.LoadProductsCallback) {
        appExecutors.transactIO {
            val allProducts = productsDao.getAllProducts()
            appExecutors.transcatMain {
                if (allProducts.isNullOrEmpty()) {
                    callback.onProductNotAvailable()
                } else {
                    val validData = ArrayList<ProductEntity>()
                    allProducts.filterTo(validData) { it.stock > 0 }
                    callback.onProductsLoad(validData)
                }
            }
        }
    }

    override fun getProductsFromCategory(
        category: String,
        callback: ProductDataSource.LoadProductsCallback
    ) {
        appExecutors.transactIO {
            val productsFromCategory = categoryDao?.getProductsFromCategory(category)
            appExecutors.transcatMain {
                if (productsFromCategory.isNullOrEmpty()) {
                    callback.onProductNotAvailable()
                } else {
                    callback.onProductsLoad(productsFromCategory)
                }
            }
        }
    }

    override fun getProduct(deviceWayId: String, callback: ProductDataSource.GetProductCallback) {
        appExecutors.transactIO {
            val productForDeviceWayID = productsDao.getProductForDeviceWayID(deviceWayId)
            appExecutors.transcatMain {
                productForDeviceWayID?.let {
                    callback.onProductLoaded(it)
                    return@transcatMain
                }
                callback.onProductNotAvailable()
            }
        }

    }

    override fun getProductFromLimit(
        start: Int,
        count: Int,
        callback: ProductDataSource.LoadProductsCallback
    ) {
        appExecutors.transactIO {
            val productsFromLimit = productsDao.getProductsFromLimit(start, count)
            appExecutors.transcatMain {
                if (productsFromLimit.isEmpty()) {
                    callback.onProductNotAvailable()
                } else {
                    callback.onProductsLoad(productsFromLimit)
                }
            }
        }
    }

    override fun getProductFromLimitForCategory(
        category: String,
        start: Int,
        count: Int,
        callback: ProductDataSource.LoadProductsCallback
    ) {
        appExecutors.transactIO {
            val productsFromCategoryForLimit =
                categoryDao?.getProductsFromCategoryForLimit(start, count, category)
            appExecutors.transcatMain {
                if (productsFromCategoryForLimit.isNullOrEmpty()) {
                    callback.onProductNotAvailable()
                } else {
                    callback.onProductsLoad(productsFromCategoryForLimit)
                }
            }
        }

    }

    override fun updateProduct(product: ProductEntity) {
        appExecutors.transactIO {
            productsDao.updateProduct(product)
        }
    }

    override fun refreshProducts() {
        if (!SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
            deleteAllProducts()
        }
    }

    override fun deleteAllProducts() {
        appExecutors.transactIO {
            productsDao.removeAllProducts()
            categoryDao?.removeCategories()
        }
    }

    override fun deleteProduct(deviceWayId: String) {
        appExecutors.transactIO {
            productsDao.deleteProduct(deviceWayId)
        }
    }

    override fun insertCategories(categories: List<CategoryEntity>) {
        appExecutors.transactIO {
            categoryDao?.insertCategories(categories)
        }
    }

    override fun insertProducts(products: List<ProductEntity>) {
        appExecutors.transactIO {
            productsDao.insertProducts(products)
        }
    }

    companion object {
        private var INSTANCE: ProductLocalDataSource? = null

        @JvmStatic
        fun getInstance(
            productsDao: ProductsDao,
            categoryDao: CategoryDao
        ) =
            INSTANCE
                ?: synchronized(this) {
                    INSTANCE ?: ProductLocalDataSource(
                        AppExecutors(),
                        productsDao,
                        categoryDao
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
