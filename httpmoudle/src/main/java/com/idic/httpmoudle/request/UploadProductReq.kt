package com.idic.httpmoudle.request

/**
 * 文 件 名: UploadProduct
 * 创 建 人: sineom
 * 创建日期: 2019/3/11 15:23
 * 修改时间：
 * 修改备注： 更新货道产品库存信息
 */

data class UploadProductReq(
    //锁定库存
    val lockStock: String,
    //产品id
    val productId: String,
    //库存
    var stock: String,
    //货道id
    val deviceWayId: String,
    //价格
    val bingdingPrice: String,
    //货道容量
    val capacity: String
) : DKRequest()