package com.idic.blindbox.common

/**
 * 文 件 名: SystemStatus
 * 创 建 人: sineom
 * 创建日期: 2020/6/19 14:19
 * 修改时间：
 * 修改备注：
 */

enum class SystemStatus(val byte: Byte) {

    //启动中
    INITCOMPLETE(0x01.toByte()),

    //    "待机中"
    STANDBY(0x02.toByte()),

    //"售卖中"
    SELLING(0x03.toByte()),

    //"维护中"
    MAINTENANCE(0x04.toByte()),

    //"升级中"
    UPGRADE(0x05.toByte()),

    //停止中
    STOP_IN(0x06.toByte())
}