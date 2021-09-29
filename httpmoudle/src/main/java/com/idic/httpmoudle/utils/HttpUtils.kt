package com.idic.httpmoudle.utils

import android.util.Log
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * 文 件 名: HttpUtils
 * 创 建 人: sineom
 * 创建日期: 2019-08-02 15:14
 * 修改时间：
 * 修改备注：
 */

object HttpUtils {

    private val gson = Gson()
//    private val midiaType = MediaType.parse("application/json; charset=utf-8")
    private val midiaType = MediaType.parse("text/html;charset=UTF-8")

    fun createBody(json: String): RequestBody {
        return RequestBody.create(midiaType, json)
    }
    fun createBody(): RequestBody {
        return RequestBody.create(null,"")
    }

    fun createBody(any: Any): RequestBody {
        Log.i("condy","json="+gson.toJson(any))
        return RequestBody.create(midiaType, gson.toJson(any))

    }
}