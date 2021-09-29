package com.idic.blindbox.widget

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.idic.blindbox.home.BindCategoryActivity
import kotlin.math.abs


/**
 * 文 件 名: FloatWindowImageView
 * 创 建 人: sineom
 * 创建日期: 2020/9/1 09:07
 * 修改时间：
 * 修改备注：
 */
class FloatWindowImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var mLastX = 0
    private var mLastY = 0

    private var isScroller = false

    private var downWidth = 0
    private var downHeight = 0


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        LogUtils.d("rawX-- down -->${event.rawX}")
        LogUtils.d("rawY-- down -->${event.rawY}")
        LogUtils.d("x-- down -->${event.x}")
        LogUtils.d("y-- down -->${event.y}")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                mLastX = x
                mLastY = y
                downWidth = (150 - x)
                downHeight = (200 - y)
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX: Int = x - mLastX
                val offsetY: Int = y - mLastY
                var l = left + offsetX
//                if (l > 930) l = 930
//                if (l < 0) l = 0
//
                var t = top + offsetY
//                if (t > 1720) t = 1720
//                if (t < 0) t = 0
//
                var r = right + offsetX
//                if (r < 0) r = 0
//                if (r > 930) r = 930
//
//
                var b = bottom + offsetY
//                if (b < 0) b = 0
//                if (b > 1720) b = 1720

                if (r > 1080) {
                    r = 1080
                    l = 930
                }

                if (l < 0) {
                    l = 0
                    r = 150
                }
                if (b > 1765) {
                    t = 1565
                    b = 1765
                }

                if (t < 0) {
                    t = 0
                    b = 200
                }

//                if (r > 930 + downWidth) {
//                    l = 930
//                    r = 1080
//                }
//                if (l < 0) {
//                    l = 0
//                    r = 150
//                }
//
//                if (b > 1720 + downHeight) {
//                    t = 1720
//                    b = 1920
//                }
//
//                if (t < 0) {
//                    t = 0
//                    b = 200
//                }

                layout(
                    l, t,
                    r, b
                )
                if (!isScroller) {
                    isScroller = (abs(mLastX - x) >= ViewConfiguration.get(
                        context
                    ).scaledTouchSlop) || abs(mLastY - y) >= ViewConfiguration.get(
                        context
                    ).scaledTouchSlop
                }
            }

            MotionEvent.ACTION_UP -> {
                if (!isScroller) {
                    ActivityUtils.startActivity(BindCategoryActivity::class.java)
                }
                isScroller = false
            }
        }
        return true
    }
}