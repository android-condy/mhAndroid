package com.idic.login.crash

import com.blankj.utilcode.util.AppUtils
import com.elvishew.xlog.XLog
import com.idic.utilmoudle.AppUtil

/**
 * 文 件 名: CrashHandler
 * 创 建 人: sineom
 * 创建日期: 2019/2/28 15:20
 * 修改时间：
 * 修改备注： 崩溃自动重启
 */

class CrashHandler() :
    Thread.UncaughtExceptionHandler {


    override fun uncaughtException(t: Thread, e: Throwable) {
        XLog.e("发生崩溃", e)
        AppUtil.relaunch()
    }
}
