package com.idic.backstagemoudle.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.idic.basecommon.utils.ScreenType

/**
 * 文 件 名: ContainerEntity
 * 创 建 人: sineom
 * 创建日期: 2019-08-06 14:34
 * 修改时间：
 * 修改备注： 货柜设置
 */

@Entity(indices = [Index("num")])
data class ContainerConfigEntity(
    //驱动板地址
    @PrimaryKey
    val num: Int,
    //名称
    var containerName: String = "",
    //列数
    var columns: String = "",
    //行数
    var rows: String = "",

    //升降机是否开启
    var openLayer: Boolean = false,

    //升降机最大的层数
    var maxLayer: Int = 7,
    //光幕类型
    var screenType: ScreenType = ScreenType.Normal,

    var wayType: String = "",

    var containerId: String = "1",

    //记录当前的选择 临时变量，存储时赋值到实际变量中
    var tempOpenLayer: Boolean = false,
    var tempMaxLayer: Int = 7,
    var tempScreenType: ScreenType = ScreenType.Normal


) {
    fun updateChange(containerEntity: ContainerConfigEntity) {
        openLayer = containerEntity.tempOpenLayer
        maxLayer = containerEntity.tempMaxLayer
        screenType = containerEntity.tempScreenType
    }

    fun clearChange(containerEntity: ContainerConfigEntity) {
        tempOpenLayer = containerEntity.openLayer
        tempMaxLayer = containerEntity.maxLayer
        tempScreenType = containerEntity.screenType
    }
}
