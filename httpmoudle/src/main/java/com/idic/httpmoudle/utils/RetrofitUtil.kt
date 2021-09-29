/*
 * Copyright (C) 2016 david.wei (lighters)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idic.httpmoudle.utils

import android.util.Log
import com.elvishew.xlog.XLog
import com.idic.httpmoudle.IApi
import com.idic.httpmoudle.URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by david on 16/8/19.
 * Email: huangdiv5@gmail.com
 * GitHub: https://github.com/alighters
 */
class RetrofitUtil {

    private var api: Retrofit

    private val TIMEOUT = 30L

    private var client: OkHttpClient

    private val okHttp = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(LogInterceptor())



    init {
        client = okHttp.build()
        api = Retrofit.Builder().client(client)
            .baseUrl(URL.API_IP)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun getDefautlRetrofit() = api.create(IApi::class.java)


    fun <T> getApiService(service: Class<T>): T {
        return api.create(service)
    }


    companion object {
        val instance = RetrofitHolder.instance
    }

    /**
     * 请求拦截器
     */
    class LogInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()

            var builder = request.newBuilder()
            builder.addHeader("Connection", "close")
//            builder.addHeader("Content-Type", "text/html;charset=UTF-8")
            //the request url
            val url = request.url().toString()

            //the request method
            val method = request.method()
            val t1 = System.nanoTime()
            XLog.i(
                "http",
                String.format(
                    Locale.getDefault(),
                    "Sending %s request [url = %s]",
                    method,
                    url
                )
            )
            XLog.i(
                String.format(
                    Locale.getDefault(),
                    "Sending %s request [url = %s]",
                    method,
                    url
                )
            )
            //the request body
            var requestBody = request?.body()
//            if (requestBody is FormBody) {
//                var bodyBuilder = FormBody.Builder();
//
//                var formBody = request.body() as FormBody;
//                for (i in 0..formBody.size() - 1) {
//                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i))
//                }
//
//                requestBody = bodyBuilder.addEncoded("device_id", URL.device_id)
//                    .build()
//                request = builder.post(requestBody).build()
//            }
            if (isFile(url)) return chain.proceed(request)
            requestBody?.let { it ->
                val sb = StringBuilder("Request Body [")
                val buffer = Buffer()
                it.writeTo(buffer)
                var charset = Charset.forName("UTF-8")
                val contentType = it.contentType()
                contentType?.let {
                    charset = it.charset(charset)
                    sb.append(buffer.readString(charset))
                    sb.append(" (Content-Type = ").append(it.toString()).append(",")
                        .append(requestBody.contentLength()).append("-byte body)")
                    sb.append(" (Content-Type = ").append(it.toString())
                        .append(",binary ").append(requestBody.contentLength())
                        .append("-byte body omitted)")
                    sb.append("]")
                    XLog.i(String.format(Locale.getDefault(), "%s %s", method, URLDecoder.decode(sb.toString())))

                }
            }
            val response = chain.proceed(request)
            val t2 = System.nanoTime()
            //the response time
            XLog.i(
                "http",
                String.format(
                    Locale.getDefault(),
                    "Received response for [url = %s] in %.1fms",
                    url,
                    (t2 - t1) / 1e6
                )
            )
            XLog.i(
                String.format(
                    Locale.getDefault(),
                    "Received response for [url = %s] in %.1fms",
                    url,
                    (t2 - t1) / 1e6
                )
            )

            //the response state
            XLog.i(
                "http",
                String.format(
                    Locale.CHINA,
                    "Received response is %s ,message[%s],code[%d]",
                    if (response.isSuccessful) "success" else "fail",
                    response.message(),
                    response.code()
                )
            )
            XLog.i(
                String.format(
                    Locale.CHINA,
                    "Received response is %s ,message[%s],code[%d]",
                    if (response.isSuccessful) "success" else "fail",
                    response.message(),
                    response.code()
                )
            )

            //the response data
            val body = response?.body()

            val source = body!!.source()
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()
            var charset = Charset.defaultCharset()
            val contentType = body.contentType()
            if (contentType != null) {
                charset = contentType.charset(charset)
            }
            val bodyString = buffer.clone().readString(charset)

            XLog.i("http", String.format("Received response json string [%s]", bodyString.toString()))
            XLog.i( String.format("Received response json string [%s]", bodyString.toString()))
            return response
        }

        //unicode ->String
        fun decode(encodeText: String): String {
            fun decode1(unicode: String) = unicode.toInt(16).toChar()
            val unicodes = encodeText.split("\\u")
                .map { if (it.isNotBlank()) decode1(it) else null }.filterNotNull()
            return String(unicodes.toCharArray())
        }

        private fun isFile(url: String): Boolean {
            return url.endsWith(".mp4") ||
                    url.endsWith(".jpg") ||
                    url.endsWith(".mp4") ||
                    url.endsWith(".jpeg") ||
                    url.endsWith(".png") ||
                    url.endsWith(".webp")
        }
    }

    private object RetrofitHolder {
        val instance = RetrofitUtil()
    }


}
