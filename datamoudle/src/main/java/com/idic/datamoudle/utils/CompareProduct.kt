package com.idic.datamoudle.utils

import com.idic.datamoudle.db.entity.ProductEntity
import java.util.*

/**
 * 文 件 名: ComparatorValues
 * 创 建 人: sineom
 * 创建日期: 2018/12/27 14:03
 * 修改时间： soldCom 表示是否根据已售罄排序，否则依据货道编号排序
 * 修改备注：
 */

class CompareProduct(private val soldCom: Boolean = false) : Comparator<ProductEntity> {

    override fun compare(goodsDataResultModel: ProductEntity, t1: ProductEntity): Int {
        val num1 = goodsDataResultModel.number + ((goodsDataResultModel.num - 1) * 100)
        val num2 = t1.number + ((t1.num - 1) * 100)
        if (soldCom) {
            val goodsDataResultModelStock = goodsDataResultModel.stock
            val goodsDataResultModelLockStock = goodsDataResultModel.lockStock
            val t1Stock = t1.stock
            val t1LockStock = t1.lockStock
            val b = goodsDataResultModelStock - goodsDataResultModelLockStock < 1
            val b1 = t1Stock - t1LockStock < 1
            return if (!b && !b1) {
                num1.compareTo(num2)
            } else if (!b && b1) {
                -1
            } else if (b && !b1) {
                1
            } else if (b && b1) {
                num1.compareTo(num2)
            } else {
                0
            }
        }
        return num1.compareTo(num2)
    }
}
