package com.idic.datamoudle.source.remote.response

import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.httpmoudle.response.DKResponse

/**
 * 文 件 名: LoadProductResponse
 * 创 建 人: sineom
 * 创建日期: 2019/1/31 16:57
 * 修改时间：
 * 修改备注：
 */

class LoadProductResponse : DKResponse() {

    //总页数
    var pageCount: Int = 0

    //总数量
    var count: Int = 0

    //每页大小
    var pageSize: Int = 0

    //当前页面index
    var currentPage: Int = 0

    //当前页面数据
    var resultList: List<ProductEntity>? = null

    override fun toString(): String {
        super.toString()
        return "LoadProductResponse(pageCount=$pageCount, count=$count, pageSize=$pageSize, currentPage=$currentPage, resultList=$resultList)"
    }


}