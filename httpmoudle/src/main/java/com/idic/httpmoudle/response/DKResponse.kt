package com.idic.httpmoudle.response

import com.idic.httpmoudle.HttpCode

/**
 * 文 件 名: DKReponse
 * 创 建 人: sineom
 * 创建日期: 2019/1/31 14:17
 * 修改时间：
 * 修改备注：
 */

open class DKResponse {

    var status = -1

    var msg = ""

    var data: Any? = null

    fun isSuccess() = status == HttpCode.OK

    override fun toString(): String {
        return "DKReponse(status=$status, msgBuffer='$msg', data=${data ?: "null"})"
    }

    class ErrorData {
        var code = -1
        var msg = ""
    }

}