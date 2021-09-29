package com.idic.backstagemoudle.ui

import android.view.View
import com.idic.backstagemoudle.db.entity.AislesBindGoodsEntity

/**
 * 文 件 名: StockManagerModifyCallback
 * 创 建 人: sineom
 * 创建日期: 2019-08-24 16:46
 * 修改时间：
 * 修改备注：
 */

interface StockManagerModifyCallback {

    fun add(view: View, aisleEntity: AislesBindGoodsEntity)
//
    fun sub(view: View, aisleEntity: AislesBindGoodsEntity)
//
    fun onCountClick(view: View, aisleEntity: AislesBindGoodsEntity)
    fun save(view: View, aisleEntity: AislesBindGoodsEntity)
//
//    fun productDetail(view: View, aisleEntity: AisleConfigEntity)
    fun productDetail(view: View, aisleEntity: AislesBindGoodsEntity)
}