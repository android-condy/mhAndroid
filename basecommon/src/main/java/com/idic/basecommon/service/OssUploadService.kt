package com.idic.basecommon.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider
import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.OSSConfig
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.events.UploadLogEvent
import com.idic.basecommon.utils.CustomObserver
import com.idic.basecommon.utils.FilePath
import com.idic.basecommon.utils.ZipUtils
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileFilter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


/**
 * 文 件 名: OssUploadService
 * 创 建 人: sineom
 * 创建日期: 2019-09-24 10:57
 * 修改时间：
 * 修改备注：
 */

class OssUploadService : Service() {

    private lateinit var mOss: OssService

    private var disposable: Disposable? = null

    @SuppressLint("SimpleDateFormat")
    val dateFormate = SimpleDateFormat("yy-MM-dd HH:mm:ss")

    override fun onCreate() {
        super.onCreate()
        Log.i("condy","OssUploadService onCreate")
        val credentialProvider = OSSAuthCredentialsProvider(OSSConfig.STS_SERVER_URL)
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒
        conf.maxConcurrentRequest = 5 // 最大并发请求书，默认5个
        conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次
        GlobalScope.launch {
            val oss = OSSClient(applicationContext, OSSConfig.endpoint, credentialProvider, conf)
            mOss = OssService(oss, OSSConfig.bucketName)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        disposable?.dispose()
        Log.i("condy","OssUploadService onStartCommand")
        RxRelay.instance.toObserver(UploadLogEvent::class.java)
            .subscribeOn(Schedulers.io())
            .throttleLast(5, TimeUnit.SECONDS)
            .subscribe(CustomObserver<UploadLogEvent>().apply {
                _subscribe {
                    this@OssUploadService.disposable = it
                }
                _onNext {
                    val rootDir = File(FilePath.loggParsent)
                    if (!rootDir.exists()) {
                        return@_onNext
                    }
                    val arrayOfFiles = rootDir.listFiles(FileFilter {
                        it.name.endsWith(".csv") || it.name.endsWith(".back")
                    })
                    val file = File(FilePath.zipLogFile)
                    ZipUtils.zipFiles(
                        ArrayList<File>().apply { addAll(arrayOfFiles) },
                        file,
                        null
                    )

                    mOss.asyncMultipartUpload(
                        DeviceInfo.getInstance().mDeviceNumber +
                                File.separator + dateFormate.format(Date()) +
                                File.separator + "log.zip",
                        file.absolutePath
                    )

                }
            })
        return START_NOT_STICKY

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
