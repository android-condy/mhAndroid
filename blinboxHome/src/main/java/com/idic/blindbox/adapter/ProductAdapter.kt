package com.idic.blindbox.adapter

import com.idic.blindbox.BR
import com.idic.datamoudle.db.entity.NewProductEntity
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

class ProductAdapter(
    layoutId: Int,
    variableId: Int,
    datas: ArrayList<NewProductEntity>,
    private var listener: ((holder: ItemViewHolder) -> Unit)? = null
) : BaseAdapter<NewProductEntity>(layoutId, variableId, datas) {


    override fun convert(holder: ItemViewHolder, position: Int, t: NewProductEntity) {
        holder.setBinding(variableId, t)
        listener?.invoke(holder)
    }

//    override fun areItemsTheSame(
//        oldItemPosition: Int,
//        newItemPosition: Int,
//        newData: List<ProductEntity>
//    ): Boolean {
//        val oldPrd = adapterDatas[oldItemPosition]
//        val newPrd = newData[newItemPosition]
//        return newPrd.productId == oldPrd.productId && newPrd.deviceWayId == oldPrd.deviceWayId
//    }

//    override fun areContentsTheSame(
//        oldItemPosition: Int,
//        newItemPosition: Int,
//        newData: List<ProductEntity>
//    ): Boolean {
//        val oldPrd = adapterDatas[oldItemPosition]
//        val newPrd = newData[newItemPosition]
//        return newPrd.number == oldPrd.number &&
//                newPrd.lockStock == oldPrd.lockStock &&
//                newPrd.productId == oldPrd.productId &&
//                newPrd.deviceWayId == oldPrd.deviceWayId &&
//                newPrd.num == oldPrd.num &&
//                newPrd.productImg == oldPrd.productImg &&
//                newPrd.synopsis == oldPrd.synopsis &&
//                newPrd.stock == oldPrd.stock &&
//                newPrd.productName == oldPrd.productName &&
//                newPrd.categoryId == oldPrd.categoryId &&
//                newPrd.productPrice == oldPrd.productPrice &&
//                newPrd.capacity == oldPrd.capacity &&
//                newPrd.goodsWay == oldPrd.goodsWay
//    }
}