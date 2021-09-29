package com.idic.login.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSON
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder
import com.allenliu.versionchecklib.v2.builder.UIData
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.SPKeys
import com.idic.basecommon.utils.ARouterConfig
import com.idic.basecommon.utils.FilePath
import com.idic.datamoudle.db.SourceDataBase
import com.idic.httpmoudle.exception.UserInfoError
import com.idic.login.Config
import com.idic.login.LoginApp
import com.idic.login.R
import com.idic.login.db.dao.DeviceHelper
import com.idic.login.db.entity.LoginInfo
import com.idic.login.model.DeviceInfo
import com.idic.login.service.LoginSuccessService
import com.idic.login.ui.ILogin
import com.idic.login.ui.LoginImp
import kotlinx.android.synthetic.main.activity_check_info.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class CheckInfoActivity : BaseFullActivity(), ILogin.Login {

    private val loginImpl = LoginImp()
    private var infos: List<LoginInfo>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_check_info)
        video?.let {
            it.setUrl(FilePath.bgVideo)
            it.setLooping(true)
        }
//        GlobalScope.launch {
//            infos = LoginApp.mInstance!!.dataBase.loginDao().getInfos()
//            val key = SPUtils.getInstance().getString(SPKeys.DEVICES_KEY)
//            var loginInfo: LoginInfo? = null
//            if (!infos.isNullOrEmpty()) {
//                loginInfo = infos!!.first()
//            }
//            delay(1000)
//            if (loginInfo == null || key.isNullOrEmpty()) {
//                toLogin()
//            } else {
//                loginImpl.login(
//                    loginInfo.userName,
//                    loginInfo.password,
//                    key,
//                    this@CheckInfoActivity
//                )
//            }
//        }

    }

    override fun onResume() {
        super.onResume()
        video?.start()
    }

    override fun onStop() {
        super.onStop()
        video?.release()
    }

    private fun toLogin() {
        GlobalScope.launch {
            delay(1500)
            val allProducts = SourceDataBase.getInstance(application).productDao().getAllProducts()
            if (allProducts.isNullOrEmpty()) {
                ActivityUtils.startActivity(LoginActivity::class.java)
            } else {
                ARouter.getInstance().navigation(LoginSuccessService::class.java).loginSuccess(null)
            }
            finish()
        }
    }

    override fun loginSuccess(deviceInfo: DeviceInfo) {
        ARouter.getInstance().navigation(LoginSuccessService::class.java).loginSuccess(deviceInfo)
        finish()
    }

    override fun loginFail(throwable: Throwable) {
        with(throwable.javaClass) {
            when {
                isAssignableFrom(UserInfoError::class.java) -> {
                    if (infos.isNullOrEmpty()) {
                        toLogin()
                    } else {
                        val first = infos!!.first()
                        loginImpl.login(
                            first.userName,
                            first.password,
                            first.activationCode,
                            this@CheckInfoActivity
                        )
                    }
                }
                else -> {
                    toLogin()
                }
            }
        }

    }
}
