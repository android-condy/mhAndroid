package com.idic.blindbox.home

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.boxtest.ShipmentManager
import com.idic.backstagemoudle.boxtest.common.outage
import com.idic.basecommon.utils.CustomObserver
import com.idic.blind.Shipment
import com.idic.blindbox.Config
import com.idic.blindbox.R
import com.idic.blindbox.RouterPath
import com.idic.blindbox.ad.BannerAdItemFragment
import com.idic.blindbox.adapter.BindingAdapter
import com.idic.blindbox.databinding.ActivityBlindShipmentBinding
import com.idic.datamoudle.Injection
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.db.entity.NewBannerEntity
import com.idic.datamoudle.db.entity.NewProductDetail
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.datamoudle.source.AdDataSource
import com.idic.datamoudle.source.repository.BannersDatasCallBack
import com.idic.datamoudle.utils.HttpActivityExt
import com.idic.datamoudle.utils.uploadChannelInfo
import com.idic.httpmoudle.request.UploadProductReq
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.ordermoudle.BasePaySuccessActivity
import com.idic.utilmoudle.DKToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_blind_shipment.*
import serialport_idic.MyFunc

@Suppress("DUPLICATE_LABEL_IN_WHEN")
@Route(path = RouterPath.PAY_SUCCESS)
class BlindShipmentActivity : BasePaySuccessActivity(), BannersDatasCallBack,
    AdDataSource.LoadAdDatasCallBack {
    override fun onBannerDataLoaded(banners: List<NewBannerEntity>) {
        val images = ArrayList<BannerEntity>()
        banners.forEach {
            var bannerEntity = BannerEntity()
            bannerEntity.image = it.img_url
            images.add(bannerEntity)
        }
        runOnUiThread {
            adBanner?.let { bannerViewPager ->
                bannerViewPager.removeAllViews()
                bannerViewPager.removeMessage()
                bannerViewPager.setPagerFragment(
                    images.size,
                    supportFragmentManager
                ) {
                    BannerAdItemFragment.newInstance(images[it])
                }
                indicaZoom?.let {
                    it.removeAllViews()
                    it.addPagerData(bannerViewPager)
                    if (images.size > 0) {
                        it.onPageSelected(0)
                    }
                }
            }
        }


        runOnUiThread {
            binding.product = productDetail
            BindingAdapter.loadProductImg(ivPrdbg, productDetail?.image_url!!)
        }
    }

    override fun onBannerNotData() {
    }

    @Autowired(name = "productDetail")
    @JvmField
    var productDetail: NewProductDetail? = null
    @Autowired(name = "order_sn")
    @JvmField
    var order_sn: String = ""
    private lateinit var binding: ActivityBlindShipmentBinding

    private var order_byte = byteArrayOf()

    private val timeHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        ShipmentManager.setErrorHandler(errorHandler)
        ShipmentManager.setReceiverHandler(receiveHandler)
        initView()
        HttpActivityExt.getGoodsSuccessBanners(this)
        XLog.i("http", "开始查询出货信息")
        ShipmentManager.machineFaultQuery()
    }

    override fun onStop() {
        super.onStop()
        timeHandler.removeCallbacks(toErrorTask)
    }

    override fun onDestroy() {
        super.onDestroy()
        ShipmentManager.setErrorHandler(null)
        ShipmentManager.setReceiverHandler(null)
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_blind_shipment
        )
