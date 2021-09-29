package com.idic.httpmoudle.service

import android.content.Context
import android.util.Log
import com.idic.basecommon.DeviceInfo

/**
 * 文 件 名: HttpConfigServiceImpl
 * 创 建 人: sineom
 * 创建日期: 2019-09-02 16:50
 * 修改时间：
 * 修改备注：
 */

class HttpConfigServiceImpl : HttpConfigService {
    override fun updateToken(token: String) {
        DeviceInfo.getInstance().token = token
    }

    override fun init(context: Context?) {
    }
}