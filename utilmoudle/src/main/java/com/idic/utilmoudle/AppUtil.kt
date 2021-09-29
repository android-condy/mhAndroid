package com.idic.utilmoudle

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ShellUtils
import java.io.DataOutputStream

/**
 * 文 件 名: AppUtil
 * 创 建 人: sineom
 * 创建日期: 2020/9/24 10:45
 * 修改时间：
 * 修改备注：
 */

object AppUtil {
    fun relaunch() {
        ShellUtils.execCmd(
            "am force-stop com.idic; adb shell am start -n com.idic/" + ActivityUtils.getLauncherActivity(),
            true
        )
    }
    fun reShunDown() {
        //重启手机
        try {
            Log.v("condy", "root Runtime->reboot")
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot "))
            proc.waitFor()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

//    @Synchronized
//    fun getRootAhth(): Boolean {
//        var process: Process? = null
//        var os: DataOutputStream? = null
//        try {
//            process = Runtime.getRuntime().exec("su")
//            os = DataOutputStream(process!!.outputStream)
//            os.writeBytes("exit\n")
//            os.flush()
//            val exitValue = process.waitFor()
//            return if (exitValue == 0) {
//                true
//            } else {
//                false
//            }
//        } catch (e: Exception) {
//            Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: " + e.message)
//            return false
//        } finally {
//            try {
//                os?.close()
//                process!!.destroy()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }
//    }

    /**
     * 获取当前app version code
     */
    fun getAppVersionCode(context: Context): Int {
        var appVersionCode: Int = 0
        try {
            val packageInfo = context.getApplicationContext()
                .getPackageManager()
                .getPackageInfo(context.getPackageName(), 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode().toInt()
            } else {
                appVersionCode = packageInfo.versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("", e.message)
        }

        return appVersionCode
    }


}