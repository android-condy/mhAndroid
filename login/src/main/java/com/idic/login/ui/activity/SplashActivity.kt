package com.idic.login.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.NetworkUtils
import com.idic.basecommon.utils.ARouterConfig
import com.idic.basecommon.utils.FilePath
import com.idic.login.R
import com.idic.utilmoudle.AppUtil
import com.idic.utilmoudle.DKToastUtils
import com.idic.utilmoudle.copyFilesFassets
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Thread {
            try {
                DKToastUtils.showCustomShort("加载资源中,请稍候....")

                copyFilesFassets("", FilePath.localVideoPath)
                val connected = NetworkUtils.isAvailable()
                if (!connected) {
                    val intent = Intent(this@SplashActivity, NetErrorActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    GlobalScope.launch {
                        delay(1500)
                        ARouter.getInstance().build(ARouterConfig.BLIND_BOX).navigation()
                        finish()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                DKToastUtils.showCustomShort("加载资源失败,正在重试")
                AppUtil.relaunch()
            }
        }.start()
        //APP心跳检测
//        Utils.getApp().startService(Intent(Utils.getApp(), HeartbeatService::class.java))
    }

}
