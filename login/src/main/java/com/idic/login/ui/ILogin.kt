package com.idic.login.ui

import com.idic.login.model.DeviceInfo

/**
 * 文 件 名: ILogin
 * 创 建 人: sineom
 * 创建日期: 2019/2/1 09:46
 * 修改时间：
 * 修改备注：
 */

internal interface ILogin {

    interface Login {

        fun loginSuccess(deviceInfo: DeviceInfo)

        fun loginFail(throwable: Throwable)
    }

    fun login(
        userName: String,
        password: String,
        key: String,
        login: Login
    )
}