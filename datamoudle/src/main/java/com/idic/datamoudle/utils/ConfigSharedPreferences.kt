package com.idic.datamoudle.utils

import android.content.Context
import com.blankj.utilcode.util.AppUtils
import com.idic.basecommon.R

/**
 * 文 件 名: Config
 * 创 建 人: sineom
 * 创建日期: 2020/8/4 09:23
 * 修改时间：
 * 修改备注：
 */

object ConfigSharedPreferences {
    fun saveSharedPreferences(context: Context, value: Int) {
        val sharedPreferences = context.getSharedPreferences("flash_version", Context.MODE_PRIVATE); //私有数据
        val editor = sharedPreferences.edit();//获取编辑器
        editor.putInt("flash_version", value)
        editor.commit()
    }

    fun getSharedPreferences(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences("flash_version", Context.MODE_PRIVATE); //私有数据
        return sharedPreferences.getInt("flash_version", 0)
    }

    fun saveSharedPreferences(context: Context, value: String) {
        val sharedPreferences = context.getSharedPreferences("flash_version", Context.MODE_PRIVATE); //私有数据
        val editor = sharedPreferences.edit();//获取编辑器
        editor.putString("last_video_url", value)
        editor.commit()
    }

    fun getSharedPreferencesVideoAdrress(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("flash_version", Context.MODE_PRIVATE); //私有数据
        return sharedPreferences.getString("last_video_url","")!!
    }

}