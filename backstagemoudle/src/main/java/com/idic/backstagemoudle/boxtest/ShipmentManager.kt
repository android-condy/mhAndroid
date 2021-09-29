package com.idic.backstagemoudle.boxtest

import android.os.Handler
import android.support.annotation.Size
import android.util.Log
import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.boxtest.instruction.FunctionCode
import com.idic.backstagemoudle.boxtest.instruction.InstrucBuild
import com.idic.blind.Shipment
import java.math.BigInteger
import kotlin.random.Random

/**
 * 文 件 名: ShipManagement
 * 创 建 人: sineom
 * 创建日期: 2020/6/18 15:31
 * 修改时间：
 * 修改备注：
 */

object ShipmentManager {

    //查询出货结果失败
    val inShipmentLength = 9

    //出货完成的长度  0x004C~0x0053
    val shippedCompleteMinLength = BigInteger("004C", 16).toInt()
    val shippedCompleteMaxLength = BigInteger("0053", 16).toInt()

    private val shipment = Shipment()


    //打开串口
    fun open(sPort: String, sBaudRate: String = "9600") {
        XLog.d("打开串口:${sPort} 波特率--->$sBaudRate")
        shipment.openCom(sPort, sBaudRate)
    }

    //关闭串口
    fun close() {
        shipment.closeCom()
    }

    /**
     * 发送心跳
     * 如果8分钟没有发送,机器会重启
     */
    fun sendHeartbeat() {
        shipment.writeData(
            FunctionCode.heartbeat
        )
    }

    /**
     * 开启主动的回复  握手操作
     */
    fun openListener() {
        shipment.writeData(
            FunctionCode.license
        )
    }

    /**
     * 接收反馈消息的handler
     */
    fun setReceiverHandler(handler: Handler?) {
        shipment.receiverHandler = handler
    }

    /**
     * 通讯失败的handler 此handler收到消息表示和主控通讯失败
     */
    fun setErrorHandler(handler: Handler?) {
        shipment.errorHandler = handler
    }

    /**
     * 打开维护模式
     */
    fun openMaintenanceMode() {
        Log.i("http", "打开维护模式")
        shipment.writeData(
            InstrucBuild.maintenanceModeOpen(
                true
            )
        )
    }

    /**
     * 关闭维护模式
     */
    fun closeMaintenanceMode() {
        Log.i("http", "关闭维护模式")
        shipment.writeData(
            InstrucBuild.maintenanceModeOpen(
                false
            )
        )
    }

    /**
     * 机器故障查询
     */
    fun machineFaultQuery() {
        shipment.writeData(
            FunctionCode.faultQuery
        )
    }

    /**
     * 机器重启
     */
    fun machineResset() {
        shipment.writeData(
            FunctionCode.systemReset
        )
    }

    /**
     * 开始出货
     * [aisleNumber] 货道编号
     * [ByteArray] 流水号,供查询结果使用
     */
    fun startShipment(aisleNumber: Int): ByteArray {
        val SERIAL_NUMBER = Random.nextBytes(32)
        val shipment1 =
            InstrucBuild.shipment(
                aisleNumber,
                SERIAL_NUMBER
            )
        shipment.writeData(shipment1)
        return SERIAL_NUMBER
    }

    /**
     * 根据流水号查询出货结果
     */
    fun queryShipmentResult(@Size(32) byteArray: ByteArray) {
        val queryShipmentResult =
            InstrucBuild.queryShipmentResult(
                byteArray
            )
        shipment.writeData(queryShipmentResult)
    }

    /**
     * 更新货道
     */
    fun updateAllAisle() {
        shipment.writeData(
            FunctionCode.updateChannel
        )
    }

    /**
     * 获取货道总数量
     */
    fun getAisleCount() {
        shipment.writeData(
            FunctionCode.getChannelCount
        )
    }

    /**
     * 商品类型设定
     */
    fun productTypeSet() {
        XLog.d("商品类型设定")
        shipment.writeData(
            FunctionCode.ProductSpecifications
        )
    }

    /**
     * 盘点操作
     */
    fun inventory() {
        XLog.d("盘点操作")
        shipment.writeData(
            FunctionCode.FullFill
        )
    }

    /**
     * 获取所有的货道
     */
    fun getAllAisle() {
        XLog.d("获取所有的货道")
        shipment.writeData(
            FunctionCode.getAllAisle
        )
    }

    /**
     * 解禁所有的货道
     */
    fun AllAisleUnblocked() {
        XLog.d("解禁所有的货道")
        shipment.writeData(
            FunctionCode.allAisleUse
        )
    }

    //传感器
    fun getSensor() {
        XLog.d("传感器")
        shipment.writeData(
            FunctionCode.sensor
        )
    }

    /**
     * 启用回收
     */
    fun recycling() {
        XLog.d("启用回收")
        shipment.writeData(
            FunctionCode.recycling
        )
    }

    /**
     * 查询启用回收
     */
    fun queryRecycling() {
        XLog.d("查询启用回收")
        shipment.writeData(
            FunctionCode.query_recycling
        )
    }

    //查询是否取货
    fun queryGetGood() {
        XLog.d("查询是否取货")
        shipment.writeData(
            FunctionCode.query_get_good
        )
    }

    /**
     * 再次开门
     */
    fun openAgain() {
        XLog.d("再次开门")
        shipment.writeData(
            FunctionCode.openDoorAgain
        )
    }

    /**
     * 查询系统的状态
     */
    fun querySystemStatus() {
        XLog.d("查询系统的状态")
        shipment.writeData(
            FunctionCode.systemStatus
        )
    }
}