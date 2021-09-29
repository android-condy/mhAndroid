package com.idic.categorythome.model

import android.app.Application
import android.databinding.ObservableField
import com.idic.ordermoudle.IOrder
import com.idic.ordermoudle.model.BasePayModel

/**
 * 文 件 名: PayModel
 * 创 建 人: sineom
 * 创建日期: 2019-08-15 16:05
 * 修改时间：
 * 修改备注：
 */

class PayModel(
    application: Application,
    private val orderOperation: IOrder
) : BasePayModel(application, orderOperation) {
    //订单总金额
    val totalAmount = ObservableField<String>()

    //购买总数量
    val payCount = ObservableField<String>()
}