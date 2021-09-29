package com.idic.blindbox.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.idic.basecommon.delayqueryTime
import com.idic.basecommon.obtainViewModel
import com.idic.basecommon.utils.CustomObserver
import com.idic.blindbox.R
import com.idic.blindbox.RouterPath
import com.idic.blindbox.ViewModelFactory
import com.idic.categorythome.model.PayModel
import com.idic.datamoudle.db.entity.NewProductDetail
import com.idic.httpmoudle.response.BaseResponse
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.ordermoudle.BasePayActivity
import com.idic.ordermoudle.ORDER_INFO
import com.idic.ordermoudle.response.SQBOrderInfo
import com.idic.ordermoudle.response.SQBPayCode
import com.idic.utilmoudle.DKToastUtils
import com.idic.utilmoudle.QRCode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_blind_pay.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@Route(path = RouterPath.PAY)
class BlindPayActivity : BasePayActivity() {

    @Autowired(name = "good")
    @JvmField
    var productDetail: NewProductDetail? = null

    //支付方式,1 微信 反之支付宝
    @Autowired(name = "type")
    @JvmField
    var type: Int = 1
    var order_sn: String? = ""

    private val exitTimer = ExitTimer(200000L)

    private var mPayModel: PayModel? = null

    private var sqbData: SQBPayCode? = null
    private var mDisposable: Disposable? = null

    private var weChatBitmap: Bitmap? = null
    private var aliPayBitmap: Bitmap? = null
    private var orderStatusHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            resquestOrderStatus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blind_pay)
        ARouter.getInstance().inject(this)
        Log.i("condy", "productDetail=" + productDetail)
        val gson = Gson()
        val jsonArray = JsonArray()
        val jsonObject = JsonObject()
        jsonObject.addProperty("id", productDetail?.id)
        jsonObject.addProperty("sku_code", productDetail?.sku_code)
        jsonObject.addProperty("number", "1")
        jsonObject.addProperty("egg_code_x", productDetail?.egg_code_x)
        jsonObject.addProperty("egg_code_y", productDetail?.egg_code_y)
        jsonArray.add(jsonObject)
        var pro_list = gson.toJson(jsonArray)
        Log.i("condy", "=====" + pro_list)
        var map = HashMap<String, String>()
        map.put("pro_list", pro_list)
