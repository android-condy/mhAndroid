package com.idic.backstagemoudle.boxtest.adapter

import android.support.v7.widget.RecyclerView
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
    var datas: ArrayList<T>
) : RecyclerView.Adapter<ItemViewHolder>() {

    abstract fun convert(holder: ItemViewHolder, position: Int, t: T)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        convert(holder, position, datas[position])
    }

    fun updateAllData(datas: List<T>) {
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun insertData(t: T) {
        datas.add(t)
        notifyItemInserted(datas.size - 1)
    }

    fun removeData(t: T) {
        if (!datas.contains(t))
            return
        val indexOf = datas.indexOf(t)
        datas.remove(t)
        notifyItemRemoved(indexOf)
    }
}