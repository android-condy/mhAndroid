package com.idic.login.broadCast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cn.jpush.android.api.JPushInterface
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.StringUtils
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.events.JpushMessage
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.events.UploadLogEvent
import com.idic.login.model.JpushModel

/**
 * 文 件 名: JpushCast
 * 创 建 人: sineom
 * 创建日期: 2018/11/21 18:03
 * 修改时间：
 * 修改备注：
 */

class JpushCast : BroadcastReceiver() {


    private val UPDATE = 2

    private val BANNER = 1


    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (intent.action == JPushInterface.ACTION_MESSAGE_RECEIVED) {
                val bundle = intent.extras
                val title = bundle?.getString(JPushInterface.EXTRA_TITLE)
                val extras = bundle?.getString(JPushInterface.EXTRA_EXTRA)
                if (StringUtils.isEmpty(extras)) {
                    XLog.d("推送内容为空")
                    return
                }
                val gson = Gson()
                val json = gson.fromJson<JpushModel>(extras, JpushModel::class.java)
                if (DeviceInfo.getInstance().deviceId == json.deviceId) {
                    when (title) {
                        "common" -> { //公用通知
                        }
                        "manage" -> {////前往后台
                        }
                        "pick" -> { ////取餐
                        }
                        "buy" -> {
                        }
                        "update" -> {
                            when (json.data) {
                                "banner" -> {// 推送轮播
                                    RxRelay.instance.post(JpushMessage(JpushMessage.JPushType.UPDATEBANNER))
                                }
                                else -> {// 推送商品信息

                                    RxRelay.instance.post(JpushMessage(JpushMessage.JPushType.UPDATEALLPRODUCT))
                                }
                            }
                        }
                        "uploadLog" -> {
                            RxRelay.instance.post(UploadLogEvent())
                        }
                        "remoteRoot" -> {
                            val jpushMessage = JpushMessage(JpushMessage.JPushType.OPENLOCK)
                            val replace = json.data.replace("\\", "")
                            val parseObject =
                                JSONObject.parseObject(replace)
                            jpushMessage.container = parseObject.getString("container")
                            jpushMessage.column = parseObject.getString("column")
                            jpushMessage.row = parseObject.getString("row")
                            RxRelay.instance.post(jpushMessage)
                        }
                    }
                }
                XLog.d("title-----$title")
                XLog.d("extras-----$extras")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
