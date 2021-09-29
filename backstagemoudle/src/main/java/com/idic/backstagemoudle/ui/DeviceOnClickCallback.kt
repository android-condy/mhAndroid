package com.idic.backstagemoudle.ui

import android.view.View
import android.widget.AdapterView
import com.idic.backstagemoudle.db.entity.DeviceConfigEntity

/**
 * 文 件 名: DeviceOnClickCallback
 * 创 建 人: sineom
 * 创建日期: 2019-08-27 16:39
 * 修改时间：
 * 修改备注：
 */

interface DeviceOnClickCallback {


    fun afterTextChanged(charSequence: CharSequence, deviceConfigEntity: DeviceConfigEntity)

    fun onFocusChange(view: View, hasFocus: Boolean)

    fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long)
}