//        map.put("device_id", "FS1912550188")
        RetrofitUtil.instance.getDefautlRetrofit()
            .getCreateOrder(map = map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(CustomObserver<BaseResponse>().apply {
                _onNext {
                    Log.i("condy", "_onNext")
                    try {
                        var jsonObject = JSON.parseObject(JSON.toJSONString(it))
                        if (it.isSuccess()) {
                            order_sn = jsonObject.getJSONObject("data").getString("order_code")
                            var wxQrCode = jsonObject.getJSONObject("data").getString("pay_url")
                            val wh = Utils.getApp().resources.getDimension(R.dimen.x250).toInt()
                            ivPayQrCode?.let {
                                if (wxQrCode.isEmpty()) return@let
                                if (weChatBitmap == null) {
                                    weChatBitmap = QRCode.createQRCode(
                                        wxQrCode,
                                        wh
                                    )
                                }
                                it.setImageBitmap(weChatBitmap)
                                it.isClickable = false
                            }
                            orderStatusHandler.sendEmptyMessage(1)

                        } else {
                        }
                    } catch (e: Exception) {
                        DKToastUtils.showCustomShort("数据解析异常")
                    } finally {
                    }
                }
                _onError {
                }


            })
    }

    //轮询交易状态
    private fun resquestOrderStatus() {
        RetrofitUtil.instance.getDefautlRetrofit()
            .getOrderStatus(order_sn = order_sn!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(CustomObserver<Any>().apply {
                _onNext {
                    Log.i("condy", "_onNext")
                    try {
                        var jsonObject = JSON.parseObject(JSON.toJSONString(it))
                        var code = jsonObject.getIntValue("code")
                        if (code == 0) {
                            Log.i("condy", "付款成功!")
                            var status = jsonObject.getJSONObject("data").getIntValue("status")
                            if (status == 1) {
                                orderStatusHandler.removeMessages(1)
                                ARouter.getInstance().build(RouterPath.PAY_SUCCESS)
                                    .withObject("productDetail", productDetail)
                                    .withString("order_sn",order_sn)
                                    .navigation()
                                finish()
                            } else if (status == 4 || status == 3) {
                                tvScanInfo.text = "支付中"
                            }
                            if (status == 4 || status == 0) {
                                orderStatusHandler.sendEmptyMessageDelayed(1, 1000)
                            }

                        } else {
                            DKToastUtils.showCustomShort(jsonObject.getString("msg"))
                        }
                    } catch (e: Exception) {
                        orderStatusHandler.removeMessages(1)
                        DKToastUtils.showCustomShort("数据解析异常")
                    } finally {
                    }
                }
                _onError {
//                    DKToastUtils.showCustomShort(it.message)
                }

                _subscribe {
                    mDisposable = it
                }


            })
    }

    private fun initView() {
        tvChangePay.setOnClickListener {
            if (sqbData == null) {
                return@setOnClickListener
            }
            type = if (type == 1) {
                0
            } else {
                1
            }
//            changePay()
        }
    }

    override fun onResume() {
        super.onResume()
        exitTimer.start()
    }

    override fun onStop() {
        super.onStop()
        exitTimer.cancel()
        orderStatusHandler.removeMessages(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable?.dispose()
    }

    private fun changePay() {

        if (type == 1) {
            tvScanInfo.text = getString(R.string.wechatScan)
            tvChangePay.text = getString(R.string.AliPayScan)
            val wh = Utils.getApp().resources.getDimension(R.dimen.x250).toInt()
            ivPayQrCode?.let {
                val wxQrCode: String = "${sqbData?.wxQrCode}"
                if (wxQrCode.isEmpty()) return@let
                if (weChatBitmap == null) {
                    weChatBitmap = QRCode.createQRCode(
                        wxQrCode,
                        wh
                    )
                }
                it.setImageBitmap(weChatBitmap)
                it.isClickable = false
            }
        } else {
            tvScanInfo.text = getString(R.string.AliPayScan)
            tvChangePay.text = getString(R.string.weChatInfo)
            val wh = Utils.getApp().resources.getDimension(R.dimen.x250).toInt()
            ivPayQrCode?.let {
                val wxQrCode: String = "${sqbData?.alipayQrCode}"
                if (wxQrCode.isEmpty()) return@let
                if (aliPayBitmap == null) {
                    aliPayBitmap = QRCode.createQRCode(
                        wxQrCode,
                        wh
                    )
                }
                it.setImageBitmap(aliPayBitmap)
                it.isClickable = false
            }
        }
    }

    private fun createOrder() {
//        val productsInfo = ArrayList<OrderProduct>()
//        val orderProduct =
//            OrderProduct(
////                productEntity!!.productId,
////                "${productEntity!!.productPrice}",
////                productEntity!!.deviceWayId,
////                "1"
//            )
//        productsInfo.add(orderProduct)
//        mPayModel?.createOrder(CreateOrder(productsInfo, "${productEntity!!.productPrice}"), this)
    }

    override fun createSuccess(createOrderResponse: SQBOrderInfo) {
        super.createSuccess(createOrderResponse)
//        mPayModel?.getPayQrCode(createOrderResponse.orderId, this)
    }


    override fun createFail() {
        super.createFail()
        Log.i("http", "创建订单失败")
        DKToastUtils.showLong(getString(R.string.orderCreateFail))
    }

    override fun getPayCodeSuccess(sqbData: SQBPayCode) {
//        this.sqbData = sqbData
//        super.getPayCodeSuccess(sqbData)
//        val createQRCode = GlobalScope.launch(Dispatchers.Main) {
////            changePay()
//            //付款码显示成功开始轮询订单
//            mPayModel?.querySQBPayStatus("${sqbOrderInfo?.orderId}", this@BlindPayActivity)
//        }
//        jobs.add(createQRCode)
    }

    override fun getPayCodeFail() {
        super.getPayCodeFail()
        Log.i("http", "获取支付码失败")
//        DKToastUtils.showLong(getString(R.string.getPayCodeFail))
    }

    override fun SQBPaySuccess() {
//        if (isPay) return
//        super.SQBPaySuccess()
//        ARouter.getInstance().build(RouterPath.PAY_SUCCESS)
//            .withObject(ORDER_INFO, sqbOrderInfo)
//            .navigation()
//        finish()
    }

    override fun waitPay() {
        super.waitPay()
//        val queryOrderStatusJob = GlobalScope.launch {
//            delay(delayqueryTime)
//            if (isStop) return@launch
//            mPayModel?.querySQBPayStatus("${sqbOrderInfo?.orderId}", this@BlindPayActivity)
//        }
//        jobs.add(queryOrderStatusJob)
    }

    override fun payFail() {
        super.payFail()
//        waitPay()
    }

    private inner class ExitTimer(countTimer: Long) : CountDownTimer(countTimer, 1000) {

        override fun onFinish() {
            finish()

        }

        @SuppressLint("StringFormatMatches")
        override fun onTick(millisUntilFinished: Long) {
            tvTimeCount.text =
                    getString(R.string.payTime, "${(millisUntilFinished / 1000).toInt()}")
        }

    }
}