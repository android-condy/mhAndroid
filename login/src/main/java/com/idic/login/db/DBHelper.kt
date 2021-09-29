package com.idic.login.db

import android.app.Application
import com.idic.login.BuildConfig
import com.idic.login.db.entity.DeviceInfo
import com.idic.login.db.entity.MyObjectBox
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

/**
 * 文 件 名: DBHelper
 * 创 建 人: sineom
 * 创建日期: 2019/1/31 15:26
 * 修改时间：
 * 修改备注：
 */

class DBHelper {

    fun getDeviceBox(): Box<DeviceInfo> {
        return boxStore!!.boxFor(DeviceInfo::class.java)
    }


    fun clearAllData() {
        getDeviceBox().removeAll()
    }


    companion object {

        var boxStore: BoxStore? = null
            private set

        fun initDB(application: Application) {
            if (boxStore == null) {
                boxStore = MyObjectBox.builder().androidContext(application).build()
            }
            if (BuildConfig.DEBUG) {
                AndroidObjectBrowser(boxStore).start(application)
            }
        }

        val instance: DBHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DBHelper()
        }
    }
}