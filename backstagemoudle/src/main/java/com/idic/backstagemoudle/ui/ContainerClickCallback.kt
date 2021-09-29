package com.idic.backstagemoudle.ui

import android.view.View
import com.idic.backstagemoudle.db.entity.ContainerConfigEntity

/**
 * 文 件 名: ContainerClickCallback
 * 创 建 人: sineom
 * 创建日期: 2019-08-22 13:31
 * 修改时间：
 * 修改备注：
 */

interface ContainerClickCallback {

    fun onClick(view: View, containerEntity: ContainerConfigEntity)

    fun onScreenClick(view: View, containerEntity: ContainerConfigEntity)

    fun onLayerOpenClick(view: View, containerEntity: ContainerConfigEntity)
}