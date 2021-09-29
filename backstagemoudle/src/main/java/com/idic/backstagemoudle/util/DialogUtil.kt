package com.idic.backstagemoudle.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context

/**
 * 文 件 名: DialogUtil
 * 创 建 人: sineom
 * 创建日期: 2019-08-23 09:41
 * 修改时间：
 * 修改备注：
 */

object DialogUtil {
    fun createDialog(activity: Activity, okClick: (() -> Unit), cancelClick: (() -> Unit)): AlertDialog? {
        val ab = AlertDialog.Builder(activity)
        ab.setCancelable(false)
        ab.setTitle("温馨提醒")
        ab.setMessage("发现尚未保存的修改内容")
        ab.setNegativeButton(
            "保存"
        ) { dialog, which ->
            okClick.invoke()
            dialog.dismiss()
        }
        ab.setNeutralButton(
            "取消"
        ) { dialog, which ->
            cancelClick.invoke()
            dialog.dismiss()
        }
        return ab.create()
    }


    fun saveSharedPreferences(context: Context, value: String) {
        val sharedPreferences = context.getSharedPreferences("light", Context.MODE_PRIVATE); //私有数据
        val editor = sharedPreferences.edit();//获取编辑器
        editor.putString("key", value)
        editor.commit()
    }

    fun getSharedPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("light", Context.MODE_PRIVATE); //私有数据
        return sharedPreferences.getString("key", "")
    }

    fun saveUserId(context: Context, value: Int) {
        val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        val editor = sharedPreferences.edit();//获取编辑器
        editor.putInt("user_id", value)
        editor.commit()
    }

    fun getUserId(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        return sharedPreferences.getInt("user_id", 0)
    }
}