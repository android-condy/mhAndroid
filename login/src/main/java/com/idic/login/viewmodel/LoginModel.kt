package com.idic.login.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField

class LoginModel(
    application: Application
) : AndroidViewModel(application) {

    val userName = ObservableField<String>()

    val password = ObservableField<String>()

    val dataLoading = ObservableField<Boolean>()

    //激活码
    val key = ObservableField<String>()

}