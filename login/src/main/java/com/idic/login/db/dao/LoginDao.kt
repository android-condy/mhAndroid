package com.idic.login.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.idic.login.db.entity.LoginInfo

/**
 * 文 件 名: LoginDao
 * 创 建 人: sineom
 * 创建日期: 2019-07-30 18:10
 * 修改时间：
 * 修改备注：
 */
@Dao
interface LoginDao {

    @Insert
    fun insert(loginInfo: LoginInfo)

    @Update
    fun update(loginInfo: LoginInfo)

    @Query("delete from logininfo")
    fun removeAll()

    @Query("select * from logininfo")
    fun getInfos(): List<LoginInfo>
}