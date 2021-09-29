package com.idic.login.db

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.idic.login.db.dao.LoginDao
import com.idic.login.db.entity.LoginInfo

/**
 * 文 件 名: LoginDatabase
 * 创 建 人: sineom
 * 创建日期: 2019-07-30 18:14
 * 修改时间：
 * 修改备注：
 */

@Database(entities = [(LoginInfo::class)], version = 1)
abstract class LoginDatabase : RoomDatabase() {

    abstract fun loginDao(): LoginDao

    companion object {
        @Volatile
        private var mInstance: LoginDatabase? = null

        fun getInstance(application: Application) =
            mInstance ?: synchronized(this) {
                mInstance
                    ?: buildDatabase(application).also { mInstance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                LoginDatabase::class.java, "login.db"
            ).build()

    }
}