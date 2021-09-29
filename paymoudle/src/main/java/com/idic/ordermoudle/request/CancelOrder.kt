package com.idic.ordermoudle.request

import com.idic.httpmoudle.request.DKRequest

/**
 * 文 件 名: CancelOrder
 * 创 建 人: sineom
 * 创建日期: 2019/3/6 15:32
 * 修改时间：
 * 修改备注：
 */

data class CancelOrder(val orderId: String) : DKRequest() {
    init {
      ininKey_token_time()
    }
}