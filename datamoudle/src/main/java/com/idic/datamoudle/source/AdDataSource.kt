package com.idic.datamoudle.source

import com.idic.datamoudle.db.entity.BannerEntity

/**
 * 文 件 名: AdDataSource
 * 创 建 人: sineom
 * 创建日期: 2019-08-07 11:21
 * 修改时间：
 * 修改备注：
 */

interface AdDataSource {

    interface LoadAdDatasCallBack {

        fun onBannerLoaded( banners: List<BannerEntity>)

        fun onAdDataNotAvaliad()
    }

    fun getAds(
        type: String,
        callBack: LoadAdDatasCallBack
    )

    fun saveAds(banners: List<BannerEntity>)

    fun removeAllData()
}