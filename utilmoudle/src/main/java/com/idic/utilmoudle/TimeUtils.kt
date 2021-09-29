package com.idic.utilmoudle

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * 文 件 名: TimeUtils
 * 创 建 人: sineom
 * 创建日期: 2018/11/17 14:09
 * 修改时间：
 * 修改备注：
 */

object TimeUtils {

    @SuppressLint("SimpleDateFormat")
    val dateFormate = SimpleDateFormat("yyMMddHHmmss")

    @SuppressLint("SimpleDateFormat")
    val yearMD = SimpleDateFormat("yyyy年MM月dd日HH:mm EEEE")

    fun getTimestamp(): String {
        return Date().time.toString()
    }

    fun getYearDay(): String {
        return yearMD.format(Date())
    }

    fun getQuetyOrderTime(): String {
        return dateFormate.format(Date())
    }
}