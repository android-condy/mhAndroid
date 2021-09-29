package com.idic.backstagemoudle.data

import com.idic.backstagemoudle.db.entity.AisleConfigEntity
import com.idic.backstagemoudle.db.entity.ContainerConfigEntity
import com.idic.httpmoudle.response.DKResponse
import io.reactivex.Observable

/**
 * 文 件 名: ContainerDataSource
 * 创 建 人: sineom
 * 创建日期: 2019-08-14 15:44
 * 修改时间：
 * 修改备注：
 */

interface StockManagerDataSource {

    interface GetContainersCallback {
        //数据加载成功
        fun onContainers(containerEntity: List<ContainerConfigEntity>)

        //无数据
        fun onContainersNotAvailable()
    }

    interface LoadProductsCallback {

        //数据加载成功
        fun onProductsLoad(products: List<AisleConfigEntity>)

        //无数据
        fun onProductNotAvailable()
    }

    //获取所有的货柜信息
    fun getAllContainer(callback: GetContainersCallback)

    //根据货柜ID获取对应的产品
    fun getProductsForContainer(containerId: String, callback: LoadProductsCallback)

    //修改货道信息
    fun uploadStock(aisleEntity: AisleConfigEntity): Observable<DKResponse>

    //全部补满
    fun allFillUp(): Observable<DKResponse>

    //整行补满
    fun rowFillUp(row: String, containerId: String): Observable<DKResponse>
}