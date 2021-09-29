package com.idic.login.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import com.alibaba.fastjson.JSON
import com.allenliu.versionchecklib.callback.APKDownloadListener
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder
import com.allenliu.versionchecklib.v2.builder.UIData
import com.allenliu.versionchecklib.v2.callback.CustomInstallListener
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.UriUtils
import com.idic.basecommon.utils.FilePath
import java.io.File

/**
 * 文 件 名: HeartbeatService
 * 创 建 人: sineom
 * 创建日期: 2020/5/11 15:55
 * 修改时间：
 * 修改备注：
 */

class HeartbeatService : Service() {


    override fun onCreate() {
        super.onCreate()
        FileUtils.createOrExistsDir(FilePath.aliPayApk)

    }


    override fun onDestroy() {
        super.onDestroy()
        AllenVersionChecker.getInstance().cancelAllMission()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}