package com.idic.ordermoudle

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.idic.basecommon.utils.CustomObserver
import com.idic.httpmoudle.response.DKResponse
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.httpmoudle.utils.RxHolder
import com.idic.ordermoudle.response.CardDetails
import com.idic.utilmoudle.DKToastUtils
import kotlinx.android.synthetic.main.activity_check_vip.*

class CheckVipActivity : AppCompatActivity() {

    private val timerHandler = TimerHandler()

    private val EXIT = 0x01

    private val delayTime = 30000L

    private var onKeyListener = View.OnKeyListener { v, keyCode, event ->

        if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.action) {
            if (spin_kit.visibility == View.VISIBLE) {
                return@OnKeyListener false
            }
            v as EditText
            val cardCode = v.text.toString().trim()
            v.setText("")
            XLog.i("Info", "读卡数据：$cardCode")
            checkCardId(cardCode.trim())
        }
        return@OnKeyListener false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_vip)
        etReceiverCard?.setOnKeyListener(onKeyListener)
        timerHandler.removeMessages(EXIT)
        timerHandler.sendEmptyMessageDelayed(EXIT, delayTime)
    }

    override fun onStop() {
        super.onStop()
        timerHandler.removeMessages(EXIT)
    }

    @SuppressLint("SetTextI18n", "CheckResult", "StringFormatMatches")
    private fun checkCardId(cardCode: String) {
        spin_kit.visibility = View.VISIBLE
        RetrofitUtil.instance.getDefautlRetrofit()
            .queryCard(cardCode)
            .filter { dkResponse ->
                val data = dkResponse.data
                val success = dkResponse.isSuccess() && data != null
                if (!success) {
                    data?.let {
                        try {
                            val gson = Gson()
                            val fromJson =
                                gson.fromJson(gson.toJson(it), DKResponse.ErrorData::class.java)
                            DKToastUtils.showCustomShort(fromJson.msg)
                            return@filter success
                        } catch (e: Exception) {
                        }
                    }
                    DKToastUtils.showCustomShort("会员不存在,请联系管理员")
                }
                success
            }.compose(RxHolder.getResponseData<CardDetails>())
            .compose(RxHolder.IO2Main())
            .subscribe(CustomObserver<CardDetails>().apply {
                _onNext {
                    spin_kit.visibility = View.INVISIBLE
                    infoGroup.visibility = View.INVISIBLE
                    tvCardNumber.text = getString(R.string.cardNumber, it.cardNumber)
                    tvAccountBalance.text =
                        getString(R.string.accountBalance, "${it.accountBalance}")
                    cardInfoGroup.visibility = View.VISIBLE
                }
                _onError {
                    DKToastUtils.showCustomShort("会员不存在,请联系管理员")
                    spin_kit.visibility = View.INVISIBLE
                }
                _onComplete {
                    timerHandler.removeMessages(EXIT)
                    timerHandler.sendEmptyMessageDelayed(EXIT, 5000)
                }
            })
    }

    @SuppressLint("HandlerLeak")
    private inner class TimerHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            finish()
        }
    }
}
