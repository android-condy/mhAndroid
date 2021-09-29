package com.idic.datamoudle

import android.app.Application
import com.idic.basecommon.AppExecutors
import com.idic.basecommon.BaseApp
import com.idic.datamoudle.db.SourceDataBase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 文 件 名: DataApp
 * 创 建 人: sineom
 * 创建日期: 2019-08-21 16:37
 * 修改时间：
 * 修改备注：
 */

class DataApp : BaseApp() {

    var mAppExecutors: AppExecutors? = null
        private set

    var dataBase: SourceDataBase? = null
        private set

    override fun initModuleApp(application: Application) {
        mInstance = this
        mAppExecutors = AppExecutors()
    }

    override fun initModuleData(application: Application) {
        GlobalScope.launch {
            dataBase = SourceDataBase.getInstance(application)
            dataBase?.productDao()?.getAllProducts()
        }

    }


    companion object {

        var mInstance: DataApp? = null
    }

}