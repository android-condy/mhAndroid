package com.idic.login.db.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * 文 件 名: LoginInfo
 * 创 建 人: sineom
 * 创建日期: 2018/11/16 16:24
 * 修改时间：
 * 修改备注：
 */

@Entity
class DeviceInfo {

    @Id
    var id: Long = 0

    //登录用户名
    var device_name: String = ""

    //登录密码
    var password: String = ""

    //设备key
    var device_key: String = ""

    //设备id
    var deviceId: String = ""

    //商户ID
    var merchantId: String = ""

    //设备编号
    var number: String = ""

    //token
    var accesstoken: String = ""

}