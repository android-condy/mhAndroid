package com.idic.widgetmoudle.adapter

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 文 件 名: SpaceItemDecoration
 * 创 建 人: sineom
 * 创建日期: 2018/11/19 17:48
 * 修改时间：
 * 修改备注：
 */

class SpaceItemDecoration(
    private var left: Int,
    private var top: Int,
    private var right: Int,
    private var bottom: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = left
        outRect.top = top
        outRect.right = right
        outRect.bottom = bottom
    }

}
