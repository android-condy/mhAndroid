package com.idic.datamoudle.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * "id": 1,
"title": "龙珠系列",
"sort": 4,
"img_url": "http://sl-mh-az.oss-cn-hangzhou.aliyuncs.com/product/da777a0e7071454c9de848afbaf5e002.jpg",
"status": 0,
"created_at": "2021-02-23 10:47:43",
"updated_at": "2021-02-24 13:21:53",
"deleted_at": null
 */

class NewCategoryEntity {

    //类目名称
    var title: String = ""

    //图片地址
    var img_url: String = ""
        get() {
            if (field.isNullOrEmpty()) {
                return field
            } else {
                return field.replace("\\", "")
            }
        }

    var created_at: String = ""
    var updated_at: String = ""
    var deleted_at: String = ""

    //状态
    var status: String = ""

    var sort: Int? = 0
    var id: Int? = 0
}