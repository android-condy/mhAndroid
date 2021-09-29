package com.idic.backstagemoudle.boxtest.view

import android.util.Log
import com.idic.backstagemoudle.BR
import com.idic.backstagemoudle.R
import com.idic.backstagemoudle.boxtest.adapter.BaseAdapter
import com.idic.backstagemoudle.boxtest.adapter.ItemViewHolder
import com.idic.backstagemoudle.boxtest.data.Aisle

/**
 * 文 件 名: AisleAdapter
 * 创 建 人: sineom
 * 创建日期: 2020/6/18 16:25
 * 修改时间：
 * 修改备注：
 */

class AisleAdapter( private var listener: ((holder: ItemViewHolder) -> Unit)? = null) : BaseAdapter<Aisle>(
    R.layout.test_aisle_item, BR.aisle, ArrayList<Aisle>()) {
    override fun convert(holder: ItemViewHolder, position: Int, t: Aisle) {
        holder.setBinding(variableId, t)
        listener?.invoke(holder)
    }

}