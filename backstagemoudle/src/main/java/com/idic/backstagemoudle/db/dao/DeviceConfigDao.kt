package com.idic.backstagemoudle.db.dao

import android.arch.persistence.room.*
import com.idic.backstagemoudle.db.entity.DeviceConfigEntity

/**
 * 文 件 名: DeviceConfigDao
 * 创 建 人: sineom
 * 创建日期: 2019-08-21 15:45
 * 修改时间：
 * 修改备注：
 */
@Dao
interface DeviceConfigDao {


    //插入单个
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContainer(deviceConfigEntity: DeviceConfigEntity)

    //更新指定的数据，如果不存在则会插入
    @Update
    fun updateContainer(deviceConfigEntity: DeviceConfigEntity)

    //删除指定
    @Delete
    fun deleteContainer(deviceConfigEntity: DeviceConfigEntity)

    //删除所有的产品
    @Query("delete from DeviceConfigEntity")
    fun removeAllProducts()

    //获取所有的系统配置
    @Query("select * from DeviceConfigEntity")
    fun getAllConfig(): List<DeviceConfigEntity>
}