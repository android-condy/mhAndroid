package com.idic.ordermoudle

import android.annotation.SuppressLint
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.TimeUtils
import com.idic.basecommon.utils.CustomObserver
import com.idic.httpmoudle.response.DKResponse
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.httpmoudle.utils.RxHolder
import com.idic.ordermoudle.request.*
import com.idic.ordermoudle.response.SQBDevList
import com.idic.ordermoudle.response.SQBOrderInfo
import com.idic.ordermoudle.response.SQBPayCode
import com.idic.ordermoudle.util.AntUtil
import com.idic.ordermoudle.util.UnifiedSignature
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * 文 件 名: OrderOperation
 * 创 建 人: sineom
 * 创建日期: 2019-08-15 14:39
 * 修改时间：
 * 修改备注：
 */

internal class OrderOperation : IOrder {

    private var getPayCodeDisposable: Disposable? = null

    private var createOrderDisposable: Disposable? = null

    private var queryPayStatusDisposable: Disposable? = null

    override fun createOrder(createOrder: CreateOrder, callBack: IOrder.GetOrderInfo) {
        createOrderDisposable?.dispose()
        RetrofitUtil.instance.getDefautlRetrofit()
            .addOrder(HttpUtils.createBody(createOrder))
            .observeOn(AndroidSchedulers.mainThread())
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    callBack.createFail()
                }
                success
            }.compose(RxHolder.getResponseData<SQBOrderInfo>())
            .compose(RxHolder.IO2Main())
            .subscribe(CustomObserver<SQBOrderInfo>().apply {
                _onNext {
                    callBack.createSuccess(it)
                }
                _onError {
                    callBack.createFail()
                }
                _subscribe {
                    createOrderDisposable = it
                }
            })
    }

    override fun cancelCreateOrder() {
        createOrderDisposable?.dispose()
    }

    override fun cancelOrder(orderId: String) {
        RetrofitUtil.instance.getDefautlRetrofit()
            .cancelShouQianBaOrder(HttpUtils.createBody(CancelOrder(orderId)))
            .subscribeOn(Schedulers.io())
            .subscribe(CustomObserver<DKResponse>())
    }

    override fun refundSQBOrder(refundData: List<SQBDevList>) {
        RetrofitUtil.instance.getDefautlRetrofit()
            .addRefundSQB(HttpUtils.createBody(RefundSQB(refundData)))
            .subscribeOn(Schedulers.io())
            .subscribe(CustomObserver<DKResponse>())
    }

    override fun getPayQrCode(orderId: String, callBack: IOrder.GetPayCode) {
        getPayCodeDisposable?.dispose()
        RetrofitUtil.instance.getDefautlRetrofit()
            .getSQBPayCode(HttpUtils.createBody(GetPayCodeRest(orderId)))
            .filter {
                val success = it.isSuccess() && it.data != null
                if (!success) {
                    callBack.getPayCodeFail()
                }
                success
            }.compose(RxHolder.getResponseData<SQBPayCode>())
            .compose(RxHolder.IO2Main())
            .subscribe(CustomObserver<SQBPayCode>().apply {
                _onNext {
                    callBack.getPayCodeSuccess(it)
                }
                _onError {
                    callBack.getPayCodeFail()
                }
                _subscribe {
                    getPayCodeDisposable = it
                }
            })
    }

    @SuppressLint("SimpleDateFormat")
    override fun getAntPayCode(
        params: HashMap<String, Any>,
        callBack: IOrder.GetPayCode
    ) {
        val datas = HashMap<String, Any>()
        datas["appId"] = "mylsQhaOX4EvADGd"
        datas["mchId"] = "722827011283046400"
        //数据全面
        datas["nonce"] = UUID.randomUUID().toString().replace("-", "")
        datas["bizContent"] = JSON.toJSONString(params)
        datas["notifyUrl"] = "http://san2018.w3.luyouxia.net/api/success/antsSuccess.do"
        datas["timestamp"] = TimeUtils.date2String(
            Date(),
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        )
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("appId", "${datas["appId"]}")
            .addFormDataPart("mchId", "${datas["mchId"]}")
            .addFormDataPart(
                "notifyUrl",
                "${datas["notifyUrl"]}"
            )
            .addFormDataPart(
                "sign",
//                UnifiedSignature.createSign(datas, "4a5ad70aafef48d651868b520fe6fe22")
                AntUtil.getSignToken(datas)
            )
            .addFormDataPart("nonce", "${datas["nonce"]}")
            .addFormDataPart("bizContent", "${datas["bizContent"]}")
            .addFormDataPart(
                "timestamp", TimeUtils.date2String(
                    Date(),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                )
            )
            .build()
        RetrofitUtil.instance.getDefautlRetrofit()
            .antCode(requestBody = requestBody)
            .compose(RxHolder.IO2Main())
            .subscribe(CustomObserver<Any>().apply {
                _onNext {
                    var data = JSON.parseObject(JSON.toJSONString(it))["data"] as? String

                    if (data.isNullOrEmpty()) {
                        callBack.getPayCodeFail()
                        return@_onNext
                    }
                    val codeUrl = JSON.parseObject(data)["payParam"] as? String
                    if (codeUrl.isNullOrEmpty()) {
                        callBack.getPayCodeFail()
                        return@_onNext
                    }
                    callBack.getPayCodeSuccess(SQBPayCode().apply {
                        wxQrCode = codeUrl
                        alipayQrCode = codeUrl
                    })
                }
                _onError {
                    callBack.getPayCodeFail()
                }
                _subscribe {
                    getPayCodeDisposable = it
                }
            })
    }

    override fun cancelGetPayCode() {
        getPayCodeDisposable?.dispose()
    }

    override fun querySQBPayStatus(orderID: String, callBack: IOrder.GetOrderStatus) {
        queryPayStatusDisposable?.dispose()
        RetrofitUtil.instance.getDefautlRetrofit()
            .queryShouQianBaIsPay(HttpUtils.createBody(QuerySQBPayStatus(orderID)))
            .compose(RxHolder.IO2Main())
            .subscribe(CustomObserver<DKResponse>().apply {
                _onNext {
                    if (it.isSuccess()) {
                        callBack.SQBPaySuccess()
                    } else {
                        callBack.waitPay()
                    }

                }
                _onError {
                    callBack.payFail()
                }
                _subscribe {
                    queryPayStatusDisposable = it
                }
            })
    }

    override fun cancelQuerySQBPayStatus() {
        queryPayStatusDisposable?.dispose()
    }
}