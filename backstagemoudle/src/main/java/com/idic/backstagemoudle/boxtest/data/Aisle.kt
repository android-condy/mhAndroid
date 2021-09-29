package com.idic.backstagemoudle.boxtest.data

/**
 * 文 件 名: Aisle
 * 创 建 人: sineom
 * 创建日期: 2020/6/18 15:18
 * 修改时间：
 * 修改备注：
 */

data class Aisle(
    //编号
    val number: Int,
    //行数
    var row: Int = 0,
    //列数
    var column: Int = 0
)