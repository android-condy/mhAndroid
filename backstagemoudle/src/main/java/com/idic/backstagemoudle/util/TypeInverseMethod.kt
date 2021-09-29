package com.idic.backstagemoudle.util

import android.databinding.InverseMethod
import com.idic.backstagemoudle.R
import com.idic.basecommon.utils.MotorType
import com.idic.basecommon.utils.ScreenType
import com.idic.utilmoudle.ResourcesUtil

/**
 * 文 件 名: MotorTypeInverseMethod
 * 创 建 人: sineom
 * 创建日期: 2019-08-22 15:17
 * 修改时间：
 * 修改备注：
 */

object TypeInverseMethod {

    @JvmStatic
    @InverseMethod("motorTypeToString")
    fun stringToMotorType(value: String): MotorType? {
        if (value.isEmpty()) return null

        return when (value) {
            ResourcesUtil.getString(R.string.unFeedbackLock) -> {
                MotorType.NO_FEEDBACK_LOCK
            }
            ResourcesUtil.getString(R.string.feedbackLock) -> {
                MotorType.FEEDBACK_LOCK
            }
            ResourcesUtil.getString(R.string.twoWireMotor) -> {
                MotorType.TWO_WIRE_MOTOR
            }
            ResourcesUtil.getString(R.string.threeWireMotor) -> {
                MotorType.THREE_WIRE_MOTOR
            }
            else -> {
                return null
            }
        }
    }

    @JvmStatic
    fun motorTypeToString(motorType: MotorType): String {
        return when (motorType) {
            MotorType.NO_FEEDBACK_LOCK -> {
                ResourcesUtil.getString(R.string.unFeedbackLock)
            }
            MotorType.FEEDBACK_LOCK -> {
                ResourcesUtil.getString(R.string.feedbackLock)
            }
            MotorType.TWO_WIRE_MOTOR -> {
                ResourcesUtil.getString(R.string.twoWireMotor)
            }
            MotorType.THREE_WIRE_MOTOR -> {
                ResourcesUtil.getString(R.string.threeWireMotor)
            }
        }
    }

    @InverseMethod("screenTypeToString")
    @JvmStatic
    fun stringToScreenType(value: String): ScreenType? {
        if (value.isEmpty()) return null
        return when (value) {
            ResourcesUtil.getString(R.string.close) -> {
                ScreenType.Normal
            }
            ResourcesUtil.getString(R.string.screenStop) -> {
                ScreenType.Stop_off
            }

            ResourcesUtil.getString(R.string.ScreenRunning) -> {
                ScreenType.Running
            }
            else -> {
                null
            }
        }
    }

    @JvmStatic
    fun screenTypeToString(screenType: ScreenType): String {
        return when (screenType) {
            ScreenType.Normal -> {
                ResourcesUtil.getString(R.string.close)
            }
            ScreenType.Stop_off -> {
                ResourcesUtil.getString(R.string.screenStop)
            }
            ScreenType.Running -> {
                ResourcesUtil.getString(R.string.ScreenRunning)
            }

        }
    }
}


