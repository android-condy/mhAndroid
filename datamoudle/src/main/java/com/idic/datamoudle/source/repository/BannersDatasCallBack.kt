package com.idic.datamoudle.source.repository

import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.db.entity.NewBannerEntity

/**
 * Author: condy
 * Date: 2021/3/12 14:22
 * Description: ${DESCRIPTION}
 */
  interface BannersDatasCallBack {

    fun onBannerDataLoaded(banners: List<NewBannerEntity>)

    fun onBannerNotData()
}