package com.idic.login

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.idic.basecommon.BaseApp
import com.idic.login.crash.CrashHandler
import com.idic.login.db.DBHelper
import com.idic.login.db.LoginDatabase

/**
 * 文 件 名: LoginApp
 * 创 建 人: sineom
 * 创建日期: 2019-08-22 09:17
 * 修改时间：
 * 修改备注：
 */

class LoginApp : BaseApp() {

    lateinit var dataBase: LoginDatabase
        private set

    companion object {

        var mInstance: LoginApp? = null
    }


    override fun initModuleApp(application: Application) {
        mInstance = this
        dataBase = LoginDatabase.getInstance(application)
        JPushInterface.setDebugMode(BuildConfig.DEBUG)
        JPushInterface.init(application)
        //ObjectBox数据库
//        DBHelper.initDB(application)
    }

    override fun initModuleData(application: Application) {
        if (!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler())
        }
    }
}