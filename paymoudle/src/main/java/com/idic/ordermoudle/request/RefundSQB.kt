package com.idic.ordermoudle.request

import com.google.gson.Gson
import com.idic.httpmoudle.request.DKRequest
import com.idic.ordermoudle.response.SQBDevList

/**
 * 文 件 名: RefundSQB
 * 创 建 人: sineom
 * 创建日期: 2019/3/6 16:11
 * 修改时间：
 * 修改备注：
 */

class RefundSQB(
    refundData: List<SQBDevList>
) : DKRequest() {

    var deviceWay = ""

    init {
        ininKey_token_time()
        val gson = Gson()
        deviceWay = gson.toJson(refundData)
    }
}