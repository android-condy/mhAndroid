package com.idic.backstagemoudle.ui

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.system.Os
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.idic.backstagemoudle.BuildConfig
import com.idic.backstagemoudle.R
import com.idic.backstagemoudle.util.DialogUtil
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.SPKeys
import com.idic.basecommon.utils.ARouterConfig
import com.idic.datamoudle.db.entity.NewBannerEntity
import com.idic.httpmoudle.URL
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.httpmoudle.utils.RxHolder
import com.idic.utilmoudle.DKToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login_backstage.*
import org.json.JSONObject

@Route(path = ARouterConfig.BACKSTAGE_HOME)
class LoginBackstageActivity : BaseFullActivity() {

    private val timerExit = TimerExit()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(true)
        setContentView(R.layout.activity_login_backstage)
        initView()
        timerExit.start()
    }

    @SuppressLint("HardwareIds")
    private fun initView() {
        dialog_btnLogin.setOnClickListener {
            //            if (BuildConfig.DEBUG) {
//                ActivityUtils.startActivity(BackStageHomeActivity::class.java)
//                finish()
//                return@setOnClickListener
//            }
            var name = dialog_tieName.text.toString()
            var password = dialog_tiePW.text.toString()
            if (StringUtils.isEmpty(name)) {
                DKToastUtils.showCustomShort(getString(R.string.etUserName))
                return@setOnClickListener
            }
            if (StringUtils.isEmpty(password)) {
                DKToastUtils.showCustomShort(getString(R.string.etPassword))
                return@setOnClickListener
            }
//            var jsonObject = JsonObject()
//            jsonObject.addProperty("login_name", name)
//            jsonObject.addProperty("login_pwd", password)
//            jsonObject.addProperty("device_id", URL.device_id)
            RetrofitUtil.instance.getDefautlRetrofit()
                .getBackStageLogin(login_name = name, login_pwd = password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy({
                    DKToastUtils.showCustomShort("无法连接上服务器")
                }, {

                }, {
                    if (it.isSuccess()) {
//                    Log.i("condy","解析结果="+ Gson().toJsonTree(it.data).asJsonObject.get("user_id").asInt.toString())
                        var jsonObject = JSON.parseObject(JSON.toJSONString(it.data))
                        val user_id = jsonObject.getIntValue("user_id")
                        DialogUtil.saveUserId(this@LoginBackstageActivity, user_id)
                        ActivityUtils.startActivity(BackStageHomeActivity::class.java)
                        finish()
                    } else {
                        DKToastUtils.showCustomShort(it.msg)
                    }
                })
//            if (name != DeviceInfo.getInstance().userName || password != DeviceInfo.getInstance().password) {
//                DKToastUtils.showCustomShort(getString(R.string.dialog_login_fail))
//                return@setOnClickListener
//            }
//            ActivityUtils.startActivity(BackStageHomeActivity::class.java)
//            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        timerExit.cancel()
        finish()
    }

    inner class TimerExit : CountDownTimer(60000, 1000) {
        override fun onFinish() {
            finish()
        }

        override fun onTick(millisUntilFinished: Long) {

        }
    }
}
