package com.idic.widgetmoudle

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout


/**
 * 文 件 名: BottomSheetMenu
 * 创 建 人: sineom
 * 创建日期: 2019-08-19 10:31
 * 修改时间：
 * 修改备注：
 */

open class BaseBottomSheetMenu : BottomSheetDialogFragment() {

    private var behavior: BottomSheetBehavior<View>? = null

    private var height = 1920

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (context == null) {
            super.onCreateDialog(savedInstanceState)
        } else
            BottomSheetDialog(context!!, R.style.TransparentBottomSheetStyle)
    }

    override fun onResume() {
        super.onResume()
        val lp = dialog.window!!.attributes
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        val bottomSheet = dialog.delegate.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            bottomSheet.setBackgroundColor(Color.TRANSPARENT)
            val layoutParams = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = getHeight()
            behavior = BottomSheetBehavior.from(bottomSheet)
            // 初始为展开状态
            behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


    fun getHeight(): Int {
        return height
    }

    fun setHeight(height: Int) {
        this.height = height
    }

    fun getBehavior(): BottomSheetBehavior<View>? {
        return behavior
    }

    fun close() {
        dismiss()
    }
}
