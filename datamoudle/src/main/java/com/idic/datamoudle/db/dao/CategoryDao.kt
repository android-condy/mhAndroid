package com.idic.datamoudle.db.dao

import android.arch.persistence.room.*
import com.idic.datamoudle.db.entity.CategoryEntity
import com.idic.datamoudle.db.entity.ProductEntity

/**
 * 文 件 名: Products
 * 创 建 人: sineom
 * 创建日期: 2019-08-06 13:37
 * 修改时间：
 * 修改备注：
 */
@Dao
interface CategoryDao {

    //获取所有的类目
    @Query("select * from CategoryEntity")
    fun getCategories(): List<CategoryEntity>

    //删除所有的产品
    @Query("delete  from CategoryEntity")
    fun removeCategories()

    //更新指定的数据
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCategory(categoryEntity: CategoryEntity)

    //插入数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(categoryEntities: List<CategoryEntity>)

    //删除指定的产品
    @Delete
    fun deleteCategory(categoryEntity: CategoryEntity)

    //根据类目id获取该类目下的所有产品
    @Query("select * from ProductEntity  where ProductEntity.categoryId=:category")
    fun getProductsFromCategory(category: String): List<ProductEntity>

    //在指定的类目中分页查找所有数据
    @Query("select * from ProductEntity  where ProductEntity.categoryId=:category limit :start AND :count")
    fun getProductsFromCategoryForLimit(
        start: Int,
        count: Int,
        category: String
    ): List<ProductEntity>
}