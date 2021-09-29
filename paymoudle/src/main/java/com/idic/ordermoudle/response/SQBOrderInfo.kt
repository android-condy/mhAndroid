package com.idic.ordermoudle.response

import com.idic.httpmoudle.response.DKResponse


class SQBProductVoItem {
    var buyCount: String = ""
    var wayName: String? = null
    var productId: String = ""
    var deviceWayId: String? = null
    var orderId: String? = null
    var updateTime: String? = null
    var cTime: String? = null
    var deviceId: String? = null
    var productName: String = ""
    var aisleId: String? = null
    var productCost: String = ""
    var createTime: String? = null
    var delStatus: String? = null
    var uTime: String? = null
    var id: String? = null

    override fun toString(): String {
        return "SQBProductVoItem(buyCount='$buyCount', wayName=$wayName, productId='$productId', deviceWayId=$deviceWayId, orderId=$orderId, updateTime=$updateTime, cTime=$cTime, deviceId=$deviceId, productName='$productName', aisleId=$aisleId, productCost='$productCost', createTime=$createTime, delStatus=$delStatus, uTime=$uTime, id=$id)"
    }


}


class SQBDevList {
    var columnSeial: String? = null
    var lockStock: String = "0"
    var orderId: String = ""
    var tradeNum: String? = null
    var num: String? = null
    var tradeCost: String? = null
    var type: String? = null
    var deviceId: String? = null
    var productName: String? = null
    var capacity: String? = null
    var id: String = ""
    var stock: String = "0"
    var containerId: String? = null
    var buyCount: String? = null
    var containerNum: String? = null
    var rowSeial: String? = null
    var productId: String = ""
    var productImg: String? = null
    var updateTime: String? = null
    var cSeial: String? = null
    var goodsWay: String? = null
    var createTime: String? = null
    var rSeial: String? = null
    var typeId: String? = null
    var typeValue: String? = null
    var productPrice: String? = null
    var status: String? = null
    var syStock: Int = 0

    override fun toString(): String {
        return "SQBDevList(columnSeial=$columnSeial, lockStock='$lockStock', orderId='$orderId', tradeNum=$tradeNum, num=$num, tradeCost=$tradeCost, type=$type, deviceId=$deviceId, productName=$productName, capacity=$capacity, id='$id', stock='$stock', containerId=$containerId, buyCount=$buyCount, containerNum=$containerNum, rowSeial=$rowSeial, productId='$productId', productImg=$productImg, updateTime=$updateTime, cSeial=$cSeial, goodsWay=$goodsWay, createTime=$createTime, rSeial=$rSeial, typeId=$typeId, typeValue=$typeValue, productPrice=$productPrice, status=$status, syStock=$syStock)"
    }

}


class SQBOrderInfo : DKResponse() {

    var totalAmount: String = ""
    var orderId: String = ""
    var devList: List<SQBDevList>? = null
    var orderCode: String = ""
    var orderCost: String = ""
    var id: String = ""
    var productNum: String = ""
    var orderProductVo: List<SQBProductVoItem>? = null
    var subjectName: String = ""
    override fun toString(): String {
        return "SQBOrderInfo(totalAmount='$totalAmount', orderId='$orderId', devList=$devList, orderCode='$orderCode', orderCost='$orderCost', id='$id', productNum='$productNum', orderProductVo=$orderProductVo, subjectName='$subjectName')"
    }

}


