package com.idic.backstagemoudle.ui

import android.view.View
import android.widget.RadioGroup
import com.idic.backstagemoudle.db.entity.AisleConfigEntity

/**
 * 文 件 名: AisleClickCallback
 * 创 建 人: sineom
 * 创建日期: 2019-08-22 14:52
 * 修改时间：
 * 修改备注：
 */

interface AisleClickCallback {
    fun onClick(view: View, aisleEntity: AisleConfigEntity)

    fun onSplitTypeChanged(radioGroup: RadioGroup, id: Int, aisleEntity: AisleConfigEntity)
}