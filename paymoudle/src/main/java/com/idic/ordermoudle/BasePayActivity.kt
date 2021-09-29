package com.idic.ordermoudle

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import com.alibaba.android.arouter.launcher.ARouter
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.events.ReadCardEvent
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.utils.CustomObserver
import com.idic.httpmoudle.response.DKResponse
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.ordermoudle.response.SQBOrderInfo
import com.idic.ordermoudle.response.SQBPayCode
import com.idic.utilmoudle.DKToastUtils
import io.reactivex.schedulers.Schedulers

/**
 * 文 件 名: BasePayActivity
 * 创 建 人: sineom
 * 创建日期: 2019-08-19 16:05
 * 修改时间：
 * 修改备注：
 */

open class BasePayActivity : BaseFullActivity(),
    IOrder.GetOrderInfo,
    IOrder.GetPayCode,
    IOrder.GetOrderStatus {

    //当前页面是否存在
    var isStop = false

    //是否已经支付
    var isPay = false

    //订单是否创建
    var isCreateOrder = false

    var sqbOrderInfo: SQBOrderInfo? = null



    override fun onStop() {
        super.onStop()
        isStop = true
    }

    override fun onResume() {
        super.onResume()
        isStop = false
    }

    override fun createSuccess(createOrderResponse: SQBOrderInfo) {
        isCreateOrder = true
        sqbOrderInfo = createOrderResponse

    }

    override fun createFail() {
        isCreateOrder = false
    }

    override fun getPayCodeSuccess(sqbData: SQBPayCode) {
    }

    override fun getPayCodeFail() {
    }

    override fun SQBPaySuccess() {
        isPay = true


    }

    override fun waitPay() {
    }

    override fun payFail() {
    }





}
