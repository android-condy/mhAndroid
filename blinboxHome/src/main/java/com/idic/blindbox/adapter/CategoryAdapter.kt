package com.idic.blindbox.adapter

import com.idic.blindbox.BR
import com.idic.blindbox.R
import com.idic.datamoudle.db.entity.CategoryEntity
import com.idic.datamoudle.db.entity.NewCategoryEntity
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.widgetmoudle.adapter.BaseAdapter
import com.idic.widgetmoudle.adapter.ItemViewHolder

/**
 * 文 件 名: LuckBagAdapter
 * 创 建 人: sineom
 * 创建日期: 2019/3/5 17:38
 * 修改时间：
 * 修改备注：
 */

class CategoryAdapter(
    datas: ArrayList<NewCategoryEntity>,
    private var listener: ((holder: ItemViewHolder) -> Unit)? = null
) : BaseAdapter<NewCategoryEntity>(R.layout.activity_bind_category_item, BR.category, datas) {


    override fun convert(holder: ItemViewHolder, position: Int, t: NewCategoryEntity) {
        holder.setBinding(variableId, t)
        listener?.invoke(holder)
    }
}