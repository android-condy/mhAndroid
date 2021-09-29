package com.idic

import android.app.Application
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.idic.basecommon.BaseApp
import com.idic.basecommon.service.SetUrlService
import com.idic.blindbox.service.LightService
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * 文 件 名: App
 * 创 建 人: sineom
 * 创建日期: 2019-08-08 14:01
 * 修改时间：
 * 修改备注：
 */

class App : BaseApp() {

    override fun initModuleApp(application: Application) {
    }

    override fun initModuleData(application: Application) {
    }


    override fun onCreate() {
        super.onCreate()
        initLog()
        val context = applicationContext
// 获取当前包名
        val packageName = context.packageName
// 获取当前进程名
//        val processName = getProcessName(android.os.Process.myPid())
// 设置是否为上报进程
//        val strategy = CrashReport.UserStrategy(context)
//        strategy.isUploadProcess = processName == null || processName == packageName
// 初始化Bugly
        Log.i("condy", "heartbeatHandler1")
//        CrashReport.initCrashReport(context, "8dc5a19be0", false, strategy)
        Log.i("condy", "heartbeatHandler2")
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
        }
        ARouter.init(this)
//        val setUrlService = ARouter.getInstance().navigation(SetUrlService::class.java)
//        setUrlService.setApiUrl(BuildConfig.Base_url, BuildConfig.Base_port)
//        setUrlService.setImageUrl(BuildConfig.Base_Img_url, BuildConfig.Base_imgport)
        startService(Intent(this, LightService::class.java))
        initMoudle()
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader!!.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim({ it <= ' ' })
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader!!.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }

}