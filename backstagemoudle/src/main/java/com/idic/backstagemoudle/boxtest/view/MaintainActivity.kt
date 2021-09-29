package com.idic.backstagemoudle.boxtest.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.R
import com.idic.blind.Shipment
import com.idic.backstagemoudle.boxtest.ShipmentManager
import com.idic.backstagemoudle.boxtest.instruction.FunctionCode
import com.idic.utilmoudle.DKToastUtils
import kotlinx.android.synthetic.main.activity_maintain.*
import serialport_idic.MyFunc
import java.text.SimpleDateFormat
import java.util.*

/**
 * 维护activity
 */
class MaintainActivity : AppCompatActivity() {


    private val buffer = StringBuffer()

    private var sDateFormat = SimpleDateFormat("HH:mm:ss:SS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintain)
        initView()
    }

    private fun initView() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    fun click(view: View) {
        when (view.id) {
            R.id.updateChannel -> {
                //更新货道
                sendMsg(FunctionCode.updateChannel)
                ShipmentManager.updateAllAisle()
                showInfo("更新货道", "更新完成后点击确定按钮")
            }
            R.id.FullFill -> {
                //全部补满
                sendMsg(FunctionCode.FullFill)
                ShipmentManager.inventory()
                showInfo("盘点中", "盘点完成后点击确定按钮")
            }
        }
    }

    private fun showInfo(title: String, content: String) {
//        val rxDialogSure = RxDialogSure(this).apply {
//            setTitle(title)
//            setContent(content)
//            setCanceledOnTouchOutside(false)
//            setSure("$title 完成")
//            setSureListener(View.OnClickListener { dismiss() })
//        }
//        rxDialogSure.show()
    }


    private fun sendMsg(bytes: ByteArray) {
        runOnUiThread {
            val bysStr = MyFunc.toHexString(bytes).split(" ").toString()
            buffer.append("${sDateFormat.format(Date())}发送消息${bysStr}\n")
            tvTestMsg.text = buffer.toString()
        }
    }

    override fun onResume() {
        super.onResume()
        ShipmentManager.setReceiverHandler(receiveHandler)
        ShipmentManager.openMaintenanceMode()
    }

    override fun onDestroy() {
        super.onDestroy()
        ShipmentManager.closeMaintenanceMode()
    }


    private val receiveHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg == null) {
                return
            }
            msg.data.getByteArray(Shipment.RECEIVER_DATA)?.let { bytes ->
                val bysStr = MyFunc.toHexString(bytes).split(" ").toString()
                buffer.append("${sDateFormat.format(Date())} 接收到消息${bysStr}\n")
                tvTestMsg.text = buffer.toString()
                when (bytes[4] * bytes[5]) {
                    0x01.toByte() + 0x24.toByte() -> {
                        //货道数量获取
                        val result = bytes[bytes.lastIndex - 3]
                        DKToastUtils.showCustomShort("获取到的货道总数量为${MyFunc.byteToInt(result)}")
                    }
                    0x03.toByte() * 0x01.toByte() -> {
                        //商品类型设定
                        val result = bytes[bytes.lastIndex - 3]
                        if (result == 0x01.toByte()) {
                            DKToastUtils.showCustomShort("商品类型设定成功")
                        } else {
                            DKToastUtils.showCustomShort("商品类型设定失败")
                        }
                    }
                    0x01.toByte() * 0x2a.toByte() -> {
                        //系统故障查询

                    }
                    else->{}
                }
            }
        }
    }
}