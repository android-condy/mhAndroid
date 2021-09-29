package com.idic.backstagemoudle.ui.adapter

import com.idic.backstagemoudle.viewmodel.CheckInfo
import com.idic.widgetmoudle.adapter.BaseAdapter
import com.idic.widgetmoudle.adapter.ItemViewHolder

/**
 * 文 件 名: CheckInfoAdapter
 * 创 建 人: sineom
 * 创建日期: 2019-08-30 13:41
 * 修改时间：
 * 修改备注：
 */

class CheckInfoAdapter(
    layoutId: Int,
    variableId: Int,
    adapterDatas: List<CheckInfo>
) :
    BaseAdapter<CheckInfo>(layoutId, variableId, adapterDatas) {

    override fun convert(holder: ItemViewHolder, position: Int, checkInfo: CheckInfo) {
        holder.setBinding(variableId, checkInfo)
    }

    fun insertData(checkInfo: CheckInfo) {
        (adapterDatas as ArrayList).add(checkInfo)
        notifyItemInserted(adapterDatas.size - 1)
    }

    fun insertDatas(checkInfos: List<CheckInfo>) {
        (adapterDatas as ArrayList).addAll(checkInfos)
        notifyItemRangeInserted(adapterDatas.size - 1, checkInfos.size)
    }

}
