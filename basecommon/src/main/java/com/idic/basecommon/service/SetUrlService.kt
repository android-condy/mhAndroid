package com.idic.basecommon.service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * 文 件 名: SetUrlService
 * 创 建 人: sineom
 * 创建日期: 2019-09-05 17:49
 * 修改时间：
 * 修改备注：
 */

interface SetUrlService : IProvider {

    fun setApiUrl(url:String,port:String)

    fun setImageUrl(url:String,port:String)
}