package com.idic.datamoudle.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import android.support.annotation.VisibleForTesting
import com.idic.datamoudle.db.dao.BannerDao
import com.idic.datamoudle.db.dao.CategoryDao
import com.idic.datamoudle.db.dao.ProductsDao
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.db.entity.CategoryEntity
import com.idic.datamoudle.db.entity.ProductEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 文 件 名: DataBase
 * 创 建 人: sineom
 * 创建日期: 2019-08-06 14:05
 * 修改时间：
 * 修改备注：
 */
@Database(
    entities = [ProductEntity::class, CategoryEntity::class, BannerEntity::class],
    version = 2,
    exportSchema = false
)

abstract class SourceDataBase : RoomDatabase() {

    abstract fun productDao(): ProductsDao

    abstract fun categoryDao(): CategoryDao

    abstract fun bannerDao(): BannerDao


    companion object {
        @Volatile
        private var mInstance: SourceDataBase? = null

        @VisibleForTesting
        val DATABASE_NAME = "drink_data"

        fun getInstance(application: Context): SourceDataBase {
            mInstance ?: synchronized(this) {
                mInstance ?: buildDatabase(application).also {
                    mInstance = it
                }
            }
            return mInstance!!
        }


        private fun buildDatabase(
            appContext: Context
        ): SourceDataBase {
            val db = Room.databaseBuilder(appContext, SourceDataBase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
//                        GlobalScope.launch {
//                            val categoryEntity =
//                                CategoryEntity("测试类目", "test", "test", "test", 1, "1")
//                            getInstance(appContext).categoryDao()
//                                .insertCategories(arrayListOf(categoryEntity))
//                            val datas = ArrayList<ProductEntity>()
//                            (1..100).forEach {
//                                val productEntity = ProductEntity()
//                                with(productEntity) {
//                                    num = 1
//                                    lockStock = 0
//                                    productId = "$it"
//                                    deviceWayId = "$it"
//                                    number = it
//                                    productImg = ""
//                                    stock = 5
//                                    productName = "商品$it"
//                                    categoryId = "1"
//                                    productPrice = 0.01F
//                                    capacity = 5
//                                    goodsWay = "$it"
//                                    localImg = ""
//                                }
//                                datas.add(productEntity)
//                            }
//                            getInstance(appContext).productDao().insertProducts(datas)
//                        }
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .build()

            return db
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ProductEntity ADD COLUMN localImg TEXT NOT NULL  DEFAULT \"1.jpg\"")
            }
        }

    }


}