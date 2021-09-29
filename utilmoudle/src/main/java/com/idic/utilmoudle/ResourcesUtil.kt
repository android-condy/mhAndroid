package com.idic.utilmoudle

import android.graphics.drawable.Drawable
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.blankj.utilcode.util.Utils

/**
 * 文 件 名: ResourcesUtil
 * 创 建 人: sineom
 * 创建日期: 2019/2/18 15:44
 * 修改时间：
 * 修改备注：
 */

object ResourcesUtil {

    fun getString(@StringRes id: Int): String {
        return Utils.getApp().getString(id)
    }

    fun getString(@StringRes id: Int, vararg str: Any): String {
        return Utils.getApp().getString(id, str)
    }

    fun getDimen(@DimenRes id: Int): Float {
        return Utils.getApp().resources.getDimension(id)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable {
        return Utils.getApp().resources.getDrawable(id)
    }
}