package com.idic.utilmoudle

/**
 * 文 件 名: ShipmentInfo
 * 创 建 人: sineom
 * 创建日期: 2019-05-15 09:33
 * 修改时间：
 * 修改备注：
 */

data class ShipmentInfo(
    val deviceId: String, //设备编号
    val merchantId: String,//商户编号
    val orderId: String,//订单编号
    val deviceWayId: String,//货道编号
    val shipmentType: String,//出货状态 出货状态（0：正常出货；1：异常出货）
    val remark: String//出货信息
)