package com.idic.httpmoudle.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.elvishew.xlog.LogUtils
import com.elvishew.xlog.XLog
import com.idic.basecommon.service.SetUrlService
import com.idic.httpmoudle.URL

/**
 * 文 件 名: SetUrlServiceImpl
 * 创 建 人: sineom
 * 创建日期: 2019-09-05 17:50
 * 修改时间：
 * 修改备注：
 */

@Route(path = "/httpUrl/set")
class SetUrlServiceImpl : SetUrlService {

    override fun setApiUrl(url: String, port: String) {
        URL.API_IP = url
        URL.API_PORT = port
        com.blankj.utilcode.util.LogUtils.d("URL.API_IP----->${URL.API_IP}")
    }

    override fun setImageUrl(url: String, port: String) {
        URL.IMG_DOMIAN = url
        URL.IMG_PORT = port
    }

    override fun init(context: Context?) {
    }

}