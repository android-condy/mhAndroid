package com.idic.backstagemoudle.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.blankj.utilcode.util.Utils
import com.idic.backstagemoudle.db.conver.DKConverters
import com.idic.backstagemoudle.db.dao.DeviceConfigDao
import com.idic.backstagemoudle.db.entity.AisleConfigEntity
import com.idic.backstagemoudle.db.entity.ContainerConfigEntity
import com.idic.backstagemoudle.db.entity.DeviceConfigEntity
import com.idic.httpmoudle.URL
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * 文 件 名: DataBase
 * 创 建 人: sineom
 * 创建日期: 2019-08-06 14:05
 * 修改时间：
 * 修改备注：
 */
@Database(
    entities = [DeviceConfigEntity::class, AisleConfigEntity::class, ContainerConfigEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DKConverters::class)
abstract class DeviceConfigDataBase : RoomDatabase() {

    abstract fun deviceConfigDao(): DeviceConfigDao


    companion object {
        @Volatile
        private var mInstance: DeviceConfigDataBase? = null

        fun getInstance(): DeviceConfigDataBase {
            mInstance ?: synchronized(this) {
                mInstance
                    ?: buildDatabase().also {
                        mInstance = it
                    }
            }
            return mInstance!!
        }


        private fun buildDatabase(): DeviceConfigDataBase {
            val db = Room.databaseBuilder(
                Utils.getApp(),
                DeviceConfigDataBase::class.java, "device_config"
            ).addCallback(
                object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        GlobalScope.launch {
                            val database = getInstance()
                            database.runInTransaction {
                                database.deviceConfigDao().insertContainer(
                                    DeviceConfigEntity().apply {
                                        domain = URL.API_IP
                                        port = URL.API_PORT
                                        imgBaseURL = URL.IMG_DOMIAN
                                        imgPort = URL.IMG_PORT
                                    }
                                )
                            }
                        }
                    }
                }
            )
                .build()
            val job = GlobalScope.launch {
                db.query("select 2", null)
            }
            runBlocking {
                job.join()
            }
            return db
        }
    }


}