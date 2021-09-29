package com.idic.backstagemoudle.data.remote

import com.blankj.utilcode.util.SPUtils
import com.idic.backstagemoudle.data.StockManagerDataSource
import com.idic.backstagemoudle.db.entity.AisleConfigEntity
import com.idic.backstagemoudle.db.entity.ContainerConfigEntity
import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.SPKeys
import com.idic.basecommon.utils.CustomObserver
import com.idic.httpmoudle.request.DKRequest
import com.idic.httpmoudle.request.FullpuRow
import com.idic.httpmoudle.request.UploadProductReq
import com.idic.httpmoudle.response.DKResponse
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.httpmoudle.utils.RxHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * 文 件 名: ContainerRemoteDataSource
 * 创 建 人: sineom
 * 创建日期: 2019-08-14 15:43
 * 修改时间：
 * 修改备注： 通过网络获取货柜的配置
 */

class StockManagerRemoteDataSource : StockManagerDataSource {

    override fun uploadStock(aisleEntity: AisleConfigEntity): Observable<DKResponse> {
        val uploadProductReq = UploadProductReq(
            "${aisleEntity.tempLockStock}",
            aisleEntity.productId,
            "${aisleEntity.tempStock}",
            aisleEntity.wayId,
            aisleEntity.productPrice,
            "${aisleEntity.tempCapacity}"
        )
        return RetrofitUtil.instance.getDefautlRetrofit()
            .modifyChannelInfo(HttpUtils.createBody(uploadProductReq))
            .compose(RxHolder.IO2Main())
    }

    override fun allFillUp(): Observable<DKResponse> {
        val response = DKRequest()
        response.deviceId = DeviceInfo.getInstance().deviceId
        return RetrofitUtil.instance.getDefautlRetrofit()
            .supplementFull(HttpUtils.createBody(response))
            .compose(RxHolder.IO2Main())
    }

    override fun rowFillUp(row: String, containerId: String): Observable<DKResponse> {
        val rowFillUpRequest = FullpuRow(containerId, row)
        return RetrofitUtil.instance.getDefautlRetrofit()
            .FillupChannelForRow(HttpUtils.createBody(rowFillUpRequest))
            .compose(RxHolder.IO2Main())
    }


    private var getContainerInfoDispo: Disposable? = null

    private var getProductsDisposable: Disposable? = null

    override fun getAllContainer(callback: StockManagerDataSource.GetContainersCallback) {
        getContainerInfoDispo?.dispose()
        RetrofitUtil.instance.getDefautlRetrofit()
            .loadContainerInfo(HttpUtils.createBody("{\"deviceId\":\"${DeviceInfo.getInstance().deviceId}\"}"))
            .observeOn(AndroidSchedulers.mainThread())
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    callback.onContainersNotAvailable()
                }
                success
            }.compose(RxHolder.getResponseListData<ContainerConfigEntity>())
            .compose(RxHolder.IO2Main())
            .subscribe(CustomObserver<List<ContainerConfigEntity>>().apply {
                _onError {
                    callback.onContainersNotAvailable()
                }
                _onNext {
                    if (it.isEmpty()) {
                        callback.onContainersNotAvailable()
                    } else {
                        callback.onContainers(it)
                    }
                }
                _subscribe {
                    getContainerInfoDispo = it
                }
            })
    }

    override fun getProductsForContainer(
        containerId: String,
        callback: StockManagerDataSource.LoadProductsCallback
    ) {
        getProductsDisposable?.dispose()
        RetrofitUtil.instance
            .getDefautlRetrofit()
            .loadContainerChannelInfo(
                HttpUtils.createBody(
                    "{\"containId\":\"$containerId\",\"deviceId\":\"${DeviceInfo.getInstance().deviceId}\"}"
                )
            ).observeOn(AndroidSchedulers.mainThread())
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    callback.onProductNotAvailable()
                }
                success
            }.compose(RxHolder.getResponseListData<AisleConfigEntity>())
            .compose(RxHolder.IO2Main())
            .subscribe(CustomObserver<List<AisleConfigEntity>>().apply {
                _onError {
                    callback.onProductNotAvailable()
                }
                _onNext { list ->
                    if (list.isEmpty()) {
                        callback.onProductNotAvailable()
                    } else {
                        val validDatas = ArrayList<AisleConfigEntity>()
                        //由于后台数据未返回对应的货柜id，需手动添加
                        list.filter {
                            it.productId.isNotEmpty() && it.status == "1"
                        }.forEach {
                            it.containerId = containerId
                            validDatas.add(it)
                        }
                        if (validDatas.isNotEmpty()) {
                            callback.onProductsLoad(validDatas)
                        } else {
                            callback.onProductNotAvailable()
                        }
                    }
                }
                _subscribe {
                    getProductsDisposable = it
                }
            })
    }

    companion object {

        private var mInstance: StockManagerRemoteDataSource? = null

        fun getInstance() = mInstance ?: synchronized(this) {
            mInstance ?: StockManagerRemoteDataSource().also { mInstance = it }
        }
    }
}