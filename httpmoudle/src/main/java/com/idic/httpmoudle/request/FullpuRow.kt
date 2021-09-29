package com.idic.httpmoudle.request

import com.idic.basecommon.DeviceInfo

/**
 * 文 件 名: FullpuRow
 * 创 建 人: sineom
 * 创建日期: 2019/3/12 14:19
 * 修改时间：
 * 修改备注：
 */
//后台按行补满货道库存
data class FullpuRow(
    val containId: String,
    val rowNum: String
) : DKRequest() {
    init {
        deviceId = DeviceInfo.getInstance().deviceId
    }
}