package com.idic.datamoudle.db.entity

/**
 * Author: condy
 * Date: 2021/3/15 13:36
 * Description: ${DESCRIPTION}
 */
class NewProductDetail {
    var id: Int = 0
    var iot_id: String = ""
    var egg_code_x: Int = 0
    var egg_code_y: Int = 0
    var sku_code: String = ""
    var caizhi: String = ""
    var aisles: Int = 0
    var size: String = ""
    var sku_name: String = ""
    var price: String = ""
    var image_url: String = ""
        get() {
            if (field.isNullOrEmpty()) {
                return field
            } else {
                return field.replace("\\", "")
            }
        }

    override fun toString(): String {
        return "NewProductDetail(id=$id, iot_id='$iot_id', egg_code_x=$egg_code_x, egg_code_y=$egg_code_y, sku_code='$sku_code', caizhi='$caizhi', aisles=$aisles, size='$size', sku_name='$sku_name', price='$price', image_url='$image_url')"
    }


}