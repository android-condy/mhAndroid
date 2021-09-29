package com.idic.backstagemoudle.data.reposity

import com.blankj.utilcode.util.SPUtils
import com.idic.backstagemoudle.data.remote.StockManagerRemoteDataSource
import com.idic.backstagemoudle.db.entity.AisleConfigEntity
import com.idic.backstagemoudle.db.entity.ContainerConfigEntity
import com.idic.basecommon.SPKeys
import com.idic.datamoudle.DataApp
import com.idic.httpmoudle.response.DKResponse
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 文 件 名: ContainerReposity
 * 创 建 人: sineom
 * 创建日期: 2019-08-07 15:39
 * 修改时间：
 * 修改备注：
 */

class StockManagerRepository
private constructor(private val containerRemoteDataSource: com.idic.backstagemoudle.data.StockManagerDataSource) :
    com.idic.backstagemoudle.data.StockManagerDataSource {


    override fun getProductsForContainer(
        containerId: String,
        callback: com.idic.backstagemoudle.data.StockManagerDataSource.LoadProductsCallback
    ) {
        if (!SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
            containerRemoteDataSource.getProductsForContainer(containerId, callback)
        } else {
            GlobalScope.launch {
                var allProducts = DataApp.mInstance!!.dataBase!!.productDao().getAllProducts()
                val products = ArrayList<AisleConfigEntity>()
                allProducts.forEach {
                    var i = it.number / 10
                    var i1 = it.number % 10
                    if (i1 == 0) {
                        i
                    } else {
                        i++
                    }
                    var aisleConfigEntity = AisleConfigEntity(
                        num = 1,
                        channelNumber = it.number,
                        containerId = "1",
                        wayId = it.deviceWayId,
                        columnSeial = "${if (i1 == 0) 10 else i1}",
                        rowSeial = "$i",
                        goodsWay = "${it.number}",
                        productId = "${it.number}",
                        productName = it.productName,
                        stock = it.stock,
                        lockStock = 0,
                        status = "1",
                        capacity = it.capacity,
                        productPrice = "${it.productPrice}"
                    )
                    products.add(aisleConfigEntity)
                }
                GlobalScope.launch(Dispatchers.IO) {
                    callback.onProductsLoad(products)
                }

            }
        }

    }

    override fun getAllContainer(callback: com.idic.backstagemoudle.data.StockManagerDataSource.GetContainersCallback) {
        if (!SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
            containerRemoteDataSource.getAllContainer(object :
                com.idic.backstagemoudle.data.StockManagerDataSource.GetContainersCallback {
                override fun onContainers(containerEntity: List<ContainerConfigEntity>) {
                    callback.onContainers(containerEntity)
                }

                override fun onContainersNotAvailable() {
                    callback.onContainersNotAvailable()
                }

            })
        } else {
            callback.onContainers(
                arrayListOf(
                    ContainerConfigEntity(
                        num = 1,
                        containerName = "测试货柜",
                        rows = "10",
                        columns = "10"
                    )
                )
            )
        }
    }

    override fun uploadStock(aisleEntity: AisleConfigEntity): Observable<DKResponse> {
        return containerRemoteDataSource.uploadStock(aisleEntity)
    }

    override fun allFillUp(): Observable<DKResponse> {
        return containerRemoteDataSource.allFillUp()
    }

    override fun rowFillUp(row: String, containerId: String): Observable<DKResponse> {
        return containerRemoteDataSource.rowFillUp(row, containerId)
    }

    companion object {
        private object Helper {
            val mInstance = StockManagerRepository(
                StockManagerRemoteDataSource()
            )
        }

        val mInstance =
            Helper.mInstance
    }
}