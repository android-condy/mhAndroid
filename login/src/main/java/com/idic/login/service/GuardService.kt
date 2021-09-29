package com.idic.login.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import com.blankj.utilcode.util.AppUtils
import com.elvishew.xlog.XLog
import com.idic.utilmoudle.AppUtil


class GuardService : Service() {

    private val checkHandler = Handler()

    //轮询时间
    private val loopTime = 1000 * 60 * 5L

    private val runnable = object : Runnable {
        override fun run() {
            val am =
                getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val cn = am.getRunningTasks(1)[0].topActivity!!
            if (AppUtils.getAppPackageName() != cn.packageName) {
                AppUtil.relaunch()
            }
            checkHandler.postDelayed(this, loopTime)
        }

    }

    override fun onCreate() {
        super.onCreate()
        checkHandler.removeCallbacks(runnable)
        checkHandler.postDelayed(runnable, loopTime)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
