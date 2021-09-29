package com.idic.ordermoudle.request

import com.idic.httpmoudle.request.DKRequest

/**
 * 文 件 名: GetPayCode
 * 创 建 人: sineom
 * 创建日期: 2019/3/6 16:16
 * 修改时间：
 * 修改备注：
 */

data class GetPayCodeRest(val orderId: String) : DKRequest() {
    init {
        ininKey_token_time()
    }
}