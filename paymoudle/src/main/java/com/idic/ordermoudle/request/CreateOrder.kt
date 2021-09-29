package com.idic.ordermoudle.request

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.SPKeys
import com.idic.httpmoudle.request.DKRequest

/**
 * 文 件 名: CreateOrder
 * 创 建 人: sineom
 * 创建日期: 2019/3/6 14:45
 * 修改时间：
 * 修改备注： 创建订单所需要的参数
 */

class CreateOrder(
    productsInfo: List<OrderProduct>,
    totalAmount: String//订单总金额
) : DKRequest() {

    //订单总金额
    var orderCost = ""

    //订单中所有产品的信息
    var orderProduct: Any? = null

    //商户id
    val merchantId = SPUtils.getInstance().getString(SPKeys.MERCHANT_ID)

    //未说明有什么用处
    val orderResource = "0"

    init {
        orderCost = totalAmount
        ininKey_token_time()
        deviceId = DeviceInfo.getInstance().deviceId
        val gson = Gson()
        orderProduct = gson.toJson(productsInfo)
    }

}

data class OrderProduct(
    val productId: String, //产品id
    val productCost: String, //产品价格
    val deviceWayId: String, // 货道id
    val buyCount: String = "1"//购买数量
)