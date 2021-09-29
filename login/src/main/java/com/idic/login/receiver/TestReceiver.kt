package com.idic.login.receiver

import android.content.Context
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.elvishew.xlog.XLog

/**
 * 文 件 名: TestReceiver
 * 创 建 人: sineom
 * 创建日期: 2020/6/2 15:30
 * 修改时间：
 * 修改备注：
 */

class TestReceiver : JPushMessageReceiver() {
    override fun onCheckTagOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onCheckTagOperatorResult(p0, p1)
    }

    override fun onTagOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onTagOperatorResult(p0, p1)
    }

    override fun onMobileNumberOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onMobileNumberOperatorResult(p0, p1)
    }

    override fun onAliasOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onAliasOperatorResult(p0, p1)
        XLog.d("setAlias---->$p1")
    }
}