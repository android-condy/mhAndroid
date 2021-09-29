package com.idic.datamoudle.source.remote.request

import com.idic.basecommon.DeviceInfo
import com.idic.httpmoudle.request.DKRequest

/**
 * 文 件 名: GetProductsFormCategory
 * 创 建 人: sineom
 * 创建日期: 2019-04-29 10:43
 * 修改时间：
 * 修改备注：
 */

data class GetProductsFormCategory(
    val productCategoryId: String,
    val currentPage: String = "1",
    val pageRows: String = "100"
) : DKRequest() {
    init {
        deviceId = DeviceInfo.getInstance().deviceId
    }
}