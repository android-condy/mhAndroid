package com.idic.login.ui.activity

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.alibaba.android.arouter.launcher.ARouter
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.utils.FilePath
import com.idic.login.BuildConfig
import com.idic.login.Config
import com.idic.login.R
import com.idic.login.databinding.ActivityLoginBinding
import com.idic.login.model.DeviceInfo
import com.idic.login.service.LoginSuccessService
import com.idic.login.ui.ILogin
import com.idic.login.ui.LoginImp
import com.idic.login.viewmodel.LoginModel
import com.idic.utilmoudle.DKToastUtils
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseFullActivity(), ILogin.Login {

    private lateinit var viewModel: LoginModel

    private val mUserNameWatch = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            tilName.error = if (text.contains(" ")) {
                getString(R.string.tilNameErrorInfo)
            } else {
                ""
            }
        }
    }
    private val mPWDWatch = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            tilPW.error = if (text.contains(" ")) {
                getString(R.string.tiiPwErrorInfo)
            } else {
                ""
            }
        }
    }
    private val mKeyWatch = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            tilKey.error = if (text.contains(" ")) {
                getString(R.string.tiiKeyErrorInfo)
            } else {
                ""
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginBinding = DataBindingUtil.setContentView<ActivityLoginBinding>(
            this,
            R.layout.activity_login
        )
        viewModel = ViewModelProviders.of(this).get(LoginModel::class.java)

        loginBinding.model = viewModel
        //隐藏软键盘
        root.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        tieName.addTextChangedListener(mUserNameWatch)
        tiePW.addTextChangedListener(mPWDWatch)
        tieKey.addTextChangedListener(mKeyWatch)
        video?.let {
            it.setUrl(FilePath.bgVideo)
            it.setLooping(true)
        }
//        video?.setVideoURI(Uri.parse(Config.BG_VIDEO_URI))
//        video?.setOnPreparedListener {
//            mediaPlayer = it
//            it.isLooping = true
//            it.start()
//        }
    }

    override fun onResume() {
        super.onResume()
        video?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        video?.release()
    }

    override fun loginSuccess(deviceInfo: DeviceInfo) {
        viewModel.dataLoading.set(false)
        ARouter.getInstance().navigation(LoginSuccessService::class.java).loginSuccess(deviceInfo)
        finish()
    }

    override fun loginFail(throwable: Throwable) {
        DKToastUtils.showShort(getString(R.string.loginFailInfo))
        viewModel.dataLoading.set(false)
    }


    @SuppressLint("CommitPrefEdits")
    fun login(view: View) {
        //获取输入的登录信息
        val name = viewModel.userName.get()
        val pwd = viewModel.password.get()
        val key = viewModel.key.get()
        viewModel.dataLoading.set(true)

        //Debug版
        if (BuildConfig.DEBUG) {
            if (name.isNullOrEmpty()) {
//                viewModel.userName.set("13801631401")
                viewModel.userName.set("slmh")
//                viewModel.userName.set("0513cssqbsh")
//                viewModel.userName.set("songcongcong")
//                viewModel.userName.set("ahmerchant1")
            }
            if (pwd.isNullOrEmpty()) {
                viewModel.password.set("sl888888")
            }
            if (key.isNullOrEmpty()) {
                viewModel.key.set("94a9851b1e0745fa")//0517
            }
        }

        //正式版
        if (name.isNullOrEmpty()) {
            DKToastUtils.showCustomShort(getString(R.string.nameNullInfo))
            viewModel.dataLoading.set(false)
            return
        }
        if (pwd.isNullOrEmpty()) {
            DKToastUtils.showCustomShort(getString(R.string.pwdNullInfo))
            viewModel.dataLoading.set(false)
            return
        }

        if (key.isNullOrEmpty()) {
            DKToastUtils.showCustomShort(getString(R.string.keyNullInfo))
            viewModel.dataLoading.set(false)
            return
        }

        LoginImp().login(name, pwd, key, this)
    }

}
