package com.idic.login.api.request

/**
 * 文 件 名: LoginRequest
 * 创 建 人: sineom
 * 创建日期: 2019/2/1 10:32
 * 修改时间：
 * 修改备注：
 */

internal data class LoginRequest(

    //登录名
    val loginName: String,
    //密码
    val pwd: String,
    //激活码
    val key: String? = null,
    var deviceSerial: String = android.os.Build.SERIAL
)