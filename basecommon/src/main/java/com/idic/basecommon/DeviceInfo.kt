package com.idic.basecommon

/**
 * 文 件 名: DeviceInfo
 * 创 建 人: sineom
 * 创建日期: 2019-08-15 13:50
 * 修改时间：
 * 修改备注：
 */

class DeviceInfo {
    //激活码
    var device_key = ""

    //设备ID
    var deviceId = ""

    //token
    var token = ""

    //设备编号
    var mDeviceNumber = ""
        get() {
            return if (field.isNullOrEmpty()) {
                "设备未激活"
            } else {
                field
            }
        }

    //商户ID
    var merchantId = ""

    //用户名
    @Volatile
    var userName = ""

    //密码
    @Volatile
    var password = ""

    companion object {
        private object DeviceHelper {
            val mInner = DeviceInfo()
        }

        fun getInstance() = DeviceHelper.mInner

        fun getConfig(): DeviceConfig {
            return getInstance().deviceConfig
        }
    }

    var deviceConfig: DeviceConfig = DeviceConfig()
}