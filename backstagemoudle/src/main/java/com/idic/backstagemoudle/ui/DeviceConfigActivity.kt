package com.idic.backstagemoudle.ui

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.CleanUtils
import com.blankj.utilcode.util.SPUtils
import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.BackStageArouter
import com.idic.backstagemoudle.R
import com.idic.backstagemoudle.alipay.response.AliPayConfig
import com.idic.backstagemoudle.databinding.ActivityDeviceConfigBinding
import com.idic.backstagemoudle.db.DeviceConfigDataBase
import com.idic.backstagemoudle.db.entity.DeviceConfigEntity
import com.idic.backstagemoudle.util.DialogUtil
import com.idic.basecommon.SPKeys
import com.idic.utilmoudle.AppUtil
import com.idic.utilmoudle.DKToastUtils
import kotlinx.android.synthetic.main.activity_device_config.*
import kotlinx.android.synthetic.main.back_start_toolbar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import serialport_idic.SerialPortFinder

class DeviceConfigActivity :
    AppCompatActivity(),
    DeviceOnClickCallback {

    companion object {
        const val aliPayTag = "aliPay"
    }


    private lateinit var configBinding: ActivityDeviceConfigBinding

    //最终需要保存的内存
    private var deviceConfig: DeviceConfigEntity? = null

    //记录修改的内容，也就是页面展示的内容
    private var tempDeviceConfig: DeviceConfigEntity? = null

    //当前聚焦的editText
    private var curModifyEditText: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_device_config
        )
        tvTitle.text = getString(R.string.deviceConfig)
        initView()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            tempDeviceConfig?.let { config ->
                GlobalScope.launch {
                    data?.let {
                        val aliPayConfig = it.getParcelableExtra<AliPayConfig>(aliPayTag)
                        config.aliUserId = aliPayConfig.aliUserId
                        config.alipayPublic = aliPayConfig.alipayPublic
                        config.appPrivate = aliPayConfig.appPrivate
                        config.appId = aliPayConfig.appId
                        DeviceConfigDataBase.getInstance().deviceConfigDao()
                            .updateContainer(config)
                        AppUtil.relaunch()
                    }
                }
            }
        }
    }

    private fun initView() {
        switchAdult.isChecked = SPUtils.getInstance().getBoolean(SPKeys.ADULT, true)
        GlobalScope.launch {
            tempDeviceConfig =
                DeviceConfigDataBase.getInstance().deviceConfigDao().getAllConfig().firstOrNull()
            configBinding.deviceConfig = tempDeviceConfig
            configBinding.deviceConfigCallback = this@DeviceConfigActivity
            deviceConfig =
                DeviceConfigDataBase.getInstance().deviceConfigDao().getAllConfig().firstOrNull()
            val allDevicesPath = SerialPortFinder().allDevicesPath
            val shipmentIndex = allDevicesPath.indexOf("${deviceConfig?.shipmentSerial}")
            val keyDownIndex = allDevicesPath.indexOf("${deviceConfig?.keyEventSerial}")
            val vipSerialIndex = allDevicesPath.indexOf("${deviceConfig?.vipCardSerial}")

            runOnUiThread {

                val shipmentAdapter = ArrayAdapter<String>(
                    this@DeviceConfigActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    allDevicesPath
                )
                spinnerShipmentSerial.adapter = shipmentAdapter
                spinnerShipmentSerial.setSelection(shipmentIndex)

                val keyDownAdapter = ArrayAdapter<String>(
                    this@DeviceConfigActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    allDevicesPath
                )
                spinnerKeyDownSerial.adapter = keyDownAdapter
                XLog.d("keyDownIndex--->$keyDownIndex")
                spinnerKeyDownSerial.setSelection(keyDownIndex)

                val vipCarAdapter = ArrayAdapter<String>(
                    this@DeviceConfigActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    allDevicesPath
                )
                spinnerVipCarSerial.adapter = vipCarAdapter
                XLog.d("vipSerialIndex--->$vipSerialIndex")
                spinnerVipCarSerial.setSelection(vipSerialIndex)
            }
        }

        tvSave.setOnClickListener {
            saveModify()
        }
        tvBack.setOnClickListener {
            if (isModify()) {
                DialogUtil.createDialog(this, {
                    saveModify()
                }, {
                    finish()
                })?.show()
            } else {
                finish()
            }
        }
        tvChangeUser?.setOnClickListener {
            CleanUtils.cleanInternalDbs()
            CleanUtils.cleanInternalSp()
            AppUtil.relaunch()
        }
    }

    private fun saveModify() {
        if (isModify()) {
            SPUtils.getInstance().put(SPKeys.ADULT, switchAdult.isChecked, true)
            tempDeviceConfig?.let {
                if (it.AliPaySmilePay && !deviceConfig!!.AliPaySmilePay) {
                    ARouter.getInstance().build(BackStageArouter.DOWNLOAD_ALIPAY_INFO)
                        .navigation(this, 1000)
                } else {
                    GlobalScope.launch {
                        DeviceConfigDataBase.getInstance().deviceConfigDao()
                            .updateContainer(it)
                        AppUtil.relaunch()
                    }
                }
            }
        } else {
            DKToastUtils.showCustomLong("未做更改,无需保存")
        }

    }

    //editText的内容发生改变时回调，切勿对editText设置内容，否则会导致死循环
    override fun afterTextChanged(
        charSequence: CharSequence,
        deviceConfigEntity: DeviceConfigEntity
    ) {
        val newUrl = charSequence.toString().trim().replace(" ", "")
        curModifyEditText?.let {
            when (it.id) {
                R.id.editShopCarMax -> {
                    try {
                        if (newUrl.isEmpty()) {
                            tempDeviceConfig?.shopcarMax = 0
                        } else {
                            tempDeviceConfig?.shopcarMax = newUrl.toInt()
                        }
                        (it.parent.parent as View).isSelected = isShopCarModify()
                    } catch (e: Exception) {
                        tempDeviceConfig?.shopcarMax = deviceConfig!!.shopcarMax
                    }
                }
                R.id.editDomainApi -> {
                    tempDeviceConfig?.domain = newUrl
                    (it.parent.parent as View).isSelected = isDomainModify()
                }
                R.id.editDomainPort -> {
                    tempDeviceConfig?.port = newUrl
                    (it.parent.parent as View).isSelected = isDomainModify()
                }
                R.id.editDomainImg -> {
                    tempDeviceConfig?.imgBaseURL = newUrl
                    (it.parent.parent as View).isSelected = isDomainModify()
                }
                R.id.editDomainImgPort -> {
                    tempDeviceConfig?.imgPort = newUrl
                    (it.parent.parent as View).isSelected = isDomainModify()
                }
                R.id.editDeviceName -> {
                    tempDeviceConfig?.DeviceTitle = newUrl
                    (it.parent.parent as View).isSelected = isInfoModify()
                }
                R.id.editDevicePhone -> {
                    tempDeviceConfig?.helpPhone = newUrl
                    (it.parent.parent as View).isSelected = isInfoModify()
                }
                R.id.editPayTime -> {
                    try {
                        if (newUrl.isEmpty()) {
                            tempDeviceConfig?.payTimer = 80
                        } else {
                            tempDeviceConfig?.payTimer = newUrl.toInt()
                        }
                        (it.parent.parent as View).isSelected = isOtherSettingModify()
                    } catch (e: Exception) {
                        tempDeviceConfig?.shopcarMax = deviceConfig!!.shopcarMax
                    }
                }
            }
        }
    }

    //当edittext聚焦时会调用
    override fun onFocusChange(view: View, hasFocus: Boolean) {
        curModifyEditText = if (hasFocus) {
            view as EditText
        } else {
            null
        }
    }

    //串口spinner的选择事件
    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        val devicePath = parent.selectedItem as String
        when (parent.id) {
            R.id.spinnerShipmentSerial -> {
                tempDeviceConfig?.shipmentSerial = devicePath
                XLog.d("选择出货串口--->$devicePath")
            }
            R.id.spinnerKeyDownSerial -> {
                tempDeviceConfig?.keyEventSerial = devicePath
                XLog.d("选择按键串口--->$devicePath")
            }
            R.id.spinnerVipCarSerial -> {
                tempDeviceConfig?.vipCardSerial = devicePath
                XLog.d("选择会员卡--->$devicePath")
            }
        }
        (parent.parent.parent as View).isSelected = isSerialModify()
    }

    private fun isModify(): Boolean {
        return isOtherSettingModify() || isInfoModify() || isSerialModify() || isDomainModify() || isVipCarModify() || isShopCarModify()
    }

    //串口设置是否发生了更改
    private fun isSerialModify(): Boolean {
        return tempDeviceConfig!!.shipmentSerial != deviceConfig!!.shipmentSerial ||
                tempDeviceConfig!!.keyEventSerial != deviceConfig!!.keyEventSerial ||
                tempDeviceConfig!!.vipCardSerial != deviceConfig!!.vipCardSerial

    }

    //购物车设置是否发生了更改
    private fun isShopCarModify(): Boolean {
        return tempDeviceConfig!!.openShopCar != deviceConfig!!.openShopCar ||
                tempDeviceConfig!!.shopcarMax != deviceConfig!!.shopcarMax
    }

    //会员卡设置是否发生了更改
    private fun isVipCarModify(): Boolean {
        return tempDeviceConfig!!.openVipCar != deviceConfig!!.openVipCar ||
                tempDeviceConfig!!.vipCarPositive != deviceConfig!!.vipCarPositive
    }

    //域名设置是否发生了更改
    private fun isDomainModify(): Boolean {
        return tempDeviceConfig!!.domain != deviceConfig!!.domain ||
                tempDeviceConfig!!.port != deviceConfig!!.port ||
                tempDeviceConfig!!.imgBaseURL != deviceConfig!!.imgBaseURL ||
                tempDeviceConfig!!.imgPort != deviceConfig!!.imgPort
    }

    //配置信息
    private fun isInfoModify(): Boolean {
        return tempDeviceConfig!!.DeviceTitle != deviceConfig!!.DeviceTitle ||
                tempDeviceConfig!!.helpPhone != deviceConfig!!.helpPhone
    }

    //其他设置信息
    private fun isOtherSettingModify(): Boolean {
        val isAdult = SPUtils.getInstance().getBoolean(SPKeys.ADULT, true)
        return tempDeviceConfig!!.payTimer != deviceConfig!!.payTimer ||
                tempDeviceConfig!!.AliPaySmilePay != deviceConfig!!.AliPaySmilePay ||
                tempDeviceConfig!!.stockIsPay != deviceConfig!!.stockIsPay || isAdult != switchAdult.isChecked
    }
}
