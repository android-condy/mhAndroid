package com.idic.login.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * 文 件 名: LoginInfo
 * 创 建 人: sineom
 * 创建日期: 2019-07-30 18:06
 * 修改时间：
 * 修改备注：
 */

@Entity
data class LoginInfo(
    var userName: String, //用户名
    var password: String, //密码
    @PrimaryKey
    var activationCode: String //激活码
)