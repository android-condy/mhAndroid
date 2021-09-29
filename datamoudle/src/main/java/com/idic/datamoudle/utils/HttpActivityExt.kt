package com.idic.datamoudle.utils

import android.app.Activity
import com.google.gson.Gson
import com.idic.basecommon.utils.CustomObserver
import com.idic.datamoudle.db.entity.CategoryEntity
import com.idic.datamoudle.db.entity.NewBannerEntity
import com.idic.datamoudle.db.entity.NewCategoryEntity
import com.idic.datamoudle.db.entity.NewProductEntity
import com.idic.datamoudle.source.remote.response.LoadProductResponse
import com.idic.datamoudle.source.repository.BannersDatasCallBack
import com.idic.datamoudle.source.repository.CategoryDatasCallBack
import com.idic.datamoudle.source.repository.ProductDatasCallBack
import com.idic.httpmoudle.request.UploadProductReq
import com.idic.httpmoudle.response.BaseResponse
import com.idic.httpmoudle.response.DKResponse
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.httpmoudle.utils.RxHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 文 件 名: ActivityExt
 * 创 建 人: sineom
 * 创建日期: 2019-09-11 11:43
 * 修改时间：
 * 修改备注：
 */

fun Activity.uploadChannelInfo(uploadProductReq: UploadProductReq) {
    RetrofitUtil.instance.getDefautlRetrofit()
        .modifyChannelInfo(HttpUtils.createBody(uploadProductReq))
        .subscribeOn(Schedulers.io())
        .subscribe(CustomObserver<DKResponse>())
}

class HttpActivityExt {

    companion object {

        fun getMainBanners(callback: BannersDatasCallBack) {
            RetrofitUtil.instance.getDefautlRetrofit()
                .getMainBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    val success = it.isSuccess() && it.data != null
                    if (!success) {
                        callback.onBannerNotData()
                    }
                    success
                }.compose(RxHolder.getResponseListData2<NewBannerEntity>())
                .subscribe(CustomObserver<List<NewBannerEntity>>().apply {
                    _onNext {
                        if (it.isEmpty()) {
                            callback.onBannerNotData()
                        } else {
                            callback.onBannerDataLoaded(it)
                        }
                    }
                    _onError {
                        callback.onBannerNotData()
                    }
                })
        }

        fun getCustomerServiceButtomBanner(callback: BannersDatasCallBack) {
            RetrofitUtil.instance.getDefautlRetrofit()
                .getCustomerServiceButtomBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    val success = it.isSuccess() && it.data != null
                    if (!success) {
                        callback.onBannerNotData()
                    }
                    success
                }.compose(RxHolder.getResponseListData2<NewBannerEntity>())
                .subscribe(CustomObserver<List<NewBannerEntity>>().apply {
                    _onNext {
                        if (it.isEmpty()) {
                            callback.onBannerNotData()
                        } else {
                            callback.onBannerDataLoaded(it)
                        }
                    }
                    _onError {
                        callback.onBannerNotData()
                    }
                })
        }

        fun getButtomBanners(callback: BannersDatasCallBack) {
            RetrofitUtil.instance.getDefautlRetrofit()
                .getButtomBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    val success = it.isSuccess() && it.data != null
                    if (!success) {
                        callback.onBannerNotData()
                    }
                    success
                }.compose(RxHolder.getResponseListData2<NewBannerEntity>())
                .subscribe(CustomObserver<List<NewBannerEntity>>().apply {
                    _onNext {
                        if (it.isEmpty()) {
                            callback.onBannerNotData()
                        } else {
                            callback.onBannerDataLoaded(it)
                        }
                    }
                    _onError {
                        callback.onBannerNotData()
                    }
                })
        }
        fun getGoodsSuccessBanners(callback: BannersDatasCallBack) {
            RetrofitUtil.instance.getDefautlRetrofit()
                .getGoodsSuccessBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    val success = it.isSuccess() && it.data != null
                    if (!success) {
                        callback.onBannerNotData()
                    }
                    success
                }.compose(RxHolder.getResponseListData2<NewBannerEntity>())
                .subscribe(CustomObserver<List<NewBannerEntity>>().apply {
                    _onNext {
                        if (it.isEmpty()) {
                            callback.onBannerNotData()
                        } else {
                            callback.onBannerDataLoaded(it)
                        }
                    }
                    _onError {
                        callback.onBannerNotData()
                    }
                })
        }


        fun getGoods(callback: ProductDatasCallBack,type_id:String="") {
            RetrofitUtil.instance.getDefautlRetrofit()
                .getGoods(type_id=type_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    val success = it.isSuccess() && it.data != null
                    if (!success) {
                        callback.onProductNotData()
                    }
                    success
                }.compose(RxHolder.getResponseListData2<NewProductEntity>())
                .subscribe(CustomObserver<List<NewProductEntity>>().apply {
                    _onNext {
                        if (it.isEmpty()) {
                            callback.onProductNotData()
                        } else {
                            callback.onProductDataLoaded(it)
                        }
                    }
                    _onError {
                        callback.onProductNotData()
                    }
                })
        }

        fun getGoodsCategory(callback: CategoryDatasCallBack) {
            RetrofitUtil.instance.getDefautlRetrofit()
                .getGoodsCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    val success = it.isSuccess() && it.data != null
                    if (!success) {
                        callback.onCategoryNotData()
                    }
                    success
                }.compose(RxHolder.getResponseListData2<NewCategoryEntity>())
                .subscribe(CustomObserver<List<NewCategoryEntity>>().apply {
                    _onNext {
                        if (it.isEmpty()) {
                            callback.onCategoryNotData()
                        } else {
                            callback.onCategoryDataLoaded(it)
                        }
                    }
                    _onError {
                        callback.onCategoryNotData()
                    }
                })
        }

    }


}