package com.idic.basecommon.service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * 文 件 名: DeviceConfigServer
 * 创 建 人: sineom
 * 创建日期: 2019-09-05 14:21
 * 修改时间：
 * 修改备注：
 */

interface DeviceConfigServer : IProvider {

    fun initDeviceConfig()

//    //获取设备标题
//    fun getDeviceTitle(): String
//
//    //获取客服电话
//    fun getHelpPhone(): String
//
//    //是否开启支付宝熟练福
//    fun isOpenAliPaySmile(): Boolean
//
//    //支付计时时间 单位秒
//    fun getPayTime(): Int
//
//    //应用id
//    fun getAppId(): String
//
//    //应用私钥
//    fun getAppPrivate(): String
//
//    //应用公钥
//    fun getAliPayPublic(): String
//
//    //用户id
//    fun getAliUserId(): String
//
//    //库存同步依据
//    fun stockIsPay(): Boolean
//
//    //会员卡是否反转 true 正向 反之反向
//    fun positiveCardData(): Boolean
//
//    //获取购物车最大容量
//    fun getMaxShopCapCapacity(): Int
//
//    //购物车是否开启
//    fun isOpenShopCar(): Boolean
//
    //设置跳转的主界面
    fun setMainPath(routerPath: String)
//
//    //获取跳转的主界面
//    fun getMainPath(): String
//
//    //是否隐藏相同产品
//    fun hideProductTheSame(): Boolean
}