package com.idic.datamoudle.source.remote.request

import com.idic.httpmoudle.request.DKRequest

/**
 * 文 件 名: GetBannerAds
 * 创 建 人: sineom
 * 创建日期: 2019/3/5 11:25
 * 修改时间：
 * 修改备注：
 */

class GetBannerAds(val type: String = "1") : DKRequest() {
    init {
        ininKey_token_time()
    }

}