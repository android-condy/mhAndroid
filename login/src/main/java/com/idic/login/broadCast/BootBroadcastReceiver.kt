package com.idic.login.broadCast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.elvishew.xlog.XLog
import com.idic.login.BuildConfig
import com.idic.login.ui.activity.LoginActivity
import com.idic.login.ui.activity.SplashActivity

/**
 * 开机自启app
 */
class BootBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        XLog.d("reboot ----->$action")
        if (action == ACTION) {
//            if (BuildConfig.DEBUG) {
//                ActivityUtils.startActivity(LoginActivity::class.java)
//            } else {
            ActivityUtils.startActivity(SplashActivity::class.java)
//            }
        }
    }

    companion object {
        internal val ACTION = "android.intent.action.BOOT_COMPLETED"
    }

}
