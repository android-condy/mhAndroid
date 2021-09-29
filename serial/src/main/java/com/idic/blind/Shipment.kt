package com.idic.blind

import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.elvishew.xlog.XLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import serialport_idic.ComBean
import serialport_idic.MyFunc
import serialport_idic.SerialHelper
import java.text.SimpleDateFormat

/**
 * 文 件 名: ElevatorHolder
 * 创 建 人: sineom
 * 创建日期: 2018/12/17 16:07
 * 修改时间：
 * 修改备注：//
 */

class Shipment {

    companion object {
        const val DATA_CODE = 0x01
        const val ERROR_CODE = 0x02
        const val RECEIVER_DATA = "bytes"
    }

    private var sDateFormat = SimpleDateFormat("HH:mm:ss:SS")

    private var lastTime = 0L

    private val DEFAULT_TIME = 30000L

    private val buffer = ByteArray(100)

    private var readLength = 0

//    private var dispQueueThread: DispQueueThread? = null

    private var serialHelper: SerialHelper? = null

    //串口是否打开
    var isOpen = false

    //接受数据的handler
    var receiverHandler: Handler? = null

    //发生错误的handler
    var errorHandler: Handler? = null

    //打开串口
    fun openCom(sPort: String, sBaudRate: String = "9600"): Boolean {
        if (serialHelper != null && serialHelper!!.isOpen) {
            return true
        }
        serialHelper = SerialControl(sPort, sBaudRate)
        try {
            serialHelper?.open()
            isOpen = true
            Log.e("blindBox", "串口$sPort baudrate: $sBaudRate 打开-> $isOpen")
        } catch (e: Exception) {
            Log.e("blindBox", "打开端口失败：prot:$sPort baudrate: $sBaudRate")

        }
        return isOpen
    }

    //关闭串口
    fun closeCom() {
        serialHelper?.close()
        serialHelper = null
        receiverHandler?.removeCallbacksAndMessages(null)
        errorHandler?.removeCallbacksAndMessages(null)
    }

    fun writeData(byteArray: ByteArray) {
        GlobalScope.launch {
            val bysStr = MyFunc.toHexString(byteArray).split(" ").toString()
            errorHandler?.removeCallbacks(error)
            delay(1200)
            serialHelper?.send(byteArray)
            errorHandler?.postDelayed(error, DEFAULT_TIME)
        }
    }


    private val error =
        Runnable { errorHandler?.sendEmptyMessageDelayed(ERROR_CODE, DEFAULT_TIME) }

    private inner class SerialControl(sPort: String, sBaudRate: String) :
        SerialHelper(sPort, sBaudRate) {
        //        override fun onDataReceived(ComRecData: ComBean) {
//            if (ComRecData == null) {
//                return
//            }
//            var bysStr = ""
//            try {
//                bysStr = MyFunc.toHexString(ComRecData.bRec).split(" ").toString()
//                Log.d("blindBox", "读取信息：$bysStr")
//                /**
//                 * [DEFAULT_TIME]时间内的数据视为同一帧数据,只负责拼装
//                 */
//                errorHandler?.removeCallbacksAndMessages(null)
//                val currentTimeMillis = System.currentTimeMillis()
//                val delayTime = currentTimeMillis - lastTime
//                if (delayTime > DEFAULT_TIME && receiverHandler?.hasMessages(DATA_CODE) != true
//                ) {
//                    readLength = 0
//                }
//                lastTime = currentTimeMillis
//                receiverHandler?.removeCallbacksAndMessages(null)
//                System.arraycopy(ComRecData.bRec, 0, buffer, readLength, ComRecData.bRec.size)
//                readLength += ComRecData.bRec.size
//                val obtainMessage = receiverHandler?.obtainMessage(DATA_CODE)
//
//                //实际数量数据
//                val data = ByteArray(readLength)
//                System.arraycopy(buffer, 0, data, 0, readLength)
//                val bundle = Bundle()
//                bundle.putByteArray(RECEIVER_DATA, data)
//                obtainMessage?.data = bundle
////                val delayedTime = if (data.last() == 0x0d.toByte()) {
////                    readLength = 0
////                    300L
////                } else {
////                    2000L
////                }
//                obtainMessage?.let {
//                    receiverHandler?.sendMessageDelayed(it, 1000L)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Log.d("blindBox", " 指令 $bysStr 解析失败 ----->${e.message}")
//                errorHandler?.removeCallbacksAndMessages(null)
////                val obtainMessage = errorHandler?.obtainMessage()
////                val bundle = Bundle()
////                bundle.putString("error", "解析指定发生错误")
////                obtainMessage?.let {
////                    it.data = bundle
////                    errorHandler?.sendMessage(it)
////                }
//            }
//        }
        override fun onDataReceived(ComRecData: ComBean) {
            if (ComRecData == null) {
                return
            }
            var bysStr = ""
            val obtainMessage = receiverHandler?.obtainMessage(DATA_CODE)
            val bundle = Bundle()
            try {
                errorHandler?.removeCallbacksAndMessages(null)
                bysStr = MyFunc.toHexString(ComRecData.bRec).split(" ").toString()

                receiverHandler?.removeCallbacksAndMessages(null)
                bundle.putByteArray(RECEIVER_DATA, ComRecData.bRec)
                obtainMessage?.data = bundle
                obtainMessage?.let {
                    receiverHandler?.sendMessage(it)
                }
            } catch (e: Exception) {
                XLog.d( " 指令 $bysStr 解析失败 ----->${e.message}")
//                errorHandler?.removeCallbacksAndMessages(null)
//                bundle.putByteArray(RECEIVER_DATA, ComRecData.bRec)
//                obtainMessage?.let {
//                    it.data = bundle
//                    receiverHandler?.sendMessage(it)
//                }
            }
        }
    }
}