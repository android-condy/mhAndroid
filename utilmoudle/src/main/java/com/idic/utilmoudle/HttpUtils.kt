package com.idic.utilmoudle

/**
 * Created by yueweizyw on 17/9/26.
 */

import android.util.Log
import com.elvishew.xlog.XLog
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import java.util.*


/**
 * Created by bruce on 2017/12/25.
 */
internal object HttpUtils {

    fun postKeyValeData(url: String, paramsValue: Map<String, String>): String {

        //用HttpClient发送请求，分为五步
        //第一步：创建HttpClient对象
        val httpCient = DefaultHttpClient()
        //第二步：创建代表请求的对象,参数是访问的服务器地址
        var response = ""
        try {
            //第三步：执行请求，获取服务器发还的相应对象
            val nvps = ArrayList<NameValuePair>()
            for (key in paramsValue.keys) {
                nvps.add(BasicNameValuePair(key, paramsValue[key]))
            }

            val str = EntityUtils.toString(UrlEncodedFormEntity(nvps, "utf-8"))

            val absoluteURL = "$url?$str"
            Log.i("Test", "absoluteURL:$absoluteURL")

            val httpPost = HttpPost(absoluteURL)
            //.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            val httpResponse = httpCient.execute(httpPost) as HttpResponse
            //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
            if (httpResponse.statusLine.statusCode == 200) {
                //第五步：从相应对象当中取出数据，放到entity当中
                val entity = httpResponse.entity
                response = EntityUtils.toString(entity, "utf-8")//将entity当中的数据转换为字符串

                Log.i("Test", "response:$response")
            }

        } catch (ignored: Exception) {
        }
        return response
    }

    fun postJsonData(url: String, paramsValue: String): String {
        var response = ""
        try {
            //用HttpClient发送请求，分为五步
            //第一步：创建HttpClient对象
            val httpCient = DefaultHttpClient()
            //第二步：创建代表请求的对象,参数是访问的服务器地址
            val httpPost = HttpPost(url)// 创建httpPost
            httpPost.setHeader("Accept", "application/json")
            httpPost.setHeader("Content-Type", "application/json")
            val charSet = "UTF-8"
            val entity = StringEntity(paramsValue, charSet)
            httpPost.entity = entity
            XLog.i("absoluteURL:$url")
            val httpResponse = httpCient.execute(httpPost) as HttpResponse
            //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
            if (httpResponse.statusLine.statusCode == 200) {
                //第五步：从相应对象当中取出数据，放到entity当中
                val entity = httpResponse.entity
                response = EntityUtils.toString(entity, "utf-8")//将entity当中的数据转换为字符串
                XLog.i("response:$response")
            }
        } catch (ignored: Exception) {
        }
        return response

    }
}

