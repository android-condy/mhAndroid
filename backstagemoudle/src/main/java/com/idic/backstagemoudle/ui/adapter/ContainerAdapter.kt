package com.idic.backstagemoudle.ui.adapter

import com.idic.backstagemoudle.db.entity.ContainerConfigEntity
import com.idic.widgetmoudle.adapter.BaseAdapter
import com.idic.widgetmoudle.adapter.ItemViewHolder

/**
 * 文 件 名: BackStageAdapter
 * 创 建 人: sineom
 * 创建日期: 2019-08-21 14:35
 * 修改时间：
 * 修改备注：
 */

class ContainerAdapter(
    layoutId: Int,
    variableId: Int,
    adapterDatas: List<ContainerConfigEntity>,
    private var listener: ((holder: ItemViewHolder,container:ContainerConfigEntity) -> Unit)? = null
) : BaseAdapter<ContainerConfigEntity>(layoutId, variableId, adapterDatas) {
    override fun convert(holder: ItemViewHolder, position: Int, t: ContainerConfigEntity) {
        holder.setBinding(variableId, t)
        listener?.invoke(holder,t)
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int, newData: List<ContainerConfigEntity>): Boolean {
        val oldContainer = adapterDatas[oldItemPosition]
        val newContainer = newData[newItemPosition]
        return newContainer.num == oldContainer.num
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
        newData: List<ContainerConfigEntity>
    ): Boolean {
        val oldContainer = adapterDatas[oldItemPosition]
        val newContainer = newData[newItemPosition]
        return newContainer.num == oldContainer.num &&
                newContainer.containerName == oldContainer.containerName &&
                newContainer.openLayer == oldContainer.openLayer &&
                newContainer.maxLayer == oldContainer.maxLayer &&
                newContainer.screenType == oldContainer.screenType
    }
}