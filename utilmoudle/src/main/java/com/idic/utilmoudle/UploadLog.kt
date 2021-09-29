package com.idic.utilmoudle

import com.elvishew.xlog.XLog


/**
 * 文 件 名: UploadLog
 * 创 建 人: sineom
 * 创建日期: 2019-09-17 14:43
 * 修改时间：
 * 修改备注：
 */

object UploadLog {

    /**
     * @param deviceId 设备id
     * @param merchantId 商户id
     * @param orderId 订单id
     * @param deviceWayId 货道id
     * @param msg 出货信息
     * @param success 是否成功
     *
     */
    fun uploadMsg(
        deviceId: String,
        merchantId: String,
        orderId: String,
        deviceWayId: String,
        msg: String,
        success: Boolean
    ) {
        XLog.d("开始上传出货日志----> deviceId:$deviceId  merchantId:$merchantId orderId:$orderId deviceWayId:$deviceWayId")
        val shipmentInfo = ShipmentInfo(
            deviceId = deviceId,
            merchantId = merchantId,
            orderId = orderId,
            deviceWayId = deviceWayId,
            shipmentType = if (success) "0" else "1",
            remark = msg
        )
        DKLoggerClient.instance().execute(shipmentInfo)
    }
}