package com.idic.basecommon

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * 文 件 名: ViewModelFactory
 * 创 建 人: sineom
 * 创建日期: 2019/2/18 11:21
 * 修改时间：
 * 修改备注：
 */

abstract class BaseModelFactory(
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val model =
            getModel(modelClass) ?: throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        return model
    }

    abstract fun <T : ViewModel> getModel(modelClass: Class<T>): T?
}
