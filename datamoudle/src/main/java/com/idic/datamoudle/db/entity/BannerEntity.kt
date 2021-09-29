package com.idic.datamoudle.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.idic.httpmoudle.URL

/**
 * 文 件 名: BannerInfo
 * 创 建 人: sineom
 * 创建日期: 2018/11/16 16:26
 * 修改时间：
 * 修改备注：
 */

@Entity
class BannerEntity {

    //图片地址 需拼接
    @PrimaryKey
    var image: String = ""
        get() {
            return if (!field.startsWith("http")) {
                "${URL.IMG_DOMIAN}$field"
            } else {
                field
            }
        }

    //图片描述
    var bannerExplain: String? = ""

    var id: String? = ""

    //图片域名
    var url: String = ""

    var bannerType = 0

    var localPath: String = ""

    //banner类型
    var type: Int = 1

    //图片默认轮播时间
    var playTime: Long = 6

    fun isVideo() = bannerType == 1

    fun isImage() = bannerType == 0

    override fun toString(): String {
        super.toString()
        return "Banner(image=$image, bannerExplain=$bannerExplain, id=$id, url=$url)"
    }

}