//        binding.lifecycleOwner = this
        binding.setLifecycleOwner(this)
        Glide.with(this).asGif()
            .load(R.drawable.blin_shipmenting)
            .into(logo2)

        timeHandler.postDelayed(toErrorTask, 200000)
        bgVideo.setVideoURI(Uri.parse(Config.BG_VIDEO_URI))
        bgVideo.setOnPreparedListener {
            mediaPlayer = it
            it.isLooping = true
            it.start()
        }
    }

    override fun onBannerLoaded(banners: List<BannerEntity>) {

    }

    override fun onAdDataNotAvaliad() {
    }

    override fun onProductLoaded(product: ProductEntity) {
//        runOnUiThread {
//            binding.product = product
//            BindingAdapter.loadProductImg(ivPrdbg, product.productImg)
//        }
//        Log.i("http", "开始查询出货信息")
//        ShipmentManager.machineFaultQuery()

    }

    override fun onProductNotAvailable() {
        Log.i("http", "获取产品信息失败")
        shipmentFail()
    }

    override fun shipmentSuccess() {

    }


    override fun shipmentFail() {
//        if (refundOrder.isNotEmpty()) {
//            OrderManager.getInstance().refundSQBOrder(refundOrder)
//        }
//        toError()
    }

    private val errorHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            XLog.d("errorHandler")
            toError("异常错误")
        }
    }

    private fun toError(text: String) {
        resquestTakeFail("2", text)

    }

    //接收串口返回信息的handler
    private val receiveHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg == null) {
                return
            }
            msg.data.getByteArray(Shipment.RECEIVER_DATA)?.let { bytes ->
                val bysStr = MyFunc.toHexString(bytes).split(" ").toString()
                XLog.d("返回指令:" + bysStr)
//                tv_message.text=tv_message.text.toString()+"\n["+MyFunc.toHexString(bytes.copyOfRange(4,5))+"] "+bysStr
                if (bytes.size < 5 || bytes.last() != 0x0D.toByte()) {
                    //出货结果查询指令
                    ShipmentManager.machineFaultQuery()
                    return@let
                }

                when (bytes[4] * bytes[5]) {
                    0x01.toByte() * 0x02.toByte() -> {//机器心跳接口，不处理
                        XLog.d("心跳接口")
                    }
                    0x04.toByte() * 0x27.toByte() -> {
                        //出货结果
                        if (bytes[bytes.lastIndex - 3] == 0x00.toByte()) {
                            XLog.d("出货失败" + bysStr)
                            toError("出货结果失败:" + bysStr)
//                            ShipmentManager.machineFaultQuery()
                        } else {
                            XLog.d("开始查询出货结果")
                            //出货结果查询指令
                            ShipmentManager.queryShipmentResult(order_byte)
                        }
                    }
                    0x04.toByte() * 0x2A.toByte() -> {
                        //出货结果的查询
                        val count = bytes.count { it.equals(0xa8.toByte()) }
                        var tempBytes: ByteArray
                        if (count == 1) {
                            tempBytes = bytes
                        } else {
                            tempBytes = bytes.copyOfRange(0, bytes.binarySearch(0xa8.toByte(), 1))
                        }
                        if (tempBytes.size < 15) return@let
                        when (tempBytes.size - 6) {

                            ShipmentManager.inShipmentLength -> {
                                //正在出货中
                                //出货结果查询指令
                                ShipmentManager.queryShipmentResult(order_byte)
                            }
                            in ShipmentManager.shippedCompleteMinLength..ShipmentManager.shippedCompleteMaxLength -> {
                                //出货结束
                                val byte = tempBytes[tempBytes.lastIndex - 14]
                                if (byte == 0x00.toByte()) {
                                    XLog.d("出货成功")
//                                    shipmentSuccess(binding.product!!)
                                    resquestTakeSuccess()
                                } else {
                                    XLog.d("出货失败" + bysStr)
                                    toError("出货失败:" + bysStr)
                                }
                            }
                        }

                    }
                    0x01.toByte() * 0x2A.toByte() -> {
                        //系统故障查询
                        if (bytes.size < 12) return@let
                        val size = bytes.size - 4
                        for (index in 11 until size step 2) {
                            val msg = outage[bytes[index] + bytes[index + 1]] ?: continue
                            if (!msg.isEmpty() && msg.contains("货斗有货物")) {
                                XLog.d("系统故障---->货斗有货,开始执行回收")
                                ShipmentManager.recycling()
                                return
                            } else {
                                XLog.d("不可恢复故障" + outage[bytes[index] + bytes[index + 1]] + ":" + bysStr)
                                toError(outage[bytes[index] + bytes[index + 1]] + ":" + bysStr)
                                return
                            }
                        }
                        XLog.d("机器无故障并且货斗无货,开始出货")
                        order_byte = ShipmentManager.startShipment(productDetail?.aisles!!)
                    }
                    0x04.toByte() * 0x2E.toByte() -> {
                        when (bytes[bytes.lastIndex - 3]) {
                            0x00.toByte() -> {
                                //回收执行失败
                                XLog.d("回收执行成功失败" + bysStr)
                                toError("回收失败:" + bysStr)//+bysStr)
                            }
                            0x01.toByte() -> {
                                //回收执行成功
                                XLog.d("回收执行成功,开始查询回收结果")
                                ShipmentManager.queryRecycling()
                            }
                        }
                    }
                    0x04.toByte() * 0x2F.toByte() -> {
                        when (bytes[bytes.lastIndex - 3]) {
                            0x00.toByte() -> {
                                //回收空闲
                                XLog.d("回收空闲,开始出货")
                                order_byte = ShipmentManager.startShipment(productDetail?.aisles!!)
                            }
                            0x01.toByte() -> {
                                //回收进行中
                                XLog.d("回收进行中,继续查询")
                                ShipmentManager.queryRecycling()
                            }
                            0x02.toByte() -> {
                                //回收成功
                                XLog.d("回收成功,开始出货")
                                order_byte = ShipmentManager.startShipment(productDetail?.aisles!!)
                            }
                            0x03.toByte() -> {
                                //回收失败
                                XLog.d("回收失败" + bysStr)
                                toError("回收失败:" + bysStr)
                            }
                        }
                    }
                    else -> {
                        XLog.d("其他指令" + bysStr)
                        toError("其他指令：" + bysStr)//+bysStr)
                        //出货结果查询指令
//                        ShipmentManager.machineFaultQuery()
                    }
                }
            }
        }
    }

    private fun shipmentSuccess(product: ProductEntity) {
        var newStock = product.stock - 1
        if (newStock < 0) newStock = 0
        val uploadProductReq = UploadProductReq(
            "0",
            product.productId,
            "$newStock",
            product.deviceWayId,
            "${product.productPrice}",
            "${product.capacity}"
        )
        product.stock = newStock
        Injection.provideProductsRepository()
            .updateProduct(product)
        uploadChannelInfo(uploadProductReq)
        ARouter.getInstance().build(RouterPath.SHIPMENT_SUCCESS)
            .withString("url", product.productImg)
            .navigation()
        finish()
    }

    private val toErrorTask = Runnable {
        XLog.d("超时错误")
        toError("超时错误")
    }


    //取货是否成功
    private fun resquestTakeSuccess() {
        RetrofitUtil.instance.getDefautlRetrofit()
            .getTakeStatus(
                error_id = "1", message = "取货成功", order_sn = order_sn,
                aisles = productDetail?.aisles.toString(),
                egg_code_x = productDetail?.egg_code_x.toString(),
                egg_code_y = productDetail?.egg_code_y.toString()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(CustomObserver<Any>().apply {
                _onNext {
                    Log.i("condy", "_onNext")
                    try {
                        var jsonObject = JSON.parseObject(JSON.toJSONString(it))
                        var code = jsonObject.getIntValue("code")
                        if (code == 0) {
                            ARouter.getInstance().build(RouterPath.SHIPMENT_SUCCESS)
                                .withString("url", productDetail?.image_url)
                                .navigation()
                            finish()
                        } else {
                            DKToastUtils.showCustomShort(jsonObject.getString("msg"))
                        }
                    } catch (e: Exception) {
                        DKToastUtils.showCustomShort("数据解析异常")
                    } finally {
                    }
                }
                _onError {
                    //                    DKToastUtils.showCustomShort(it.message)
                }

                _subscribe {
                }


            })
    }

    //取货是否成功
    private fun resquestTakeFail(error_id: String, message: String) {
        RetrofitUtil.instance.getDefautlRetrofit()
            .getTakeStatus(
                error_id = error_id, message = message, order_sn = order_sn,
                aisles = productDetail?.aisles.toString(),
                egg_code_x = productDetail?.egg_code_x.toString(),
                egg_code_y = productDetail?.egg_code_y.toString()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(CustomObserver<Any>().apply {
                _onNext {
                    Log.i("condy", "_onNext")
                    try {
                        var jsonObject = JSON.parseObject(JSON.toJSONString(it))
                        var code = jsonObject.getIntValue("code")
                        if (code == 0) {
                            ARouter.getInstance().build(RouterPath.SHIPMENT_ERROR)
                                .withString("url", productDetail?.image_url)
                                .navigation()
                            finish()
                        } else {
                            DKToastUtils.showCustomShort(jsonObject.getString("msg"))
                        }
                    } catch (e: Exception) {
                        DKToastUtils.showCustomShort("数据解析异常")
                    } finally {

                    }
                }
                _onError {
                    //                    DKToastUtils.showCustomShort(it.message)
                }

                _subscribe {
                }


            })
    }

}