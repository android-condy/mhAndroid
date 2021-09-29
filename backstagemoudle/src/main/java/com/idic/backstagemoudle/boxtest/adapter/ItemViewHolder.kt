package com.idic.backstagemoudle.boxtest.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 文 件 名: ItemViewHolder
 * 创 建 人: sineom
 * 创建日期: 2018/11/19 16:57
 * 修改时间：
 * 修改备注：
 */

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var binding: ViewDataBinding? = DataBindingUtil.bind(view)

    fun setBinding(variableId: Int, any: Any): ItemViewHolder {
        binding?.let {
            it.setVariable(variableId, any)
            it.executePendingBindings()
        }
        return this
    }
}