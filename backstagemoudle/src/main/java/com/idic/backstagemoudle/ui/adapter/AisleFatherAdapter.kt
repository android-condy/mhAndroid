package com.idic.backstagemoudle.ui.adapter

import android.util.Log
import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.boxtest.data.Aisle
import com.idic.backstagemoudle.db.entity.AisleConfigEntity
import com.idic.backstagemoudle.db.entity.AislesBindGoodsEntity
import com.idic.widgetmoudle.adapter.BaseAdapter
import com.idic.widgetmoudle.adapter.BaseAdapter2
import com.idic.widgetmoudle.adapter.ItemViewHolder
import com.idic.widgetmoudle.adapter.ItemViewHolder2

/**
 * 文 件 名: BackStageAdapter
 * 创 建 人: sineom
 * 创建日期: 2019-08-21 14:35
 * 修改时间：
 * 修改备注：
 */

class AisleFatherAdapter(
    layoutId: Int,
    variableId: Int,
    adapterDatas: List<Map<String, List<Aisle>>>,
    private var listener: ((holder: ItemViewHolder2, Map<String, List<Aisle>>) -> Unit)? = null
) : BaseAdapter2<Map<String, List<Aisle>>>(layoutId, variableId, adapterDatas) {
    override fun convert(
        holder: ItemViewHolder2,
        position: Int,
        t: Map<String, List<Aisle>>
    ) {
        listener?.invoke(holder, t)

    }

}