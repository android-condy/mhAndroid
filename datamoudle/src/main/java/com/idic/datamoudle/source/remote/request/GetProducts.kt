package com.idic.datamoudle.source.remote.request

import android.util.Log
import com.idic.basecommon.DeviceInfo
import com.idic.httpmoudle.request.DKRequest
import com.idic.utilmoudle.MD5Utils

/**
 * 文 件 名: GetProductsData
 * 创 建 人: sineom
 * 创建日期: 2019/3/1 16:55
 * 修改时间：
 * 修改备注：
 */

class GetProducts(
    val pageRows: String = "50", //当前页面的数量

    var currentPage: String? = "1" //当前页数
) :
    DKRequest() {

    init {
        ininKey_token_time()
        deviceId = DeviceInfo.getInstance().deviceId
    }


}