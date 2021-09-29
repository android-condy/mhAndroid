package com.idic.widgetmoudle

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup


/**
 * 文 件 名: MultiRowRadioGroup
 * 创 建 人: sineom
 * 创建日期: 2019-08-23 14:17
 * 修改时间：
 * 修改备注：
 */

class MultiRowRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RadioGroup(context, attrs) {

    var mOnCheckedChangeListener: OnCheckedChangeListener? = null


    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        if (child is ViewGroup) {
            val childCount = child.childCount
            for (i in 0 until childCount) {
                val view = child.getChildAt(i)
                if (view is RadioButton) {
                    view.setOnTouchListener { v, event ->
                        when (event.action) {
                            MotionEvent.ACTION_UP -> {
                                if (view.isChecked) return@setOnTouchListener false
                                view.isChecked = true
                                checkRadioButton(view)
                                if (mOnCheckedChangeListener != null) {
                                    mOnCheckedChangeListener?.onCheckedChanged(this, view.id)
                                }
                            }
                        }
                        false
                    }
                }
            }
        }

    }

    private fun checkRadioButton(radioButton: RadioButton) {
        var child: View
        val radioCount = childCount
        for (i in 0 until radioCount) {
            child = getChildAt(i)
            if (child is RadioButton) {
                if (child === radioButton) {
                    // do nothing
                } else {
                    child.isChecked = false
                }
            } else if (child is ViewGroup) {
                val childCount = child.childCount
                for (j in 0 until childCount) {
                    val view = child.getChildAt(j)
                    if (view is RadioButton) {
                        if (view === radioButton) {
                            // do nothing
                        } else {
                            view.isChecked = false
                        }
                    }
                }
            }
        }
    }
}
