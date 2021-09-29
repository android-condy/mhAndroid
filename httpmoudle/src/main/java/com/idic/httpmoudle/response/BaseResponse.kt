package com.idic.httpmoudle.response



open class BaseResponse {
    //0 正常返回
    var code = -1

    var msg = ""

    var data: Any? = null

    fun isSuccess() = code == 0
    override fun toString(): String {
        return "BaseResponse(code=$code, msg='$msg', data=$data)"
    }


    class ErrorData {
        var code = -1
        var msg = ""
    }

}