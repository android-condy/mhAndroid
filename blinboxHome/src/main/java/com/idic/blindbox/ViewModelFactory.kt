package com.idic.blindbox

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.support.annotation.VisibleForTesting
import android.util.Log
import com.blankj.utilcode.util.Utils
import com.idic.basecommon.BaseModelFactory
import com.idic.blindbox.model.MainModel
import com.idic.categorythome.model.PayModel
import com.idic.datamoudle.Injection
import com.idic.datamoudle.source.repository.ADRepository
import com.idic.datamoudle.source.repository.ProductRepository
import com.idic.ordermoudle.OrderManager

/**
 * 文 件 名: ViewModelFactory
 * 创 建 人: sineom
 * 创建日期: 2019-08-08 16:38
 * 修改时间：
 * 修改备注：
 */

internal class ViewModelFactory(
    val application: Application,
    private val productsRepository: ProductRepository,
    private val adRepository: ADRepository
) : BaseModelFactory(application) {

    override fun <T : ViewModel> getModel(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainModel::class.java) -> {
                    Log.i("condy","ViewModelFactory MainModel")
                    MainModel(application, productsRepository, adRepository)
                }
                isAssignableFrom(PayModel::class.java) -> {
                    PayModel(application, OrderManager.getInstance())
                }
                else -> {
                    null
                }
            }
        } as T?

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance() =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    Utils.getApp(),
                    Injection.provideProductsRepository(),
                    Injection.provideAdRepository()
                )
                    .also { INSTANCE = it }
            }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}