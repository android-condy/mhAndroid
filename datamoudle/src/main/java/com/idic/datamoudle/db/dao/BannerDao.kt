package com.idic.datamoudle.db.dao

import android.arch.persistence.room.*
import com.idic.datamoudle.db.entity.BannerEntity

/**
 * 文 件 名: BannerDao
 * 创 建 人: sineom
 * 创建日期: 2019-08-06 13:55
 * 修改时间：
 * 修改备注： 广告实体类
 */

@Dao
interface BannerDao {

    //删除所有的广告
    @Query("delete from BannerEntity")
    fun deleteAllBanner()

    @Query("select * from BannerEntity where BannerEntity.type=:type")
    fun getAllBanner(type: Int): List<BannerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(banners: List<BannerEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateBanners(banners: List<BannerEntity>)
}