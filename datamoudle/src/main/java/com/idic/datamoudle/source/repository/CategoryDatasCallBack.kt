package com.idic.datamoudle.source.repository

import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.db.entity.NewBannerEntity
import com.idic.datamoudle.db.entity.NewCategoryEntity
import com.idic.datamoudle.db.entity.NewProductEntity

/**
 * Author: condy
 * Date: 2021/3/12 14:22
 * Description: ${DESCRIPTION}
 */
interface CategoryDatasCallBack {

    fun onCategoryDataLoaded(banners: List<NewCategoryEntity>)

    fun onCategoryNotData()
}