package com.idic.httpmoudle.service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * 文 件 名: AccessTokenService
 * 创 建 人: sineom
 * 创建日期: 2019-08-02 17:53
 * 修改时间：
 * 修改备注：
 */

interface HttpConfigService : IProvider {
    /*
        token暂时无用，传任意值即可
     */
    fun updateToken(token: String = "token")
}