package com.idic.basecommon.adapter

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView

/**
 * 文 件 名: BindingAdapter
 * 创 建 人: sineom
 * 创建日期: 2019-08-22 16:59
 * 修改时间：
 * 修改备注：
 */

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun visibleGone(view: View, visible: Boolean) {
        view.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}