package com.idic.backstagemoudle.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.idic.backstagemoudle.db.entity.ContainerConfigEntity

/**
 * 文 件 名: ContainerConfigModel
 * 创 建 人: sineom
 * 创建日期: 2019-08-22 17:04
 * 修改时间：
 * 修改备注：
 */

class ContainerConfigModel : ViewModel() {

    val curSelectContainer = ObservableField<ContainerConfigEntity>(ContainerConfigEntity(1))

}