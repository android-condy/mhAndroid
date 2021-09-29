package com.idic.backstagemoudle.boxtest.instruction

import android.support.annotation.Size

/**
 * 文 件 名: InstrucBuild
 * 创 建 人: sineom
 * 创建日期: 2020/6/18 17:27
 * 修改时间：
 * 修改备注：
 */

object InstrucBuild {

    //开始出货,返回流水号,供查询使用
    fun shipment(aisleNumber: Int, @Size(32) byteArray: ByteArray): ByteArray {
        System.arraycopy(byteArray, 0, FunctionCode.start, 15, byteArray.size)
        FunctionCode.start[11] = (aisleNumber and 0xFF).toByte()
        return FunctionCode.start
    }

    /**
     * 根据流水号号查询出货结果
     */
    fun queryShipmentResult(@Size(32) byteArray: ByteArray): ByteArray {
        System.arraycopy(
            byteArray,
            0,
            FunctionCode.queryShipmentResult,
            11,
            byteArray.size
        )
        return FunctionCode.queryShipmentResult
    }

    //维护模式
    fun maintenanceModeOpen(isOpen: Boolean): ByteArray {
        return if (isOpen) {
            FunctionCode.toMaintenanceMode
        } else {
            FunctionCode.quitMaintenanceMode
        }
    }




}