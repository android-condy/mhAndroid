package com.idic.backstagemoudle.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.DownloadTask
import com.blankj.utilcode.util.AppUtils
import com.idic.backstagemoudle.BackStageArouter.DOWNLOAD_ALIPAY_INFO
import com.idic.backstagemoudle.R
import com.idic.backstagemoudle.alipay.request.AliPayConfigRequest
import com.idic.backstagemoudle.alipay.response.AliPayConfig
import com.idic.backstagemoudle.ui.DeviceConfigActivity.Companion.aliPayTag
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.utils.CustomObserver
import com.idic.basecommon.utils.FilePath.aliPayApk
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.httpmoudle.utils.RxHolder
import com.idic.utilmoudle.DKToastUtils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_load_ali_pay_info.*


@Route(path = DOWNLOAD_ALIPAY_INFO)
class LoadAliPayInfoActivity : BaseFullActivity() {

    private val alipayApkUrl =
//        "https://idictec-save.oss-cn-hangzhou.aliyuncs.com/AstraPro-3-3.7.0.201812062149.apk"
        "https://idictec-save.oss-cn-hangzhou.aliyuncs.com/smile-4.0.0.534.apk"

    private var aliPayConfig: AliPayConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_ali_pay_info)
        setFinishOnTouchOutside(false)
        Aria.download(this).register()
        initData()
        btnInstallted.setOnClickListener {
            intent.putExtra(aliPayTag, aliPayConfig)
            setResult(RESULT_OK, intent)
            finish()
        }
    }


    private fun initData() {

        RetrofitUtil.instance.getDefautlRetrofit()
            .getAliPayConfig(HttpUtils.createBody(AliPayConfigRequest()))
            .subscribeOn(Schedulers.io())
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    DKToastUtils.showCustomShort("请先在后台配置支付宝刷脸付相关信息后再开启该功能")
                    finish()
                }
                success
            }.compose(RxHolder.getResponseData<AliPayConfig>())
            .subscribe(CustomObserver<AliPayConfig>().apply {
                _onNext {
                    aliPayConfig = it
                    val appName = AppUtils.getAppName("com.alipay.zoloz.smile")
                    if (appName.isNullOrEmpty()) {
                        Aria.download(this)
                            .load(alipayApkUrl)     //读取下载地址
                            .setFilePath(aliPayApk) //设置文件保存的完整路径
                            .start();   //启动下载
                    } else {
                        installed()
                    }

                }
                _onError {
                    error()
                }
            })
    }

    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    fun running(task: DownloadTask) {
        if (task.key == alipayApkUrl) {
            val p = task.percent;    //任务进度百分比
            runOnUiThread {
                update_progress.progress = p
            }
        }

    }

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask) {
        installed()
        AppUtils.installApp(aliPayApk)
    }

    private fun installed() {
        runOnUiThread {
            update_progress.progress = 100
            spin_kit.visibility = View.GONE
            btnInstallted.visibility = View.VISIBLE
        }
    }

    @Download.onTaskFail
    fun taskFail(task: DownloadTask) {
        setResult(Activity.RESULT_CANCELED)
        error()
    }

    private fun error() {
        DKToastUtils.showCustomShort("加载配置失败")
        finish()
    }

}
