package com.idic.blindbox.listener

import android.view.View
import com.idic.datamoudle.db.entity.CategoryEntity
import com.idic.datamoudle.db.entity.NewCategoryEntity
import com.idic.datamoudle.db.entity.NewProductEntity
import com.idic.datamoudle.db.entity.ProductEntity

/**
 * 文 件 名: ItemClickListener
 * 创 建 人: sineom
 * 创建日期: 2019/3/13 17:49
 * 修改时间：
 * 修改备注：
 */

interface ItemClickListener {

    fun onClick(view: View, product: NewProductEntity)


}

//类目点击事件
interface CategoryItemClick {

    fun categoryClick(categoryEntity: NewCategoryEntity)
}
