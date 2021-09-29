package com.idic.backstagemoudle.data.remote

import com.idic.httpmoudle.URL

//加载产品列表信息
class SpinnerSelectProduct {

    var productId: String = ""
    var productName: String = ""

    var productPrice: String = ""
    var pinyin = ""
    var abbreviations = ""

    //图片地址
    var productImg = ""
        get() {
            return if (field.contains(URL.IMG_DOMIAN)) {
                field
            } else
                "${URL.IMG_DOMIAN}$field"
        }
}