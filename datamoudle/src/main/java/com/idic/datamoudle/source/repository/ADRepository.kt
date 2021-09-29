package com.idic.datamoudle.source.repository

import android.util.Log
import com.blankj.utilcode.util.FileUtils
import com.idic.basecommon.utils.FilePath
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.source.AdDataSource
import com.idic.httpmoudle.URL
import retrofit2.http.Url
import java.io.File

/**
 * 文 件 名: ADRepository
 * 创 建 人: sineom
 * 创建日期: 2019-08-07 15:38
 * 修改时间：
 * 修改备注：
 */

class ADRepository(
    private val adDataLocalSource: AdDataSource,
    private val adDataRemoteDataSource: AdDataSource
) : AdDataSource {


    override fun getAds(
        type: String,
        callBack: AdDataSource.LoadAdDatasCallBack
    ) {
        adDataLocalSource.getAds(type, object : AdDataSource.LoadAdDatasCallBack {
            override fun onBannerLoaded(banners: List<BannerEntity>) {
                callBack.onBannerLoaded(banners)
            }

            override fun onAdDataNotAvaliad() {
                adDataRemoteDataSource.getAds(type, object : AdDataSource.LoadAdDatasCallBack {
                    override fun onBannerLoaded(banners: List<BannerEntity>) {
                        if (banners.isEmpty()){
                            callBack.onAdDataNotAvaliad()
                            return
                        }
                        saveAds(banners)
                        callBack.onBannerLoaded(banners)
                    }

                    override fun onAdDataNotAvaliad() {
                        callBack.onAdDataNotAvaliad()
                    }

                })
            }

        })
    }

    fun getLocalBanner(type: String,callBack: AdDataSource.LoadAdDatasCallBack) {
        adDataLocalSource.getAds(type, callBack)
    }

    override fun removeAllData() {
        adDataLocalSource.removeAllData()
    }

    override fun saveAds(banners: List<BannerEntity>) {
        adDataLocalSource.saveAds(banners)
    }
}