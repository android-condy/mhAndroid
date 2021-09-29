package com.idic.backstagemoudle.ui

import android.os.Bundle
import com.alibaba.fastjson.JSON
import com.allenliu.versionchecklib.callback.APKDownloadListener
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder
import com.allenliu.versionchecklib.v2.builder.UIData
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener
import com.blankj.utilcode.util.AppUtils
import com.idic.backstagemoudle.R
import com.idic.basecommon.BaseFullActivity
import com.idic.utilmoudle.DKToastUtils
import com.pgyersdk.update.PgyUpdateManager
import kotlinx.android.synthetic.main.activity_check_update.*
import java.io.File

class CheckUpdateActivity : BaseFullActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_update)
        checkNewVersion()
    }

    override fun onDestroy() {
        super.onDestroy()
        AllenVersionChecker.getInstance().cancelAllMission()
    }

    private fun checkNewVersion() {
        AllenVersionChecker
            .getInstance()
            .requestVersion()
            .setRequestUrl("http://api.kjndj.com/api/mh/update?type=new")
            .request(object : RequestVersionListener {
                override fun onRequestVersionSuccess(
                    downloadBuilder: DownloadBuilder?,
                    result: String?
                ): UIData? {
                    result?.let {
                        val parseObject = JSON.parseObject(it)
                        if (parseObject.getIntValue("version") > AppUtils.getAppVersionCode()) {
                            return UIData.create().setDownloadUrl(parseObject.getString("url"))
                        } else {
                            DKToastUtils.showCustomShort("当前已是最新版本")
                            finish()
                        }

                    }
                    return null
                }

                override fun onRequestVersionFailure(message: String?) {
                }

            })
            .setDirectDownload(true)
            .setShowNotification(false)
            .setShowDownloadingDialog(false)
            .setShowDownloadFailDialog(false)
            .setRunOnForegroundService(true)
            .setApkDownloadListener(object : APKDownloadListener {
                override fun onDownloading(progress: Int) {
                    update_progress.progress = progress
                }

                override fun onDownloadFail() {
                    DKToastUtils.showCustomShort("更新失败")
                    finish()
                }

                override fun onDownloadSuccess(file: File?) {
                    PgyUpdateManager.installApk(file)
                }

            })
            .executeMission(this)
    }

}
