package com.idic.login.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.NetworkUtils
import com.idic.basecommon.BaseFullActivity
import com.idic.login.R
import com.idic.utilmoudle.AppUtil
import kotlinx.android.synthetic.main.activity_net_error.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NetErrorActivity : BaseFullActivity() {

    private var isConnect = false

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_error)
//        job = GlobalScope.launch {
//            while (!isConnect) {
//                isConnect = NetworkUtils.isAvailableByPing("www.baidu.com")
//                if (!isConnect) {
//                    delay(10 * 1000)
//                }
//            }
//            AppUtil.relaunch()
////            AppUtil.reShunDown()
//        }
        Handler().postDelayed(Runnable() {

            var LaunchIntent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
            LaunchIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(LaunchIntent);
            finish()

        }, 10000);// 10秒钟后重启应用
        ivNetError.setOnClickListener {
            job?.cancel()
            finish()
            val intent = Intent(Settings.ACTION_SETTINGS)
            startActivity(intent)
        }
    }

//    private fun restartApp() {
//        val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
//        //与正常页面跳转一样可传递序列化数据,在Launch页面内获得
//        intent?.putExtra("REBOOT", "reboot")
//        val restartIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)
//        val mgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent)
//        Process.killProcess(Process.myPid())
//    }


    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        isConnect = true
    }
}
