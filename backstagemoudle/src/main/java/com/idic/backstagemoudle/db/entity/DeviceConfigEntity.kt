package com.idic.backstagemoudle.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.idic.basecommon.DeviceConfig
import java.util.*

/**
 * 文 件 名: DeviceConfigEntity
 * 创 建 人: sineom
 * 创建日期: 2019-08-21 13:46
 * 修改时间：
 * 修改备注：
 */
@Entity
data class DeviceConfigEntity(

    @PrimaryKey
    @ColumnInfo(name = "entryid")
    var id: String = UUID.randomUUID().toString()

//    //购物车是否开启
//    var openShopCar: Boolean = false,
//
//    //是否开启会员卡
//    var openVipCar: Boolean = false,
//
//    //true 正序
//    var vipCarPositive: Boolean = true,
//
//    //购物车最大容量，默认3
//    var shopcarMax: Int = 3,
//
//    //默认出货串口
//    var shipmentSerial: String = "/dev/ttyS3",
//
//    //默认按键串口
//    var keyEventSerial: String = "/dev/ttyS1",
//
//    //默认会员卡
//    var vipCardSerial: String = "/dev/ttyS4",
//
//    //跳转到哪个主界面
//    var homeRoutePath: String = "",
//
//    //设备标题
//    var DeviceTitle: String = "无人售货机",
//
//    //联系方式
//    var helpPhone: String = "",
//
//    //域名
//    var domain: String = "",
//
//    //端口
//    var port: String = "",
//
//    //图片端口
//    var imgPort: String = "",
//
//    //图片地址
//    var imgBaseURL: String = "",
//
//    //支付宝刷脸付
//    var AliPaySmilePay: Boolean = false,
//
//    //付款倒计时 单位s
//    var payTimer: Int = 80,
//
//    //库存依据是否付款
//    var stockIsPay: Boolean = false,
//
//    //应用id
//    var appId: String = "",
//
//    //应用私钥
//    var appPrivate: String = "",
//
//    //应用公钥
//    var alipayPublic: String = "",
//
//    //用户id
//    var aliUserId: String = "",
//
//    //隐藏相同的产品
//    var hideAsProduct: Boolean = false

) : DeviceConfig()