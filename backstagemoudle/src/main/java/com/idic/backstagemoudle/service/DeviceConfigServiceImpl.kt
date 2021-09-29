package com.idic.backstagemoudle.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.idic.backstagemoudle.BackstageApp
import com.idic.backstagemoudle.db.entity.DeviceConfigEntity
import com.idic.basecommon.DeviceConfig
import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.service.DeviceConfigServer

/**
 * 文 件 名: DeviceConfigServiceImpl
 * 创 建 人: sineom
 * 创建日期: 2019-09-05 14:26
 * 修改时间：
 * 修改备注：
 */
@Route(path = "/backstage/deviceConfig")
class DeviceConfigServiceImpl : DeviceConfigServer {

    private var deviceConfig: DeviceConfigEntity? = null

    override fun initDeviceConfig() {
        DeviceInfo.getInstance().deviceConfig = deviceConfig ?: DeviceConfig()
    }


    //    override fun hideProductTheSame(): Boolean {
//        return deviceConfig?.hideAsProduct ?: false
//    }
//
//    override fun positiveCardData(): Boolean {
//        return deviceConfig?.vipCarPositive ?: false
//    }
//
//    override fun stockIsPay(): Boolean {
//        return deviceConfig?.stockIsPay ?: false
//    }
//
//    override fun getAppId(): String {
//        return "${deviceConfig?.appId}"
//    }
//
//    override fun getAppPrivate(): String {
//        return "${deviceConfig?.appPrivate}"
//    }
//
//    override fun getAliPayPublic(): String {
//        return "${deviceConfig?.alipayPublic}"
//    }
//
//    override fun getAliUserId(): String {
//        return "${deviceConfig?.aliUserId}"
//    }
//
//
//    override fun getDeviceTitle(): String {
//        return "${deviceConfig?.DeviceTitle}"
//    }
//
//    override fun getHelpPhone(): String {
//        return "${deviceConfig?.helpPhone}"
//    }
//
//    override fun isOpenAliPaySmile(): Boolean {
//        return deviceConfig?.AliPaySmilePay ?: false
//    }
//
//    override fun getPayTime(): Int {
//        return deviceConfig?.payTimer ?: 80
//    }
//
//
    override fun setMainPath(routerPath: String) {
        deviceConfig?.let {
            it.homeRoutePath = routerPath
            BackstageApp.mInstance?.dataBase?.deviceConfigDao()?.updateContainer(it)
        }
    }

    //    override fun getMaxShopCapCapacity(): Int {
//        return deviceConfig?.shopcarMax ?: 3
//    }
//
//    override fun isOpenShopCar(): Boolean {
//        return deviceConfig?.openShopCar ?: false
//    }
//
//    override fun getMainPath(): String {
//        return deviceConfig?.homeRoutePath ?: ""
//    }
//
    override fun init(context: Context?) {
        deviceConfig = BackstageApp.getDeviceConfig()
        initDeviceConfig()
    }
}