package com.idic.utilmoudle

import com.alibaba.fastjson.JSONObject
import com.idic.logger.ThreadPoolProxy

/**
 * 文 件 名: DKLoggerClient
 * 创 建 人: sineom
 * 创建日期: 2019-05-07 11:19
 * 修改时间：
 * 修改备注：
 */

class DKLoggerClient {

    fun execute(shipmentInfo: ShipmentInfo? = null, jsonData: String? = null, param: Map<String, String>? = null) {

        if (shipmentInfo != null) {
            doPostJsonData(JSONObject.toJSONString(shipmentInfo))
        } else if (!jsonData.isNullOrEmpty()) {
            doPostJsonData(jsonData)
        } else if (!param.isNullOrEmpty()) {
            doPostKeyValue(param)
        } else {
            throw Throwable("request param is null")
        }
    }

    private fun doPostJsonData(jsonData: String) {
        threadPoolProxy.executeTask(Runnable {
            HttpUtils.postJsonData(Setting.uploadUrl, jsonData)
        })
    }

    private fun doPostKeyValue(param: Map<String, String>) {
        threadPoolProxy.executeTask(Runnable {
            threadPoolProxy.executeTask(Runnable {
                HttpUtils.postKeyValeData(Setting.uploadUrl, param)
            })
        })
    }

    companion object {

        private var threadPoolProxy = ThreadPoolProxy(3, 5, 10)

        private var instance: DKLoggerClient? = null

        fun instance() =
            instance ?: synchronized(this) {
                DKLoggerClient().also { instance = it }
            }
    }
}
