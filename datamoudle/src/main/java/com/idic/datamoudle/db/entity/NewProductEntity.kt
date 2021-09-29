package com.idic.datamoudle.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.idic.basecommon.utils.FilePath
import com.idic.httpmoudle.URL
import java.util.*

/**
"id": 5747,
"price": "99.00",
"sku_code": "4718006553746",
"sku_name": "",
"type_id": 1,
"image_url": ""
 */
class NewProductEntity {
    var id = 0
    var price = ""
    var sku_code = ""
    var sku_name = ""
    var image_url = ""
        get() {
            if (field.isNullOrEmpty()) {
                return field
            } else {
                return field.replace("\\", "")
            }
        }
    var type_id = ""

//
//    //:商品条码,
//    var barcode = ""
//
//    //:商品编码,
//    var commodityCode = ""
//
//    //:商品尺寸,
//    var productSize = ""
//
//    //:商品材质
//    var productQuality = ""
//
//    //购买的数量 默认一个
//    @Ignore
//    var payCount = 0
//
//    //货道编号
//    var number = 1
//
//    //锁定库存
//    var lockStock = 0
//
//    //产品id
//    var productId = ""
//
//    @PrimaryKey
//    var deviceWayId = ""
//
//    //货柜号
//    var num = 1
//
//    //产品背景图,需拼接完整连接
//    var productImg = ""
//        get() {
//            return when {
//                field.isNullOrEmpty() -> {
//                    return field
//                }
//                field.contains(URL.IMG_DOMIAN) -> {
//                    field
//                }
//                else -> "${URL.IMG_DOMIAN}$field"
//            }
//        }
//
//    //描述
//    var synopsis: String? = ""
//
//    //库存
//    var stock = 0
//
//    //产品名称
//    var productName = ""
//
//    var categoryId: String = ""
//
//    //产品价格
//    var productPrice = 0.00F
//
//    //货道容量
//    var capacity = 10
//
//    //货道名称 如 1行1列
//    var goodsWay = ""
//
//    //图片的本地路径
//    var localImg = ""
//        get() = FilePath.imgPath + productImg.split("/").lastOrNull()
//            ?: "${UUID.randomUUID()}.jpg"
//
//
//    @Ignore
//    fun update(productEntity: NewProductEntity) {
//        productEntity.also {
//            stock = it.stock
//            lockStock = it.lockStock
//            capacity = it.capacity
//        }
//    }
//
//
//    /**
//     * 获取支付的价格
//     */
//    @Ignore
//    fun getPayPrice() = payCount * productPrice
//
//    /**
//     * 新增购买的数量
//     */
//    @Ignore
//    @Synchronized
//    fun addPayCount(): Boolean {
//        if (payCount > stock) {
//            payCount = stock
//            return false
//        }
//        payCount += 1
//        return true
//    }
//
//    //重置购买数量
//    @Ignore
//    fun resetPayCount() {
//        payCount = 0
//    }
//
//    override fun toString(): String {
//        return "ProductEntity(payCount=$payCount, number=$number, lockStock=$lockStock, productId='$productId', deviceWayId='$deviceWayId', num=$num, synopsis=$synopsis, stock=$stock, productName='$productName', categoryId='$categoryId', productPrice=$productPrice, capacity=$capacity, goodsWay='$goodsWay')"
//    }

}