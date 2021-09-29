package com.idic.datamoudle.download

import android.util.Log
import com.alibaba.fastjson.util.IOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.Utils
import com.elvishew.xlog.XLog
import com.idic.basecommon.utils.CustomObserver
import com.idic.basecommon.utils.FilePath
import com.idic.datamoudle.utils.ConfigSharedPreferences
import com.idic.httpmoudle.utils.RetrofitUtil
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


/**
 * 文 件 名: DownloadImpl
 * 创 建 人: sineom
 * 创建日期: 2019-10-14 11:07
 * 修改时间：
 * 修改备注：
 */

class DownloadImpl : IDownload {

    override fun downloadFileAsync(
        url: String,
        localFile: File,
        listener: ((Boolean) -> Unit)
    ) {
        if (FileUtils.isFileExists(localFile)) {
            listener.invoke(true)
            Log.i("http","文件已存在")
            return
        }
        RetrofitUtil.instance.getDefautlRetrofit()
            .downloadFile(url)
            .subscribeOn(Schedulers.io())
            .map {
                writeFile2Disk(it, localFile)
            }.subscribe(CustomObserver<Boolean>().apply {
                _onNext {
                    Log.i("http","下载结果$it")
                    listener.invoke(it)
                }
                _onError {
                    listener.invoke(false)
                    Log.i("http","下载失败")
                }
            })

    }

    override fun downloadFileAsync(
        url: String,
        localFile: String,
        listener: ((Boolean) -> Unit)
    ) {
        return downloadFileAsync(url, File(localFile), listener)
    }

    override fun downloadFile(url: String, localFile: String): Boolean {
        return downloadFile(url, File(localFile))
    }

    override fun downloadFile(url: String, localFile: File): Boolean {
        if (FileUtils.isFileExists(localFile)) {
            Log.i("http","文件已存在")
            return true
        }
        var result = false
        RetrofitUtil.instance.getDefautlRetrofit()
            .downloadFile(url)
            .map {
                writeFile2Disk(it, localFile)
            }.subscribe(CustomObserver<Boolean>().apply {
                _onNext {
                    result = it
                }
                _onError {
                    Log.i("http","下载结果2--->${it}")
                    result = false
                }
            })
        Log.i("http","下载结果--->$result")
        return result
    }

    private fun writeFile2Disk(response: ResponseBody, file: File): Boolean {
        if(!FileUtils.createOrExistsFile(file)){
            ConfigSharedPreferences.saveSharedPreferences(Utils.getApp(), "")
            ConfigSharedPreferences.saveSharedPreferences(Utils.getApp(), 0)
        }
        var result: Boolean
        val inputStream = response.byteStream()
        var outputStream: OutputStream? = null

//        Log.i("http","下载结果--->${ response.bytes().size/1024}kb")
        Log.i("http","本地地址--->"+file.absolutePath)
        try {
            outputStream = FileOutputStream(file)
            var len: Int
            val buff = ByteArray(1024)
            while (inputStream.read(buff).also { len = it } != -1) {
                Log.i("http","下载中--->"+len)
                outputStream.write(buff, 0, len)
            }
            result = true
        } catch (e: Exception) {
            Log.i("http","下载结果1--->"+e.toString())
            result = false
        } finally {
            IOUtils.close(outputStream)
            IOUtils.close(inputStream)
        }
        return result
    }
}