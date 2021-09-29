package com.idic.backstagemoudle.ui.adapter

import com.idic.widgetmoudle.adapter.BaseAdapter
import com.idic.widgetmoudle.adapter.ItemViewHolder

/**
 * 文 件 名: BackStageAdapter
 * 创 建 人: sineom
 * 创建日期: 2019-08-21 14:35
 * 修改时间：
 * 修改备注：
 */

class BackStageAdapter(
    layoutId: Int,
    variableId: Int,
    adapterDatas: List<String>,
    private var listener: ((holder: ItemViewHolder) -> Unit)? = null
) : BaseAdapter<String>(layoutId, variableId, adapterDatas) {
    override fun convert(holder: ItemViewHolder, position: Int, t: String) {
        holder.setBinding(variableId, t)
        listener?.invoke(holder)
    }
}