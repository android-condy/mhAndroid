package com.idic.ordermoudle.request

import com.idic.httpmoudle.request.DKRequest

/**
 * 文 件 名: WeChatOrAlipay
 * 创 建 人: sineom
 * 创建日期: 2019/3/1 17:38
 * 修改时间：
 * 修改备注： 收钱吧订单的查询参数
 */

data class QuerySQBPayStatus(
    val orderId: String
) : DKRequest() {
    init {
        ininKey_token_time()
    }
}