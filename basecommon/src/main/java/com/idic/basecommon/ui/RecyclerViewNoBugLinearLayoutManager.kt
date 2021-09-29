package com.idic.basecommon.ui

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet



/**
 * 文 件 名: RecyclerViewNoBugLinearLayoutManager
 * 创 建 人: sineom
 * 创建日期: 2019-10-19 13:54
 * 修改时间：
 * 修改备注：
 */

class RecyclerViewNoBugLinearLayoutManager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
):LinearLayoutManager(context,attrs,defStyle,defStyleRes) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            //try catch一下
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }
}
