package com.idic.categorythome.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableInt

/**
 * 文 件 名: PayModel
 * 创 建 人: sineom
 * 创建日期: 2019-08-15 16:05
 * 修改时间：
 * 修改备注：
 */

class PaySuccessModel(
    application: Application
) : AndroidViewModel(application) {

    //等待出货的数量
    val waitShimentp = ObservableInt()
    //出货成功的数量
    val shimentpSuccess = ObservableInt(0)
    //出货失败的数量
    val shimentpFail = ObservableInt(0)
}