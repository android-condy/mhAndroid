package com.idic.backstagemoudle.db.entity

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import android.os.Parcelable
import android.os.storage.StorageVolume
import com.idic.basecommon.utils.MotorType
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * 文 件 名: AisleEntity
 * 创 建 人: sineom
 * 创建日期: 2019-08-06 14:29
 * 修改时间：
 * 修改备注： 货道设置
 */

/**
 * {"id":5742,
 * "price":"99.00",
 * "sku_code":"4983164159820",
 * "sku_name":"u8fd9u662fu4e2au6d4bu8bd5u5546u54c12",
 * "type_id":1,"caizhi":null,"aisles":1,"stock":666,
 * "image_url":"http://sl-nd.oss-cn-hangzhou.aliyuncs.com/productImg/20210207145508w1VX95KOIh12CCX0.jpg"
 */

class AislesBindGoodsEntity(
    var aisles: Int,
    var id: Int,
    var price: String = "",
    var sku_code: String = "",
    var type_id: String = "",
    var sku_name: String = "",
    var caizhi: String = "",
    var stock: Int,
    var image_url: String = "",
    var egg_code_x: Int,
    var egg_code_y: Int,
    var volume: Int

) {


    override fun toString(): String {
        return "AislesBindGoodsEntity(aisles=$aisles, id=$id, price='$price', sku_code='$sku_code', type_id='$type_id', sku_name='$sku_name', caizhi='$caizhi', stock=$stock, image_url='$image_url')"
    }
}