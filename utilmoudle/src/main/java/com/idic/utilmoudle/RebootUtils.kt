//package com.idic.utilmoudle
//
//import android.annotation.SuppressLint
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.os.SystemClock
//import android.text.TextUtils
//import android.util.Log
//import com.blankj.utilcode.util.ShellUtils
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * 文 件 名: RebootUtils
// * 创 建 人: sineom
// * 创建日期: 2019-07-16 09:43
// * 修改时间：
// * 修改备注：
// */
//
//private const val ACTION_THREE_CLOCK_REBOOT = "ACTION_THREE_CLOCK_REBOOT"
//private const val ACTION_TIME_SET = "android.intent.action.TIME_SET"
//
//class RebootUtils {
//
//    companion object {
//        fun getInstance() = RebootInner.instance
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    fun start3Clock(context: Context) {
//        val mg = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val mFifteenIntent = Intent(ACTION_THREE_CLOCK_REBOOT)
//        val p = PendingIntent.getBroadcast(
//            context,
//            0, mFifteenIntent, PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        val systemTime = System.currentTimeMillis()
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = System.currentTimeMillis()
//        //时间
//        calendar.set(Calendar.HOUR_OF_DAY, 4)
//        calendar.set(Calendar.MINUTE, 30)
//        calendar.set(Calendar.SECOND, 0)
//        calendar.set(Calendar.MILLISECOND, 0)
//        val selectTime = calendar.timeInMillis
//        /**如果超过今天的3点，那么定时器就设置为明天3点 */
//        if (systemTime > selectTime) {
//            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1)
//        }
//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        val selectStr = sdf.format(Date(calendar.timeInMillis))
//        Log.d("clock", "selectStr 3 clock : $selectStr")
//        val clockTime = SystemClock.elapsedRealtime()
//        val delayTime = calendar.timeInMillis - systemTime
//        Log.d("clock", "selectStr 3 clock : $delayTime")
//        mg.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, p)
////        /**RTC_SHUTDOWN_WAKEUP 使用标识，系统进入深度休眠还唤醒 */
//        //周期性的发送
////        mg.setRepeating(
////            AlarmManager.RTC_WAKEUP,
////            delayTime,
////            AlarmManager.INTERVAL_DAY,
////            p
////        )
//    }
//
//    private var mReceiver: BroadcastReceiver? = null
//
//    /**广播初始化 */
//    private fun initReceiver(): IntentFilter {
//        mReceiver = RebootBroadcast()
//        val filter = IntentFilter()
//        filter.addAction(ACTION_THREE_CLOCK_REBOOT)
//        filter.addAction(ACTION_TIME_SET)
//        return filter
//    }
//
//
//    fun registerReboot(context: Context) {
//        val initReceiver = initReceiver()
//        context.registerReceiver(mReceiver, initReceiver)
//    }
//
//    fun unregisterReboot(context: Context) {
//        mReceiver?.let {
//            context.unregisterReceiver(it)
//        }
//
//    }
//
//
//    private inner class RebootBroadcast : BroadcastReceiver() {
//
//        override fun onReceive(context: Context, intent: Intent) {
//            val action = intent.action
//            if (TextUtils.equals(action, ACTION_THREE_CLOCK_REBOOT)) {
//                ShellUtils.execCmd("reboot", true)
//            } else if (TextUtils.equals(action, ACTION_TIME_SET)) {
//                start3Clock(context)
//            }
//        }
//
//    }
//
//    private object RebootInner {
//        val instance = RebootUtils()
//    }
//}