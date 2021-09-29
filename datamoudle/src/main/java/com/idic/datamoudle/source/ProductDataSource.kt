package com.idic.datamoudle.source

import com.idic.datamoudle.db.entity.CategoryEntity
import com.idic.datamoudle.db.entity.ProductEntity

/**
 * 文 件 名: ProductDataSource
 * 创 建 人: sineom
 * 创建日期: 2019-08-06 14:18
 * 修改时间：
 * 修改备注：
 */

interface ProductDataSource {

    interface LoadProductsCallback {

        //数据加载成功
        fun onProductsLoad(products: List<ProductEntity>)

        //无数据
        fun onProductNotAvailable()
    }

    interface GetProductCallback {

        //单个产品获取成功
        fun onProductLoaded(product: ProductEntity)

        //无相关产品数据
        fun onProductNotAvailable()
    }

    interface LoadProductCategory {
        //产品类目回调
        fun onCategoryLoaded(categorice: List<CategoryEntity>)

        //获取产品回调，无类目
        fun onCategoryNotAvailabled()
    }

    interface IProductsFromRemote {
        //获取产品类目
        fun loadProductCategory(callback: LoadProductCategory)

        //获取所有的产品
        fun getProducts(callback: LoadProductsCallback)

        //根据类目获取该类目下的所有产品
        fun getProductsFromCategory(category: String, callback: LoadProductsCallback)
    }

    interface IProductsFromLocal {

        //获取产品类目
        fun loadProductCategory(callback: LoadProductCategory)

        //获取所有的产品
        fun getProducts(callback: LoadProductsCallback)

        //根据类目获取该类目下的所有产品
        fun getProductsFromCategory(category: String, callback: LoadProductsCallback)

        //根据货道id获取产品信息
        fun getProduct(deviceWayId: String, callback: GetProductCallback)

        //分页获取产品
        fun getProductFromLimit(start: Int, count: Int, callback: LoadProductsCallback)

        //根据类目分页获取产品
        fun getProductFromLimitForCategory(
            category: String,
            start: Int,
            count: Int,
            callback: LoadProductsCallback
        )

        //保存产品
        fun updateProduct(product: ProductEntity)

        //更新所有的产品
        fun refreshProducts()

        //删除所有的产品
        fun deleteAllProducts()

        //根据货道ID删除对应的产品
        fun deleteProduct(deviceWayId: String)

        //插入所有的类目数据
        fun insertCategories(categories: List<CategoryEntity>)

        //插入所有的产品数据
        fun insertProducts(products: List<ProductEntity>)

        fun getProductsFromProductId(productID: String): List<ProductEntity>

    }


}