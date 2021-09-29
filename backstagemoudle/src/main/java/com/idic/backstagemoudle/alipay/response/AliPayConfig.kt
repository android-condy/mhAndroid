package com.idic.backstagemoudle.alipay.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 文 件 名: AliPayConfig
 * 创 建 人: sineom
 * 创建日期: 2019-08-16 13:39
 * 修改时间：
 * 修改备注：
 */

@Parcelize
data class AliPayConfig(

    val appId: String,

    val appPrivate: String,

    val alipayPublic: String,

    val aliUserId: String

) : Parcelable