package com.idic.backstagemoudle.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.idic.backstagemoudle.db.DeviceConfigDataBase
import com.idic.backstagemoudle.db.entity.DeviceConfigEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 文 件 名: DeviceConfigModel
 * 创 建 人: sineom
 * 创建日期: 2019-08-27 17:06
 * 修改时间：
 * 修改备注：
 */

class DeviceConfigModel : ViewModel() {

    val device = MutableLiveData<DeviceConfigEntity>()

    init {
        GlobalScope.launch {
            val deviceConfigEntity =
                DeviceConfigDataBase.getInstance().deviceConfigDao().getAllConfig().firstOrNull()
            device.postValue(deviceConfigEntity)
        }

    }
}