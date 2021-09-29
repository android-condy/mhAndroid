package com.idic.login.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import com.idic.basecommon.utils.CustomObserver
import com.idic.httpmoudle.response.DKResponse
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.utilmoudle.AppUtil
import io.reactivex.schedulers.Schedulers

const val DEVICE_ID_KEY = "deviceID"

class UploadStatusService : Service() {


    private val handler: Handler = Handler()

    //周期时间
    private val Time = 1000 * 60 * 5L

    //设备ID
    private var deviceID = ""


    private val runnable: Runnable = object : Runnable {
        override fun run() {
            uploadStatus(deviceID)
            handler.postDelayed(this, Time)
        }
    }

    private fun uploadStatus(deviceID: String) {
        RetrofitUtil.instance.getDefautlRetrofit()
            .uploadDeviceStatus(HttpUtils.createBody("{\"deviceId\":\"$deviceID\"}"))
            .subscribeOn(Schedulers.io())
            .subscribe(CustomObserver<DKResponse>().apply {
                _onNext {
                    if (!it.isSuccess()) {
                        AppUtil.relaunch()
                    }
                }
                _onError {
                    AppUtil.relaunch()
                }
            })
    }

    override fun onCreate() {
        super.onCreate()
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, Time)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            deviceID = it.getStringExtra(DEVICE_ID_KEY)
            uploadStatus(deviceID)
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, Time)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

}
