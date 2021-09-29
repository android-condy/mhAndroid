package com.idic.basecommon.service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * 文 件 名: URlService
 * 创 建 人: sineom
 * 创建日期: 2019-09-05 11:39
 * 修改时间：
 * 修改备注：
 */

interface GetURlService : IProvider {

    //获取api域名
    fun getApiUrl(): String

    //获取api域名的端口
    fun getApiPort(): String

    //获取图片的地址
    fun getImgUrl(): String

    //获取图片的端口
    fun getImgPort(): String
}