package com.idic.backstagemoudle.ui.adapter

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.R
import com.idic.backstagemoudle.db.entity.AisleConfigEntity
import com.idic.backstagemoudle.db.entity.ContainerConfigEntity
import com.idic.backstagemoudle.viewmodel.CheckInfo

/**
 * 文 件 名: BindingAdapter
 * 创 建 人: sineom
 * 创建日期: 2019-08-22 17:47
 * 修改时间：
 * 修改备注：
 */

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("aisleModify")
    fun aisleModify(view: View, aisleEntity: AisleConfigEntity) {
        view.isSelected = aisleEntity.motorType != aisleEntity.tempMotorType
    }

    @JvmStatic
    @BindingAdapter("screenTypeModify")
    fun screenTypeModify(view: View, containerEntity: ContainerConfigEntity) {
        view as RadioButton
        view.isSelected =
            containerEntity.screenType != containerEntity.tempScreenType && view.isChecked
    }

    @JvmStatic
    @BindingAdapter("layerModify")
    fun layerModify(view: View, containerEntity: ContainerConfigEntity) {
        view as RadioButton
        view.isSelected =
            containerEntity.openLayer != containerEntity.tempOpenLayer && view.isChecked
    }

    @JvmStatic
    @BindingAdapter("stockManagerChannel")
    fun stockPrdChannel(view: View, aisleEntity: AisleConfigEntity) {
        view as TextView
        var channelNumber = 0
        try {
            val rowSeial = aisleEntity.rowSeial ?: "1"
            val columnSeial = aisleEntity.columnSeial ?: "0"
            channelNumber = (rowSeial.toInt() - 1) * 10 + columnSeial.toInt()
        } catch (e: Exception) {
        }

        view.text = view.context.resources.getString(R.string.channelNumber, channelNumber)
    }

    @JvmStatic
    @BindingAdapter("updateStockBg")
    fun stockBg(view: View, aisleEntity: AisleConfigEntity) {
        view.isSelected = aisleEntity.stock < 1

    }

    @JvmStatic
    @BindingAdapter("stockModify")
    fun stockModify(view: View, aisleEntity: AisleConfigEntity) {
//        aisleEntity.tempCapacity = aisleEntity.capacity
//        aisleEntity.tempLockStock = aisleEntity.lockStock
//        aisleEntity.tempStock = aisleEntity.stock
        view.isActivated = aisleEntity.tempCapacity != aisleEntity.capacity ||
                aisleEntity.tempLockStock != aisleEntity.lockStock ||
                aisleEntity.tempStock != aisleEntity.stock
        view.isSelected = aisleEntity.stock < 1
    }

    @JvmStatic
    @BindingAdapter("checkBg")
    fun checkMsgBg(view: View, checkerInfo: CheckInfo) {
        view.isSelected = checkerInfo.isSuccess
        view as TextView
        view.text = checkerInfo.msg
    }

    private val loadingOptions = RequestOptions()
        .error(R.drawable.net_error_bg)
        .placeholder(R.drawable.loading_img)
        .priority(Priority.NORMAL)

    @SuppressLint("CheckResult")
    @JvmStatic
    @BindingAdapter("img")
    fun img(imageView: ImageView, url: String) {

        Glide.with(Utils.getApp())
            .load(url)
            .apply(loadingOptions)
            .into(imageView)
    }
}