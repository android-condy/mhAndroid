package com.idic.backstagemoudle.ui.adapter

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

class StockAisleAdapter(
    layoutId: Int,
    variableId: Int,
    adapterDatas: List<AislesBindGoodsEntity>,
    private var listener: ((holder: ItemViewHolder2,item:AislesBindGoodsEntity) -> Unit
    )? = null
) : BaseAdapter2<AislesBindGoodsEntity>(layoutId, variableId, adapterDatas) {
    override fun convert(holder: ItemViewHolder2, position: Int, t: AislesBindGoodsEntity) {
//        holder.setBinding(variableId, t)
        listener?.invoke(holder, t)
    }

//    override fun areItemsTheSame(
//        oldItemPosition: Int,
//        newItemPosition: Int,
//        newData: List<AisleConfigEntity>
//    ): Boolean {
//        val oldAisle = adapterDatas[oldItemPosition]
//        val newAisle = newData[newItemPosition]
//        return newAisle.num == oldAisle.num && newAisle.channelNumber == oldAisle.channelNumber
//    }

//    override fun areContentsTheSame(
//        oldItemPosition: Int,
//        newItemPosition: Int,
//        newData: List<AisleConfigEntity>
//    ): Boolean {
//        val oldAisle = adapterDatas[oldItemPosition]
//        val newAisle = newData[newItemPosition]
//        return newAisle.num == oldAisle.num &&
//                newAisle.channelNumber == oldAisle.channelNumber &&
//                newAisle.motorType == oldAisle.motorType &&
//                newAisle.tempStock == oldAisle.tempStock &&
//                newAisle.stock == oldAisle.stock &&
//                newAisle.tempLockStock == oldAisle.tempLockStock &&
//                newAisle.lockStock == oldAisle.lockStock &&
//                newAisle.tempCapacity == oldAisle.tempCapacity &&
//                newAisle.capacity == oldAisle.capacity
//    }
}