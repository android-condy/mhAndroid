package com.idic.httpmoudle.request

import android.util.Log
import com.blankj.utilcode.util.SPUtils
import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.SPKeys
import com.idic.utilmoudle.MD5Utils
import java.util.*


open class BaseRequest() {

    //md5（接口名+时间戳+0411）,然后转换大写
    var token: String? = ""

    //设备id
    var device_id: String? = android.os.Build.SERIAL
        get() =
            when {
                field.equals("unknown") || field.isNullOrEmpty() ->
                    "FS1912550188"
                else -> android.os.Build.SERIAL
            }


    //时间戳, 服务端允许客户端请求最大时间误差为5分钟
    var timestamp: String? = null
    var type_id: String? = null


    fun ininKey_token_time() {
        timestamp = Date().time.toString()
//        token = MD5Utils.encodeByMD5(interfaceName + timestamp + "0411").toUpperCase()
    }
}