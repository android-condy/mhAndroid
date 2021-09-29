package com.idic.basecommon.events

/**
 * 文 件 名: JpushMessage
 * 创 建 人: sineom
 * 创建日期: 2018/11/26 11:01
 * 修改时间：
 * 修改备注：
 */

class JpushMessage(var jPushType: JPushType) {

    enum class JPushType {
        //banner更新
        UPDATEBANNER,
        //产品更新
        UPDATEALLPRODUCT,
        //开锁
        OPENLOCK
    }

    var container: String = "0"
    var column: String = "0"
    var row: String = "0"
}