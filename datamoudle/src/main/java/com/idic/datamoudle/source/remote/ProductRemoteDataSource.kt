package com.idic.datamoudle.source.remote

import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.utils.CustomObserver
import com.idic.datamoudle.db.entity.CategoryEntity
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.datamoudle.source.ProductDataSource
import com.idic.datamoudle.source.remote.request.GetProducts
import com.idic.datamoudle.source.remote.request.GetProductsFormCategory
import com.idic.datamoudle.source.remote.response.LoadProductResponse
import com.idic.httpmoudle.request.DKRequest
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.httpmoudle.utils.RxHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

/**
 * 文 件 名: ProductRemoteDataResource
 * 创 建 人: sineom
 * 创建日期: 2019/1/31 16:35
 * 修改时间：
 * 修改备注：
 */

class ProductRemoteDataSource
private constructor() : ProductDataSource.IProductsFromRemote {


    private var getProductsDisposable: Disposable? = null


    private var getProductsFromCategory: Disposable? = null

    //获取所有的产品
    override fun getProducts(callback: ProductDataSource.LoadProductsCallback) {
        val products = ArrayList<ProductEntity>()
        getProductsDisposable?.dispose()
        RetrofitUtil.instance.getDefautlRetrofit()
            .getProducts(HttpUtils.createBody(GetProducts()))
//            .getProducts1(HttpUtils.createBody(NewProducts()))
            .observeOn(AndroidSchedulers.mainThread())
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    callback.onProductNotAvailable()
                }
                success
            }.observeOn(Schedulers.io())
            .compose(RxHolder.getResponseData<LoadProductResponse>())
            .flatMap {
                products.addAll(it.resultList!!)
                val toInt = it.pageCount
                if (toInt > 1) {
                    Observable.range(2, toInt - 1)
                } else {
                    Observable.empty()
                }
            }.flatMap {
                val getProducts = GetProducts(currentPage = "$it")
                RetrofitUtil.instance.getDefautlRetrofit()
                    .getProducts(HttpUtils.createBody(getProducts))
            }.observeOn(AndroidSchedulers.mainThread())
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    callback.onProductNotAvailable()
                }
                success
            }.compose(RxHolder.getResponseData<LoadProductResponse>())
            .compose(RxHolder.IO2Main())
            .subscribe(CustomObserver<LoadProductResponse>().apply {
                _onComplete {
                    if (products.isEmpty()) {
                        callback.onProductNotAvailable()
                    } else {
                        val validData = ArrayList<ProductEntity>()
                        products.filterTo(validData) { it.stock > 0 }
//                        Collections.sort(validData, CompareProduct(true))
                        callback.onProductsLoad(validData)
                    }
                }
                _onNext { productResponse ->
                    productResponse.resultList?.let {
                        products.addAll(it)
                    }
                }
                _onError {
                    callback.onProductNotAvailable()
                }
                _subscribe {
                    getProductsDisposable = it
                }
            })
    }

    override fun loadProductCategory(callback: ProductDataSource.LoadProductCategory) {
        val dkRequest = DKRequest()
        dkRequest.deviceId = DeviceInfo.getInstance().deviceId
        RetrofitUtil.instance.getDefautlRetrofit()
            .loadCategory(HttpUtils.createBody(dkRequest))
            .observeOn(AndroidSchedulers.mainThread())
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    callback.onCategoryNotAvailabled()
                }
                success
            }.compose(RxHolder.getResponseListData<CategoryEntity>())
            .compose(RxHolder.IO2Main())
            .subscribe(CustomObserver<List<CategoryEntity>>().apply {
                _onNext {
                    if (it.isEmpty()) {
                        callback.onCategoryNotAvailabled()
                    } else {
                        callback.onCategoryLoaded(it)
                    }
                }
                _onError {
                    callback.onCategoryNotAvailabled()
                }
            })
    }

    override fun getProductsFromCategory(
        category: String,
        callback: ProductDataSource.LoadProductsCallback
    ) {
        val getProducts = GetProductsFormCategory(category)
        getProductsFromCategory?.dispose()
        val products = ArrayList<ProductEntity>()
        RetrofitUtil.instance.getDefautlRetrofit()
            .loadProductFormCategory(HttpUtils.createBody(getProducts))
            .observeOn(AndroidSchedulers.mainThread())
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    callback.onProductNotAvailable()
                }
                success
            }.observeOn(Schedulers.io())
            .compose(RxHolder.getResponseData<LoadProductResponse>())
            .flatMap {
                products.addAll(it.resultList!!)
                val toInt = it.pageCount
                if (toInt > 1) {
                    Observable.range(2, toInt - 1)
                } else {
                    Observable.empty()
                }
            }.flatMap {
                val getProducts =
                    GetProductsFormCategory(productCategoryId = category, currentPage = "$it")
                RetrofitUtil.instance.getDefautlRetrofit()
                    .loadProductFormCategory(HttpUtils.createBody(getProducts))
            }.observeOn(AndroidSchedulers.mainThread())
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    callback.onProductNotAvailable()
                }
                success
            }.compose(RxHolder.getResponseData<LoadProductResponse>())
            .compose(RxHolder.IO2Main())
            .subscribe(CustomObserver<LoadProductResponse>().apply {
                _onComplete {
                    if (products.isEmpty()) {
                        callback.onProductNotAvailable()
                    } else {
//                        Collections.sort(products, CompareProduct(true))
                        callback.onProductsLoad(products)
                    }
                }
                _onNext { productResponse ->
                    productResponse.resultList?.let {
                        products.addAll(it)
                    }
                }
                _onError {
                    callback.onProductNotAvailable()
                }
                _subscribe {
                    getProductsFromCategory = it
                }
            })
    }

    companion object {

        private var mInstance: ProductRemoteDataSource? = null

        fun getInstance() = mInstance ?: synchronized(this) {
            mInstance ?: ProductRemoteDataSource().also { mInstance = it }
        }
    }

}