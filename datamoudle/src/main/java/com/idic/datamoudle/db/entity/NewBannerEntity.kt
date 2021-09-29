package com.idic.datamoudle.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.idic.httpmoudle.URL

/**
 *  "id": 4,
"device_id": "",
"img_url": "http://sl-mh-az.oss-cn-hangzhou.aliyuncs.com/product/a0ee3906e8924ebca097e9106bb41f85.jpg",
"type": "首页轮播",
"sort": 0,
"status": 0,
"created_at": "2021-02-22 14:58:49",
"updated_at": null,
"deleted_at": null
 */
class NewBannerEntity {
    var id = 0
    var device_id = ""
    var img_url = ""
        get() {
            return if (field.startsWith("http")) {
                field.replace("\\", "")
            } else {
                ""
            }
        }
    var type = ""
    var sort = 0
    var status = 0
    var created_at = ""
    var updated_at = ""
    var deleted_at = ""
    var video_url=""
//        get() {
//            return if (field.startsWith("http")) {
//                field.replace("\\", "")
//            } else {
//                ""
//            }
//        }
    var flag = 1
    var flash_version =0
    override fun toString(): String {
        return "NewBannerEntity(id=$id, device_id='$device_id', type='$type', sort=$sort, status=$status, created_at='$created_at', updated_at='$updated_at', deleted_at='$deleted_at', img_url='$img_url')"
    }


}

