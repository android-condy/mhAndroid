package com.idic.datamoudle

import com.idic.datamoudle.source.local.ADDataLocalSource
import com.idic.datamoudle.source.local.ProductLocalDataSource
import com.idic.datamoudle.source.remote.AdRemoteDataSource
import com.idic.datamoudle.source.remote.ProductRemoteDataSource
import com.idic.datamoudle.source.repository.ADRepository
import com.idic.datamoudle.source.repository.ProductRepository

/**
 * 文 件 名: Injection
 * 创 建 人: sineom
 * 创建日期: 2019-08-08 16:45
 * 修改时间：
 * 修改备注：
 */

object Injection {

    //获取产品存储器
    fun provideProductsRepository(): ProductRepository {
        val dataBase = DataApp.mInstance!!.dataBase!!
        return ProductRepository.getInstance(
            ProductLocalDataSource.getInstance(dataBase.productDao(), dataBase.categoryDao()),
            ProductRemoteDataSource.getInstance()
        )
    }

    fun provideAdRepository(): ADRepository {
        val dataBase = DataApp.mInstance!!.dataBase!!
        return ADRepository(
            ADDataLocalSource.getInstance(dataBase.bannerDao()),
            AdRemoteDataSource.getInstance()
        )
    }


}