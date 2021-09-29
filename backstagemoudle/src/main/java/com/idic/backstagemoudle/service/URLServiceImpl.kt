package com.idic.backstagemoudle.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.idic.backstagemoudle.BackstageApp
import com.idic.basecommon.service.GetURlService

/**
 * 文 件 名: URLServiceImpl
 * 创 建 人: sineom
 * 创建日期: 2019-09-05 11:42
 * 修改时间：
 * 修改备注：
 */

@Route(path = "/backstage/url")
class URLServiceImpl : GetURlService {

    override fun getApiUrl(): String {
        return BackstageApp.getDeviceConfig()?.domain ?: "http://api.dwyzn.com"
    }

    override fun getApiPort(): String {
        return BackstageApp.getDeviceConfig()?.port ?: "80"
    }

    override fun getImgUrl(): String {
        return BackstageApp.getDeviceConfig()?.imgBaseURL ?: "http://console.dwyzn.com"
    }

    override fun getImgPort(): String {
        return BackstageApp.getDeviceConfig()?.imgPort ?: "80"
    }

    override fun init(context: Context?) {
    }
}