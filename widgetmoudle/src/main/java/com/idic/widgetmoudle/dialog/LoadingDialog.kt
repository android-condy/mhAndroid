package com.idic.widgetmoudle.dialog

import android.os.Bundle
import android.os.CountDownTimer
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.events.SyncProductResult
import com.idic.basecommon.utils.CustomObserver
import com.idic.widgetmoudle.R

/**
 * 文 件 名: LoadingDialog
 * 创 建 人: sineom
 * 创建日期: 2019-09-02 14:20
 * 修改时间：
 * 修改备注：
 */

class LoadingDialog : BaseFullActivity() {


    private val timerExit = TimerExit()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_dialog)
        setFinishOnTouchOutside(false)
        timerExit.start()
        listener()
    }

    private fun listener() {
        RxRelay.instance.toObserver(SyncProductResult::class.java)
            .subscribe(CustomObserver<SyncProductResult>().apply {
                _subscribe {
                    disposables.add(it)
                }
                _onNext {

                    finish()
                }
            })
    }

    override fun onStop() {
        super.onStop()
        timerExit.cancel()
    }


    private inner class TimerExit : CountDownTimer(30000, 1000) {
        override fun onFinish() {
            finish()
        }

        override fun onTick(millisUntilFinished: Long) {

        }
    }
}