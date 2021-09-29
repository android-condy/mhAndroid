package com.idic.backstagemoudle.data.remote

/**
 * 文 件 名: Product
 * 创 建 人: sineom
 * 创建日期: 2019/1/31 15:20
 * 修改时间：
 * 修改备注：
 */

class ModifyProduct  {

    var id: Long = 0

    //货道编号
    var number = 1

    //锁定库存
    var lockStock = 0

    //产品id
    var productId = ""

    //货道ID
    var deviceWayId = ""

    //货柜号
    var num = 1

    //产品背景图,需拼接完整连接
    var productImg = ""

    //描述
    var synopsis = ""

    //库存
    var stock = 0

    //产品名称
    var productName = ""

    var categoryId = ""

    //产品价格
    var productPrice = "0.00"

    //货道容量
    var capacity = 10

    override fun toString(): String {
        return "Product(id=$id, number=$number, lockStock=$lockStock, productId='$productId', deviceWayId='$deviceWayId', num=$num, productImg='$productImg', synopsis='$synopsis', stock=$stock, productName='$productName', categoryId='$categoryId', productPrice='$productPrice', capacity=$capacity)"
    }


}