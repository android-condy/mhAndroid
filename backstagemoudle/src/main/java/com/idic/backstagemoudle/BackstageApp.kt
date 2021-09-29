package com.idic.backstagemoudle

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.boxtest.ShipmentManager
import com.idic.backstagemoudle.db.DeviceConfigDataBase
import com.idic.backstagemoudle.service.OpenLockService
import com.idic.basecommon.AppExecutors
import com.idic.basecommon.BaseApp
import com.idic.basecommon.service.OssUploadService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.allenliu.versionchecklib.core.AllenChecker.isDebug

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


/**
 * 文 件 名: App
 * 创 建 人: sineom
 * 创建日期: 2019-08-22 11:48
 * 修改时间：
 * 修改备注：
 */

class BackstageApp : BaseApp() {

    var mAppExecutors: AppExecutors? = null
        private set

    var dataBase: DeviceConfigDataBase? = null
        private set

    private val heartbeatHandler = Handler()

    override fun initModuleApp(application: Application) {
        mInstance = this
        mAppExecutors = AppExecutors()
    }

    override fun initModuleData(application: Application) {
        GlobalScope.launch {
            heartbeatHandler.postDelayed(heartbeat, 1000L)
            Log.i("condy", "heartbeatHandler  heartbeat   BackstageApp")
            XLog.i("heartbeatHandler")

        }

    }

    companion object {
        var mInstance: BackstageApp? = null
            private set

        fun getDeviceConfig() =
            DeviceConfigDataBase.getInstance().deviceConfigDao().getAllConfig().firstOrNull()
    }

    private val heartbeat = object : Runnable {
        override fun run() {
            ShipmentManager.sendHeartbeat()
            heartbeatHandler.postDelayed(this, 7 * 60 * 1000)
        }
    }



}
