package com.idic.widgetmoudle.utils

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.idic.widgetmoudle.R

/**
 * 文 件 名: GlideUtil
 * 创 建 人: sineom
 * 创建日期: 2019-09-04 15:00
 * 修改时间：
 * 修改备注：
 */

object GlideUtil {
    private val loadingOptions = RequestOptions()
        .error(R.drawable.net_error_bg)
        .placeholder(R.drawable.loading_img)
        .priority(Priority.NORMAL)


    @SuppressLint("CheckResult")
    fun loadImg(imageView: ImageView, res: Any?) {
        Glide.with(imageView.context.applicationContext)
            .load(res)
            .apply(loadingOptions)
            .into(imageView)
    }

    @SuppressLint("CheckResult")
    fun loadImg(imageView: ImageView, res: Any?, @DrawableRes error: Int) {
        val options = RequestOptions()
            .error(error)
            .placeholder(R.drawable.loading_img)
            .priority(Priority.NORMAL)
        Glide.with(imageView.context.applicationContext)
            .load(res)
            .apply(options)
            .into(imageView)
    }



    //加载天气
    @JvmStatic
    @BindingAdapter("icon")
    fun loadWeather(imageView: ImageView, icon: String) {
        val drawable = when (icon) {
            "xue" -> {
                R.drawable.xue
            }
            "qing" -> {
                R.drawable.qing
            }
            "yin" -> {
                R.drawable.yin
            }

            "yu" -> {
                R.drawable.yu
            }
            "yun" -> {
                R.drawable.yun
            }
            "lei" -> {
                R.drawable.lei
            }
            "shachen" -> {
                R.drawable.shachen
            }
            "wu" -> {
                R.drawable.wu
            }
            "xiaoyu" -> {
                R.drawable.xiaoyu
            }
            "yujiaxue" -> {
                R.drawable.yujiaxue
            }
            "zhenyu" -> {
                R.drawable.zhenyu
            }
            "leizhenyu" -> {
                R.drawable.leizhenyu
            }
            "wu" -> {
                R.drawable.wu
            }
            else -> {
                R.drawable.loading_img
            }
        }

        Glide.with(imageView.context.applicationContext)
            .load(drawable)
            .into(imageView)

    }
}