package com.idic.blindbox.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.service.DeviceConfigServer
import com.idic.basecommon.utils.ARouterConfig
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.datamoudle.source.AdDataSource
import com.idic.datamoudle.source.ProductDataSource
import com.idic.datamoudle.source.repository.ADRepository
import com.idic.datamoudle.source.repository.ProductRepository
import com.idic.httpmoudle.URL
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 文 件 名: MainModel
 * 创 建 人: sineom
 * 创建日期: 2019/2/21 17:40
 * 修改时间：
 * 修改备注：
 */

class MainModel(
    context: Application,
    private val productsRepository: ProductRepository,
    private val adRepository: ADRepository
) : AndroidViewModel(context) {

    val dataLoading = ObservableBoolean(false)

    val empty = ObservableBoolean(false)

    //购物车最大数量
    var mShopCarMax = 3
        private set

    //购物车是否开启
    val isOpenShopCar = ObservableBoolean(false)

    val hideTheSameProduct = ObservableBoolean(false)

    init {

        initConfig()
    }

    private fun initConfig() {

        val carService = DeviceInfo.getInstance().deviceConfig

        mShopCarMax = carService.shopcarMax
        isOpenShopCar.set(carService.openShopCar)
        hideTheSameProduct.set(carService.hideAsProduct)
        GlobalScope.launch {
            val configServer = ARouter.getInstance().navigation(DeviceConfigServer::class.java)
            configServer.setMainPath(ARouterConfig.CATEGORY_HOME)
        }
    }

    fun startAllPrd(forceUpdate: Boolean, call: ProductDataSource.LoadProductsCallback) {
        loadProductsAllPrd(forceUpdate, call)
    }

    fun loadProductsAllPrd(forceUpdate: Boolean, call: ProductDataSource.LoadProductsCallback) {
        loadProducts(forceUpdate, true, call)
    }

    /**
     * @param forceUpdate 从服务器获取数据
     * @param showLoadingUI 是否展示loading图
     */
    fun loadProducts(
        forceUpdate: Boolean,
        showLoadingUI: Boolean,
        call: ProductDataSource.LoadProductsCallback
    ) {

        dataLoading.set(showLoadingUI)

        if (forceUpdate) {
            productsRepository.refreshProducts()
        }

        productsRepository.getProducts(object : ProductDataSource.LoadProductsCallback {
            override fun onProductsLoad(products: List<ProductEntity>) {
                call.onProductsLoad(products)
                dataLoading.set(false)
                empty.set(false)
            }

            override fun onProductNotAvailable() {
                call.onProductNotAvailable()
                dataLoading.set(false)
                empty.set(true)
            }
        })

    }

    fun start(call: ProductDataSource.LoadProductCategory) {
//        productsRepository.refreshProducts()
        productsRepository.loadProductCategory(call)
    }

    fun getProductFromCategory(
        categoryId: String,
        forceUpdate: Boolean,
        call: ProductDataSource.LoadProductsCallback
    ) {
        loadProducts(categoryId, forceUpdate, false, call)
    }


    /**
     * @param forceUpdate 从服务器获取数据
     * @param showLoadingUI 是否展示loading图
     */
    private fun loadProducts(
        categoryId: String,
        forceUpdate: Boolean,
        showLoadingUI: Boolean,
        call: ProductDataSource.LoadProductsCallback
    ) {

        dataLoading.set(showLoadingUI)
        empty.set(false)
        if (forceUpdate) {
            productsRepository.refreshProducts()
        }
        productsRepository.getProductsFromCategory(
            categoryId,
            object : ProductDataSource.LoadProductsCallback {
                override fun onProductsLoad(products: List<ProductEntity>) {
                    call.onProductsLoad(products)
                    dataLoading.set(false)
                    empty.set(false)
                }

                override fun onProductNotAvailable() {
                    call.onProductNotAvailable()
                    dataLoading.set(false)
                    empty.set(true)
                }
            })

    }

    fun getProduct(deviceWayId: String, callback: ProductDataSource.GetProductCallback) {
        productsRepository.getProduct(deviceWayId, callback)
    }

    fun getHadStockProducts(productId: String): List<ProductEntity> {
        return productsRepository.getProductsFromProductId(productId)
    }


    fun getProductLimit(
        categoryId: String, startIndex: Int, count: Int,
        call: ProductDataSource.LoadProductsCallback
    ) {
        productsRepository.getProductFromLimitForCategory(categoryId, startIndex, count, call)
    }


    //根据配置封装不同的产品
    private fun packProducts(products: List<ProductEntity>): List<ProductEntity> {
        return if (hideTheSameProduct.get()) {

            val productEntity = products.first()

            val newProducts = ArrayList<ProductEntity>().apply {
                add(productEntity)
            }
            val productIds = ArrayList<String>().apply {
                add(productEntity.productId)
            }

            products.filterTo(newProducts) {
                val productId = it.productId
                val theSame = !productIds.contains(productId)
                if (theSame) {
                    productIds.add(productId)
                }
                theSame
            }
            newProducts
        } else {
            products
        }
    }

    fun removeAllPrd() {
        productsRepository.refreshProducts()
    }
    fun removeAllAd(){
        adRepository.removeAllData()
    }

    fun loadAd(callBack: AdDataSource.LoadAdDatasCallBack, type: String = "1") {
        adRepository.getAds(type, callBack)
    }

}
