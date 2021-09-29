package com.idic.datamoudle.source.remote

import android.annotation.SuppressLint
import android.support.annotation.VisibleForTesting
import com.idic.basecommon.utils.CustomObserver
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.source.AdDataSource
import com.idic.datamoudle.source.remote.request.GetBannerAds
import com.idic.datamoudle.utils.DownloadVideoTask
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.httpmoudle.utils.RxHolder
import io.reactivex.disposables.Disposable

/**
 * 文 件 名: AdRemoteDataResource
 * 创 建 人: sineom
 * 创建日期: 2019/3/5 11:03
 * 修改时间：
 * 修改备注：
 */

internal class AdRemoteDataSource private constructor() : AdDataSource {


    private var dispose: Disposable? = null

    @SuppressLint("CheckResult")
    override fun getAds(
        type: String,
        callBack: AdDataSource.LoadAdDatasCallBack
    ) {
        dispose?.dispose()
        RetrofitUtil.instance.getDefautlRetrofit()
            .getBanner(HttpUtils.createBody(GetBannerAds(type)))
            .compose(RxHolder.IO2Main())
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    callBack.onAdDataNotAvaliad()
                }
                success
            }.compose(RxHolder.getResponseListData<BannerEntity>())
            .subscribe(CustomObserver<List<BannerEntity>>().apply {
                _onNext {
                    DownloadVideoTask.downloadVideo(it, callBack)
//                    callBack.onBannerLoaded(it)
                }
                _onComplete {

                }
                _onError {
                    callBack.onAdDataNotAvaliad()
                }
            })
    }

    override fun removeAllData() {
        //pass
    }

    override fun saveAds(banners: List<BannerEntity>) {
        //pass
    }


    companion object {

        private var instance: AdRemoteDataSource? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: AdRemoteDataSource().also { instance = it }
            }

        @VisibleForTesting
        fun clearInstance() {
            instance = null
        }
    }
}