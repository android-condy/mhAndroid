package com.idic.backstagemoudle.db.conver

import android.arch.persistence.room.TypeConverter
import com.idic.basecommon.utils.MotorType
import com.idic.basecommon.utils.ScreenType

/**
 * 文 件 名: Converters
 * 创 建 人: sineom
 * 创建日期: 2019-08-06 17:47
 * 修改时间：
 * 修改备注：类型转换器
 */

class DKConverters {

    companion object {

        @TypeConverter
        @JvmStatic
        fun motorType2Int(motorType: MotorType): Int {
            return when (motorType) {
                MotorType.NO_FEEDBACK_LOCK -> {
                    0
                }
                MotorType.FEEDBACK_LOCK -> {
                    1
                }
                MotorType.TWO_WIRE_MOTOR -> {
                    2
                }
                MotorType.THREE_WIRE_MOTOR -> {
                    3
                }
            }
        }

        @TypeConverter
        @JvmStatic
        fun int2MotoType(int: Int): MotorType? {
            return when (int) {
                0 -> {
                    MotorType.NO_FEEDBACK_LOCK
                }
                1 -> {
                    MotorType.FEEDBACK_LOCK
                }
                2 -> {
                    MotorType.TWO_WIRE_MOTOR
                }
                3 -> {
                    MotorType.THREE_WIRE_MOTOR
                }
                else -> {
                    null
                }
            }
        }

        @TypeConverter
        @JvmStatic
        fun curtain2Int(curtainType: ScreenType): Int {
            /**
             *  UNCURTAIN(0), //不参考光幕
            RUN_OF_GOODS(1),//检测到掉货电机不停止，直到电机到位
            STOP_OF_GOODS(2)//检测到掉货立即停止电机
             */
            return when (curtainType) {
                ScreenType.Normal -> {
                    0
                }
                ScreenType.Running -> {
                    1
                }
                ScreenType.Stop_off -> {
                    2
                }
            }
        }

        @TypeConverter
        @JvmStatic
        fun int2curtain(int: Int): ScreenType? {
            /**
             *  Normal(0), //不参考光幕
            Running(1),//检测到掉货电机不停止，直到电机到位
            Stop_off(2)//检测到掉货立即停止电机
             */
            return when (int) {
                0 -> {
                    ScreenType.Normal
                }
                1 -> {
                    ScreenType.Running
                }
                2 -> {
                    ScreenType.Stop_off
                }
                else -> {
                    null
                }
            }
        }
    }
}