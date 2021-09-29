package com.idic.ordermoudle.response

import com.idic.httpmoudle.response.DKResponse

/**
 * 文 件 名: CardDetails
 * 创 建 人: sineom
 * 创建日期: 2019-07-26 09:30
 * 修改时间：
 * 修改备注：
 */

class CardDetails : DKResponse() {

    /**
     * id : 39158331929792346
     * operatorId : 39158331929788419
     * distributorId : 39158331929788422
     * merchantId : 39158331929788425
     * code : 1933272306
     * cardCode : 1933272306
     * cardNumber : 00000000602
     * accountBalance : 0
     * name : 迪卡测试会员卡602
     * sex : 1
     * provinceId : 340000
     * cityId : 340100
     * countiesId : 340103
     * descritpion : 中辰创富大厦
     * phone : 15659225111
     * address : 安徽省合肥市庐阳区中辰创富大厦
     * discountRate : null
     * state : 0
     * delStatus : 0
     * createTime : 1563506430000
     * updateTime : 1563530017000
     */

    var id: Long = 0
    var operatorId: Long = 0
    var distributorId: Long = 0
    var merchantId: Long = 0
    var code: String? = null
    var cardCode: String? = null
    var cardNumber: String? = null
    var accountBalance: Int = 0
    var name: String? = null
    var sex: String? = null
    var provinceId: Int = 0
    var cityId: Int = 0
    var countiesId: Int = 0
    var descritpion: String? = null
    var phone: String? = null
    var address: String? = null
    var discountRate: Any? = null
    var state: Int = 0
    var delStatus: Int = 0
    var createTime: Long = 0
    var updateTime: Long = 0
}
