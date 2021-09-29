package com.idic.blindbox.service

import com.blankj.utilcode.util.LogUtils
import com.elvishew.xlog.XLog
import com.idic.basecommon.events.RxRelay
import serialport_idic.ComBean
import serialport_idic.MyFunc
import serialport_idic.SerialHelper

/**
 * 文 件 名: KeyDownUtil
 * 创 建 人: sineom
 * 创建日期: 2018/12/10 17:31
 * 修改时间：
 * 修改备注：
 */

 object LightControl {


    private var serialHelper: SerialHelper? = null

    // 20210202
    private var serialHelper1: SerialHelper? = null

    //白灯
    private val whiteBytes = byteArrayOf(
        0xA5.toByte(),
        0x0A.toByte(),
        0x62.toByte(),
        0xA1.toByte(),
        0x03.toByte(),
        0xFF.toByte(),
        0xFF.toByte(),
        0xFF.toByte(),
        0x00.toByte(),
        0x05.toByte(),
        0x05.toByte(),
        0xEE.toByte(),
        0x5A.toByte()
    )

    //白灯 S1
    private val whiteBytes1 = byteArrayOf(
        0xA5.toByte(),
        0x07.toByte(),
        0xFF.toByte(),
        0xA0.toByte(),
        0xFF.toByte(),
        0xFF.toByte(),
        0xFF.toByte(),
        0xFF.toByte(),
        0xEE.toByte(),
        0x5A.toByte()
    )

    //紫灯
    private val purpleBytes = byteArrayOf(
        0xA5.toByte(),
        0x0A.toByte(),
        0x62.toByte(),
        0xA1.toByte(),
        0x03.toByte(),
        0x55.toByte(),
        0x1A.toByte(),
        0x8B.toByte(),
        0x00.toByte(),
        0x05.toByte(),
        0x05.toByte(),
        0xEE.toByte(),
        0x5A.toByte()
    )

    //紫灯 S1
    private val purpleBytes1 = byteArrayOf(
        0xA5.toByte(),
        0x07.toByte(),
        0xFF.toByte(),
        0xA0.toByte(),
        0xA0.toByte(),
        0x20.toByte(),
        0xF0.toByte(),
        0xFF.toByte(),
        0xEE.toByte(),
        0x5A.toByte()
    )
    //绿色
    private val greenBytes = byteArrayOf(
        0xA5.toByte(),
        0x07.toByte(),
        0xFF.toByte(),
        0xA0.toByte(),
        0x00.toByte(),
        0xFF.toByte(),
        0x00.toByte(),
        0xFF.toByte(),
        0xEE.toByte(),
        0x5A.toByte()
    )
    //红色
    private val redBytes = byteArrayOf(
        0xA5.toByte(),
        0x07.toByte(),
        0xFF.toByte(),
        0xA0.toByte(),
        0xFF.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0xFF.toByte(),
        0xEE.toByte(),
        0x5A.toByte()
    )

    /**
     * 白色：A5 07 FF A0 FF FF FF FF EE 5A
    紫色：A5 07 FF A0 A0 20 F0 FF EE 5A
    绿色：A5 07 FF A0 00 FF 00 FF EE 5A
    红色：A5 07 FF A0 FF 00 00 FF EE 5A
     */

    fun openCOM(devicePath: String): Boolean {
        var openSuccess = false
        try {
            serialHelper = SerialControl(devicePath)
            serialHelper?.open()
            openSuccess = serialHelper?.isOpen ?: false
            LogUtils.d("按键打开端口----->$openSuccess")
        } catch (e: Exception) {
            LogUtils.d("按键端口异常----->${e.message}")
        }
        return openSuccess
    }

    // 20210202
    fun openCOM1(devicePath: String): Boolean {
        var openSuccess = false
        try {
            serialHelper1 = SerialControl(devicePath)
            serialHelper1?.open()
            openSuccess = serialHelper1?.isOpen ?: false
            LogUtils.d("按键打开端口1----->$openSuccess")
        } catch (e: Exception) {
            LogUtils.d("按键端口1异常----->${e.message}")
        }
        return openSuccess
    }

    //紫灯
    fun purpleLight() {
        // serialHelper?.send(purpleBytes)
        serialHelper?.send(whiteBytes) //一直白灯
    }

    //白灯
    fun whiteLight() {
        serialHelper?.send(whiteBytes)
    }

    // 20210202 白灯新
    fun whiteLightn() {
        serialHelper1?.send(whiteBytes1)
    }

    //紫灯 20210202
    fun purpleLightn() {
        serialHelper1?.send(purpleBytes1);
    }

    //绿灯
    fun greenLightn() {
        serialHelper1?.send(greenBytes)
    }
    //红灯
    fun redLightn() {
        serialHelper1?.send(redBytes)
    }

    private class SerialControl(devicePath: String) : SerialHelper(devicePath, 9600) {
        override fun onDataReceived(ComRecData: ComBean) {
        }
    }
}
