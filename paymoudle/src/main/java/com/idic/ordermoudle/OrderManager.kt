package com.idic.ordermoudle

import com.idic.ordermoudle.request.CreateOrder
import com.idic.ordermoudle.response.SQBDevList

/**
 * 文 件 名: OrderManager
 * 创 建 人: sineom
 * 创建日期: 2019-08-15 14:54
 * 修改时间：
 * 修改备注：
 */

class OrderManager private constructor(
    private val orderOperation: IOrder
) : IOrder {
    override fun createOrder(createOrder: CreateOrder, callBack: IOrder.GetOrderInfo) {
        cancelCreateOrder()
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
        cancelGetPayCode()
        orderOperation.getPayQrCode(orderId, callBack)
    }

    override fun cancelGetPayCode() {
        orderOperation.cancelCreateOrder()
    }

    override fun querySQBPayStatus(orderID: String, callBack: IOrder.GetOrderStatus) {
        cancelQuerySQBPayStatus()
        orderOperation.querySQBPayStatus(orderID, callBack)
    }

    override fun cancelQuerySQBPayStatus() {
        orderOperation.cancelQuerySQBPayStatus()
    }

    override fun getAntPayCode(params: HashMap<String, Any>, callBack: IOrder.GetPayCode) {
        orderOperation.getAntPayCode(params, callBack)
    }

    companion object {
        private object ManagerHelper {
            val mInstance = OrderManager(OrderOperation())
        }

        fun getInstance() = ManagerHelper.mInstance
    }
}