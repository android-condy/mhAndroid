package com.idic.ordermoudle

import android.annotation.SuppressLint
import com.idic.ordermoudle.request.CreateOrder
import com.idic.ordermoudle.response.SQBDevList
import com.idic.ordermoudle.response.SQBOrderInfo
import com.idic.ordermoudle.response.SQBPayCode

/**
 * 文 件 名: IOrder
 * 创 建 人: sineom
 * 创建日期: 2019/3/6 14:31
 * 修改时间：
 * 修改备注：
 */

interface IOrder {

    interface GetOrderInfo {
        //创建 订单成功
        fun createSuccess(createOrderResponse: SQBOrderInfo)

        fun createFail()

    }

    interface GetPayCode {
        //支付二维码获取成功
        fun getPayCodeSuccess(sqbData: SQBPayCode)

        fun getPayCodeFail()
    }

    interface GetOrderStatus {
        //收钱吧支付成功
        fun SQBPaySuccess()

        //未支付
        fun waitPay()

        //支付失败 例 网络中断而无法查询到此次结果
        fun payFail()

    }

    //创建订单
    fun createOrder(createOrder: CreateOrder, callBack: GetOrderInfo)

    //取消创建订单
    fun cancelCreateOrder()

    //取消订单
    fun cancelOrder(orderId: String)

    //收钱吧退款
    fun refundSQBOrder(refundData: List<SQBDevList>)

    //获取支付嘛
    fun getPayQrCode(orderId: String, callBack: GetPayCode)


    //取消获取支付码
    fun cancelGetPayCode()

    //查询订单是否支付
    fun querySQBPayStatus(orderID: String, callBack: GetOrderStatus)

    //取消收钱吧支付情况的查询
    fun cancelQuerySQBPayStatus()

    //获取蚂蚁付款码
    fun getAntPayCode(
        params: HashMap<String, Any>,
        callBack: GetPayCode
    )
}