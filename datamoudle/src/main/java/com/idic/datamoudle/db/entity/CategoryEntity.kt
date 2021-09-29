package com.idic.datamoudle.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * 文 件 名: ProductCategory
 * 创 建 人: sineom
 * 创建日期: 2019-04-29 10:33
 * 修改时间：
 * 修改备注：产品类目实体类
 */

@Entity(indices = [Index("categoryId")])
data class CategoryEntity(

    //类目名称
    var categoryName: String,

    //图片地址
    var url: String?,


    var message: String?,

    //状态
    var status: String?,

    var sort: Int?,
    //类目id
    @PrimaryKey
    var categoryId: String
)