package com.idic.datamoudle.db.dao

import android.arch.persistence.room.*
import com.idic.datamoudle.db.entity.ProductEntity

/**
 * 文 件 名: Products
 * 创 建 人: sineom
 * 创建日期: 2019-08-06 13:37
 * 修改时间：
 * 修改备注：
 */
@Dao
interface ProductsDao {

    //获取所有的产品
    @Query("select * from ProductEntity")
    fun getAllProducts(): List<ProductEntity>

    //在所有产品中分页查找数据
    @Query("select * from ProductEntity limit :start AND :count")
    fun getProductsFromLimit(start: Int, count: Int): List<ProductEntity>

    //根据货道ID获取对应的产品
    @Query("select * from ProductEntity  where ProductEntity.deviceWayId=:deviceWayId")
    fun getProductForDeviceWayID(deviceWayId: String): ProductEntity?

    //根据货道编号获取对应的产品
    @Query("select * from ProductEntity where ProductEntity.num=:num")
    fun getAllProductFromNum(num: Int): List<ProductEntity>

    //删除所有的产品
    @Query("delete from ProductEntity")
    fun removeAllProducts()

    //更新指定的数据
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateProduct(productEntity: ProductEntity)

    //插入数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<ProductEntity>)

    //删除指定的产品
    @Delete
    fun deleteProduct(productEntity: ProductEntity)


    //根据货道id删除数据
    @Query("delete from ProductEntity where ProductEntity.deviceWayId=:deviceWayId")
    fun deleteProduct(deviceWayId: String)

    //根据产品id获取相同产品且产品库存大于0
    @Query("select * from ProductEntity where ProductEntity.productId=:productID and ProductEntity.stock > 0")
    fun getTheSameProductPay(productID: String): List<ProductEntity>

    //根据产品id获取相同产品
    @Query("select * from ProductEntity where ProductEntity.productId=:productID")
    fun getAllTheSameProduct(productID: String): List<ProductEntity>

}