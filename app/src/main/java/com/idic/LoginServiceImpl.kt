package com.idic

import android.content.Context
import android.content.Intent
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.idic.basecommon.service.DeviceConfigServer
import com.idic.basecommon.utils.ARouterConfig
import com.idic.basecommon.utils.ARouterConfig.LOGIN_SUCCESS
import com.idic.login.model.DeviceInfo
import com.idic.login.service.LoginSuccessService

/**
 * 文 件 名: AccountServiceImpl
 * 创 建 人: sineom
 * 创建日期: 2019-08-08 14:59
 * 修改时间：
 * 修改备注：
 */
@Route(path = LOGIN_SUCCESS)
class LoginServiceImpl : LoginSuccessService {

    private var context: Context? = null

    override fun loginSuccess(deviceInfo: DeviceInfo?) {
        deviceInfo?.let {


            val instance = com.idic.basecommon.DeviceInfo.getInstance()
            with(instance) {
                //激活码
                device_key = deviceInfo.device_key

                //设备ID
                deviceId = deviceInfo.deviceId

                //token
                token = deviceInfo.accesstoken

                //设备编号
                mDeviceNumber = deviceInfo.number

                //商户ID
                merchantId = deviceInfo.merchantId

                //用户名
                userName = deviceInfo.device_name

                //密码
                password = deviceInfo.password
            }
        }

        ARouter.getInstance().navigation(DeviceConfigServer::class.java).initDeviceConfig()
        val mainPath =
            com.idic.basecommon.DeviceInfo.getInstance().deviceConfig.homeRoutePath
        ARouter.getInstance().build(ARouterConfig.BLIND_BOX).navigation()
        Log.i("condy","LoginServiceImpl")

    }

    override fun init(context: Context?) {
        this.context = context

    }
}