package com.idic.datamoudle.source.local

import com.idic.basecommon.AppExecutors
import com.idic.basecommon.transactIO
import com.idic.basecommon.transcatMain
import com.idic.datamoudle.db.dao.BannerDao
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.source.AdDataSource

/**
 * 文 件 名: ADDataLocalSource
 * 创 建 人: sineom
 * 创建日期: 2019-08-07 15:29
 * 修改时间：
 * 修改备注：
 */

class ADDataLocalSource private constructor(
    val bannerDao: BannerDao,
    val appExecutors: AppExecutors
) : AdDataSource {


    override fun getAds(
        type: String,
        callBack: AdDataSource.LoadAdDatasCallBack
    ) {
        appExecutors.transactIO {
            val allBanner = bannerDao.getAllBanner(type.toInt())
            appExecutors.transcatMain {
                if (allBanner.isNotEmpty()) {
                    callBack.onBannerLoaded(allBanner)
                } else {
                    callBack.onAdDataNotAvaliad()
                }
            }
        }
    }

    override fun saveAds(banners: List<BannerEntity>) {
        appExecutors.transactIO {
            bannerDao.insert(banners)
        }
    }

    override fun removeAllData() {
        appExecutors.transactIO {
            bannerDao.deleteAllBanner()
        }
    }

    companion object {
        private var INSTANCE: ADDataLocalSource? = null

        @JvmStatic
        fun getInstance(
            bannerDao: BannerDao
        ) =
            INSTANCE
                ?: synchronized(this) {
                    INSTANCE ?: ADDataLocalSource(
                        bannerDao,
                        AppExecutors()

                    ).also { INSTANCE = it }
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