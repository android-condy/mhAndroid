package com.idic.login.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.idic.login.model.DeviceInfo

/**
 * 文 件 名: AccountService
 * 创 建 人: sineom
 * 创建日期: 2019-08-08 14:58
 * 修改时间：
 * 修改备注：
 */

interface LoginSuccessService : IProvider {

    //登录成功后调用
    fun loginSuccess(deviceInfo: DeviceInfo?)
}