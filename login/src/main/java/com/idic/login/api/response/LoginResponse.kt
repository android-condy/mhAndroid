package com.idic.login.api.response

import com.idic.httpmoudle.response.DKResponse

/**
 * 文 件 名: LoginResponse
 * 创 建 人: sineom
 * 创建日期: 2019/2/1 09:44
 * 修改时间：
 * 修改备注：
 */

internal class LoginResponse : DKResponse() {

    //设备ID
    var deviceId: String? = null

    //商户ID
    var merchantId: String? = null

    //设备编号
    var number: String? = null

    var accesstoken: String? = null

    override fun toString(): String {
        super.toString()
        return "LoginResponse(deviceId=$deviceId, merchantId=$merchantId, number=$number)"
    }

}