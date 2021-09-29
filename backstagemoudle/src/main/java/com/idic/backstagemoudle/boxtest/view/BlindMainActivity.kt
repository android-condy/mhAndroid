package com.idic.backstagemoudle.boxtest.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.R
import com.idic.backstagemoudle.boxtest.ShipmentManager
import com.idic.backstagemoudle.boxtest.common.outage
import com.idic.backstagemoudle.boxtest.instruction.FunctionCode
import com.idic.backstagemoudle.util.DialogUtil
import com.idic.basecommon.AppExecutors
import com.idic.basecommon.events.LightType
import com.idic.basecommon.events.RxRelay
import com.idic.blind.Shipment
import com.idic.blindbox.common.SystemStatus
import com.idic.utilmoudle.DKToastUtils
import kotlinx.android.synthetic.main.activity_blind_main.*
import serialport_idic.MyFunc
import serialport_idic.SerialPortFinder
import java.text.SimpleDateFormat
import java.util.*

class BlindMainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private var sDateFormat = SimpleDateFormat("HH:mm:ss:SS")

    private lateinit var mSerialPortFinder: SerialPortFinder


    private val appExecutors = AppExecutors()

    private val buffer = StringBuffer()

    private val heartbeatHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blind_main)
        initConfig()
        initView()
    }

    override fun onResume() {
        super.onResume()
        ShipmentManager.setReceiverHandler(receiveHandler)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        ShipmentManager.close()
//    }

    private fun initView() {
        toggleButtonCOM.setOnCheckedChangeListener(this)
        btnTestMotor.setOnClickListener {
            //            if (toggleButtonCOM.isChecked) {
//                //进入维护页面
            startActivity(Intent(this, MaintainActivity::class.java))
//            } else {
//                tvTestMsg.text = "串口未打开"
//            }
        }
        btnTest.setOnClickListener {
            //测试页面
            startActivity(Intent(this, TestActivity::class.java))
        }

        btnBack.setOnClickListener { finish() }
    }

    fun click(view: View) {
        when (view.id) {
            R.id.toMaintenanceMode -> {
                //维护模式
                sendMsg(FunctionCode.toMaintenanceMode)
                ShipmentManager.openMaintenanceMode()
            }
            R.id.quitMaintenanceMode -> {
                //退出维护模式
                sendMsg(FunctionCode.quitMaintenanceMode)
                ShipmentManager.closeMaintenanceMode()
            }
            R.id.updateChannel -> {
                //更新货道
                sendMsg(FunctionCode.updateChannel)
                ShipmentManager.updateAllAisle()
            }
            R.id.FullFill -> {
                //全部补满
                sendMsg(FunctionCode.FullFill)
                ShipmentManager.inventory()
            }
            R.id.openDoorAgain -> {
                //再次开门
                sendMsg(FunctionCode.openDoorAgain)
                ShipmentManager.openAgain()
            }

            R.id.faultQuery -> {
                //机器故障查询
                sendMsg(FunctionCode.faultQuery)
                ShipmentManager.machineFaultQuery()
            }
            R.id.getChannelCount -> {
                //获取货道数量
                sendMsg(FunctionCode.getChannelCount)
                ShipmentManager.getAisleCount()
            }

            R.id.getChannelLayer -> {
                //获取所有货道所在的层数
                sendMsg(FunctionCode.getAllAisle)
                ShipmentManager.getAllAisle()
            }
            R.id.btnTypeSet -> {
                //商品类型设定
                sendMsg(FunctionCode.ProductSpecifications)
                ShipmentManager.productTypeSet()
            }
            R.id.btnAllAisleUnblocked -> {
                //所有货道解禁
                sendMsg(FunctionCode.allAisleUse)
                ShipmentManager.AllAisleUnblocked()
            }
            R.id.btnRecycling -> {
                //启用回收
                sendMsg(FunctionCode.recycling)
                ShipmentManager.recycling()
            }
            R.id.btnSystemStatus -> {
                //系统状态获取
                sendMsg(FunctionCode.systemStatus)
                ShipmentManager.querySystemStatus()
            }

            R.id.system_serial_number -> {
//                sendMsg(FunctionCode.systemReset)
//                ShipmentManager.machineResset()
//                AppUtil.relaunch()
                //系统序列号
                tvTestMsg.text = "系统序列号:" + android.os.Build.SERIAL
            }
            R.id.white_light -> {
                DialogUtil.saveSharedPreferences(this, "1")
                RxRelay.instance.post(LightType.WHITE_LIGHT)
            }
            R.id.purple_light -> {
                DialogUtil.saveSharedPreferences(this, "2")
                RxRelay.instance.post(LightType.PURPLE_LIGHT)
            }
            R.id.greeen_light -> {
                DialogUtil.saveSharedPreferences(this, "3")
                RxRelay.instance.post(LightType.GREEN_LIGHT)
            }
            R.id.red_light -> {
                DialogUtil.saveSharedPreferences(this, "4")
                RxRelay.instance.post(LightType.RED_LIGHT)
            }
        }
    }

    private fun initConfig() {
        //加载串口列表
        mSerialPortFinder = SerialPortFinder()
        val entryValues = mSerialPortFinder.allDevicesPath
        val allDevices = ArrayList<String>()
        for (i in entryValues.indices) {
            allDevices.add(entryValues[i])
        }
        val aspnDevices = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, allDevices
        )
        aspnDevices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCOM.adapter = aspnDevices
        if (allDevices.size > 0) {
            spinnerCOM.setSelection(0)
        }
        spinnerCOM.onItemSelectedListener = ItemSelectedEvent()
        //加载波特率
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.baudrates_value, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBaudRateCOM.adapter = adapter
        spinnerBaudRateCOM.setSelection(0)
        spinnerBaudRateCOM.onItemSelectedListener = ItemSelectedEvent()
    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (!isChecked) {
//            ShipmentManager.close()
        } else {
//            ShipmentManager.open(
//                spinnerCOM.selectedItem.toString(),
//                spinnerBaudRateCOM.selectedItem.toString()
//            )
//            //发送心跳
//            appExecutors.diskIO.execute {
//                Thread.sleep(500)
//                sendMsg(FunctionCode.license)
//                ShipmentManager.openListener()
//                heartbeatHandler.postDelayed(heartbeat, 8 * 60 * 1000)
//            }
        }

    }

    private inner class ItemSelectedEvent : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            ShipmentManager.close()
            toggleButtonCOM.isChecked = false
            buffer.setLength(0)
        }

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
                XLog.d("接收到消息--->${bysStr}")
                buffer.append("${sDateFormat.format(Date())} 接收到消息${bysStr}\n")
                tvTestMsg.text = buffer.toString()
                when (bytes[4] * bytes[5]) {
                    0x01.toByte() * 0x24.toByte() -> {
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
                    0x01.toByte() * 0x27.toByte() -> {
                        //系统状态
                        val msg = when (bytes[bytes.lastIndex - 3]) {
                            SystemStatus.INITCOMPLETE.byte -> {
                                "初始化完成"
                            }
                            SystemStatus.STANDBY.byte -> {
                                "待机中"
                            }
                            SystemStatus.SELLING.byte -> {
                                "售卖中"
                            }
                            SystemStatus.MAINTENANCE.byte -> {
                                "维护中"
                            }
                            SystemStatus.UPGRADE.byte -> {
                                "升级中"
                            }
                            SystemStatus.STOP_IN.byte -> {
                                "停止中"
                            }
                            else -> {
                                "未知状态"
                            }
                        }
                        DKToastUtils.showCustomShort(msg)
                    }
                    0x01.toByte() * 0x2a.toByte() -> {
                        //系统故障查询
                        buffer.append("系统故障查询结果\n")
                        val size = bytes.size - 1
                        for (index in 13 until size step 2) {
                            val msg = outage[bytes[index] + bytes[index + 1]]
                            buffer.append("$msg \n")
                        }
                        tvTestMsg.text = buffer.toString()
                    }
                    else -> {
                    }
                }
            }
        }
    }


    private fun sendMsg(bytes: ByteArray) {
        runOnUiThread {
            val bysStr = MyFunc.toHexString(bytes).split(" ").toString()
            buffer.append("${sDateFormat.format(Date())}发送消息${bysStr}\n")
            tvTestMsg.text = buffer.toString()
        }
    }


    private val heartbeat = object : Runnable {
        override fun run() {
            ShipmentManager.sendHeartbeat()
            heartbeatHandler.postDelayed(this, 5 * 60 * 1000)
        }
    }
}