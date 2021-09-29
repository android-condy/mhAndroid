package com.idic.widgetmoudle.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup


/**
 * 文 件 名: BaseAdapter
 * 创 建 人: sineom
 * 创建日期: 2018/11/19 16:57
 * 修改时间：
 * 修改备注：
 */

abstract class BaseAdapter<T>(
    var layoutId: Int,
    var variableId: Int,
    var adapterDatas: List<T>
) : RecyclerView.Adapter<ItemViewHolder>() {

    abstract fun convert(holder: ItemViewHolder, position: Int, t: T)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return adapterDatas.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        convert(holder, position, adapterDatas[position])
    }

    open fun    setData(datas: List<T>) {
        if (adapterDatas.isEmpty()) {
            adapterDatas = ArrayList()
            (adapterDatas as ArrayList).addAll(datas)
            notifyItemRangeInserted(0, datas.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return areItemsTheSame(oldItemPosition, newItemPosition, datas)
                }

                override fun getOldListSize(): Int {
                    return adapterDatas.size
                }

                override fun getNewListSize(): Int {
                    return datas.size
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return areContentsTheSame(oldItemPosition, newItemPosition, datas)
                }

            })
            adapterDatas = datas
            result.dispatchUpdatesTo(this)
        }
    }

    //判断item是否相同
    open fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
        newData: List<T>
    ): Boolean {
        return false
    }

    //内容是否相同
    open fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
        newData: List<T>
    ): Boolean {
        return false
    }

    interface OtherBinding {
        fun binding(holder: ItemViewHolder, position: Int)
    }

}