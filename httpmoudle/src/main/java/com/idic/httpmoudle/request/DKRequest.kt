package com.idic.httpmoudle.request

import android.util.Log
import com.blankj.utilcode.util.SPUtils
import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.SPKeys
import com.idic.utilmoudle.MD5Utils
import java.util.*

/**
 * 文 件 名: DKRequest
 * 创 建 人: sineom
 * 创建日期: 2019/1/31 14:24
 * 修改时间：
 * 修改备注： 公共的请求参数,当某个属性为null时将不会传递
 */

open class DKRequest {

    //激活码
    var key: String? = null
        get() = SPUtils.getInstance().getString(SPKeys.DEVICES_KEY)


    //请求token
    var token: String? = null
        get() = "token"

    //登录后返回的设备id
    var deviceId: String? = null
        get() = SPUtils.getInstance().getString(SPKeys.DEVICES_ID)

    //时间戳, 服务端允许客户端请求最大时间误差为5分钟
    var timestamp: String? = null

    override fun toString(): String {
        return "DKRequest(key=$key, token=$token, deviceId=$deviceId, timestamp=$timestamp)"
    }

    fun ininKey_token_time() {
        key = SPUtils.getInstance().getString(SPKeys.DEVICES_KEY)
        timestamp = Date().time.toString()
        token = MD5Utils.encodeByMD5("$key${DeviceInfo.getInstance().token}$timestamp")
        Log.i("condy","DeviceInfo.token="+DeviceInfo.getInstance().token)
    }

}