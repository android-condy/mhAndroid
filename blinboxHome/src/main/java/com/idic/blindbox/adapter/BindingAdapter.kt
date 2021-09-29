package com.idic.blindbox.adapter

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.idic.basecommon.SPKeys
import com.idic.blindbox.R
import com.idic.datamoudle.db.entity.ProductEntity

/**
 * 文 件 名: BindingAdapter
 * 创 建 人: sineom
 * 创建日期: 2019-08-12 14:05
 * 修改时间：
 * 修改备注：
 */

object BindingAdapter {

    private val loadingOptions = RequestOptions()
        .error(R.drawable.net_error_bg)
        .placeholder(R.drawable.loading_img)
        .priority(Priority.NORMAL)

    @SuppressLint("CheckResult")
    @JvmStatic
    @BindingAdapter("productImg")
    fun loadProductImg(imageView: ImageView, url: String) {
        val resource = R.drawable.loading_img
        loadingOptions.error(resource)
        Glide.with(Utils.getApp())
            .load(url)
            .apply(loadingOptions)
            .into(imageView)
    }

    //加载货道信息
    @JvmStatic
    @BindingAdapter("channel")
    fun initChannel(tvChannel: TextView, product: ProductEntity) {
        val num = product.num
        val number = (num - 1) * 100 + product.number
        tvChannel.text = "$number"
    }

    //加载货道信息
    @JvmStatic
    @BindingAdapter("bottomMenuChannel")
    fun initBottomChannel(tvChannel: TextView, product: ProductEntity) {
        val num = product.num
        val number = (num - 1) * 100 + product.number
        tvChannel.text = tvChannel.context.getString(R.string.channelNumber, number)
    }

}