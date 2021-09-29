package com.idic.basecommon.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.idic.basecommon.utils.ScreenType

/**
 * 文 件 名: AisleService
 * 创 建 人: sineom
 * 创建日期: 2019-09-05 11:05
 * 修改时间：
 * 修改备注：
 */


interface AisleService : IProvider {

    //获取电机类型
    fun getMotorType(containerNumber: Int, channelNumber: Int): Int

    //获取光幕类型
    fun getScreenType(containerNumber: Int): ScreenType

    //升降机是否打开
    fun isOpenLayer(containerNumber: Int): Boolean

    //升降机最大层数
    fun getMaxLayer(containerNumber: Int):Int
}