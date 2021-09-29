package com.idic.backstagemoudle.data.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//加载货柜货道信息api/device/loadDeviceContain.do
@Parcelize
data class ContainerChannerInfo(

    var wayId: String = "",
    //列数
    var columnSeial: String = "0",
    //行数
    var rowSeial: String = "0",
    var lockStock: String = "0",
    //货道信息
    var goodsWay: String = "",
    var productId: String = "",
    var stock: String = "0",
    var productName: String = "",
    var status: String = "",
    //货道容量
    var capacity: String = "0"
) : Parcelable