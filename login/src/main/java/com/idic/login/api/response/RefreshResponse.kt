package com.idic.login.api.response

import com.idic.httpmoudle.response.DKResponse


/**
 * 文 件 名: AccessToken
 * 创 建 人: sineom
 * 创建日期: 2018/11/17 10:23
 * 修改时间：
 * 修改备注：
 */

internal class RefreshResponse : DKResponse() {

    var accesstoken: String? = null

    override fun toString(): String {
        super.toString()
        return "AccessToken(accessToken=$accesstoken)"
    }

}