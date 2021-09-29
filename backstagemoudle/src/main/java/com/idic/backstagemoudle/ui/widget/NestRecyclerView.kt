package com.idic.backstagemoudle.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.R
import com.idic.utilmoudle.ResourcesUtil
import kotlin.math.abs


/**
 * 文 件 名: NestRecyclerView
 * 创 建 人: sineom
 * 创建日期: 2019-09-02 10:52
 * 修改时间：
 * 修改备注：
 */

class NestRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {


    private var mLastX = 0f// 记录上次Y位置
    private var mLastY = 0f// 记录上次Y位置

    private var childY = 0
    private var childX = 0

    override fun onFinishInflate() {
        super.onFinishInflate()
        childX = ResourcesUtil.getDimen(R.dimen.y20).toInt()
        childY = ResourcesUtil.getDimen(R.dimen.y80).toInt()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.x
                mLastY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val nowX = event.x
                val nowY = event.y
                if (mLastX == 0F) mLastX = nowX
                if (abs(nowY - mLastY) < childY || abs(nowX - mLastX) > childX) {
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    parent.requestDisallowInterceptTouchEvent(false)
                    return false
                }
                mLastX = nowX
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mLastX = 0F
                parent.requestDisallowInterceptTouchEvent(
                    false
                )
            }
        }
        return super.onTouchEvent(event)
    }

}
