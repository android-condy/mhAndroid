package com.idic.ordermoudle.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import com.idic.ordermoudle.IOrder
import com.idic.ordermoudle.request.CreateOrder
import com.idic.ordermoudle.response.SQBDevList

/**
 * 文 件 名: BasePayModel
 * 创 建 人: sineom
 * 创建日期: 2019-09-09 15:01
 * 修改时间：
 * 修改备注：
 */

open class BasePayModel(
    application: Application,
    private val orderOperation: IOrder
) : AndroidViewModel(application), IOrder {

    val openAliPaySmilePay = ObservableBoolean()


    override fun createOrder(createOrder: CreateOrder, callBack: IOrder.GetOrderInfo) {
        orderOperation.createOrder(createOrder, callBack)
    }

    override fun cancelCreateOrder() {
        orderOperation.cancelCreateOrder()
    }

    override fun cancelOrder(orderId: String) {
        orderOperation.cancelOrder(orderId)
    }

    override fun refundSQBOrder(refundData: List<SQBDevList>) {
        orderOperation.refundSQBOrder(refundData)
    }

    override fun getPayQrCode(orderId: String, callBack: IOrder.GetPayCode) {
        orderOperation.getPayQrCode(orderId, callBack)
    }

    override fun cancelGetPayCode() {
        orderOperation.cancelGetPayCode()
    }

    override fun querySQBPayStatus(orderID: String, callBack: IOrder.GetOrderStatus) {
        orderOperation.querySQBPayStatus(orderID, callBack)
    }

    override fun cancelQuerySQBPayStatus() {
        orderOperation.cancelQuerySQBPayStatus()
    }

    override fun getAntPayCode(params: HashMap<String, Any>, callBack: IOrder.GetPayCode) {
        orderOperation.getAntPayCode(params, callBack)
    }

}