package com.idic.basecommon.utils

/**
 * 文 件 名: ScreenType
 * 创 建 人: sineom
 * 创建日期: 2019-06-05 14:36
 * 修改时间：
 * 修改备注：
 */

enum class COMStatus {
    SUCCESS, //成功
    FAIL,//失败
    RUNNING,//运行中
    RECEIVER,//接收到反馈
    OTHERERROR//未知异常
}

enum class LayerErrorType {
    NO_ERROR,
    //升降机版本 升降机不可用
    HAD_PRODUCT,
    ROOM_OPENED,
    OTHER_ERROR
}

enum class ScreenType(var int: Int) {
    Normal(0),
    Stop_off(1),
    Running(2),
}

enum class MotorType(var int: Int) {
    NO_FEEDBACK_LOCK(0),//无反馈锁
    FEEDBACK_LOCK(1),//有反馈锁
    TWO_WIRE_MOTOR(2),//二线制电机
    THREE_WIRE_MOTOR(3)//三线制电机
}
