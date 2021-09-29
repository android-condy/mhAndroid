package com.idic.login.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import com.idic.basecommon.SPKeys
import com.idic.httpmoudle.exception.UserInfoError
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.login.LoginApp
import com.idic.login.api.request.LoginRequest
import com.idic.login.api.response.LoginResponse
import com.idic.login.db.entity.LoginInfo
import com.idic.login.model.DeviceInfo
import com.idic.login.service.DEVICE_ID_KEY
import com.idic.login.service.GuardService
import com.idic.login.service.UploadStatusService
import com.idic.utilmoudle.DKToastUtils
import io.reactivex.schedulers.Schedulers


/**
 * 文 件 名: LoginImp
 * 创 建 人: sineom
 * 创建日期: 2019-07-23 15:56
 * 修改时间：
 * 修改备注：
 */

internal class LoginImp : ILogin {

    @SuppressLint("CheckResult")
    override fun login(userName: String, password: String, key: String, loginResult: ILogin.Login) {
        val loginRequest = LoginRequest(userName, password, key)
        val deviceInfo = DeviceInfo().apply {
            device_name = userName
            this.password = password
            device_key = key
        }
        RetrofitUtil.instance.getDefautlRetrofit()
            .login(HttpUtils.createBody(loginRequest))
//            .checkActive(key)
            .subscribeOn(Schedulers.io())
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    DKToastUtils.showLong("登录信息错误，请检查输入是否正确")
                    loginResult.loginFail(UserInfoError())
                }
                success
            }.map {
                val gson = Gson()
                gson.fromJson(gson.toJson(it.data!!), LoginResponse::class.java)
            }.map {
                with(it) {
                    SPUtils.getInstance().put(SPKeys.DEVICES_KEY, key, true)
                    SPUtils.getInstance().put(SPKeys.MERCHANT_ID, merchantId, true)
                    SPUtils.getInstance().put(SPKeys.DEVICES_ID, deviceId, true)
                    SPUtils.getInstance().put(SPKeys.MACHINE_NUMBER, number, true)
                    deviceInfo.deviceId = "$deviceId"
                    deviceInfo.merchantId = "$merchantId"
                    deviceInfo.number = "$number"
                    deviceInfo.accesstoken = "token"
                }
                deviceInfo
            }
            .subscribe({
                val loginDao = LoginApp.mInstance!!.dataBase.loginDao()
                val infos = loginDao.getInfos()
                if (infos.isEmpty()) {
                    loginDao.insert(LoginInfo(userName, password, key))
                } else {
                    val first = infos.first()
                    first.copy(userName = userName, password = password, activationCode = key)
                    loginDao.update(first)
                }

                //启动上报状态的服务
                val intent = Intent(Utils.getApp(), UploadStatusService::class.java)
                intent.putExtra(DEVICE_ID_KEY, deviceInfo.deviceId)
                Utils.getApp().startService(intent)
                //启动实时监测进程
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Utils.getApp().startService(Intent(Utils.getApp(), GuardService::class.java))
                }
                //注册极光推送的别名
                JPushInterface.setAlias(
                    Utils.getApp(),
                    0,
                    deviceInfo.deviceId
                )
//                ARouter.getInstance().navigation(SingleSmileServiceImpl::class.java).init()
                loginResult.loginSuccess(deviceInfo)
            }, {
                Log.i("http","登录发送错误", it)
                DKToastUtils.showCustomShort("与服务器通信产生异常")
                loginResult.loginFail(it)
            })
    }


}




