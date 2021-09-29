package com.idic.ordermoudle

import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.elvishew.xlog.XLog
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.DeviceInfo
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.service.AisleService
import com.idic.datamoudle.Injection
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.datamoudle.source.ProductDataSource
import com.idic.httpmoudle.request.UploadProductReq

import com.idic.ordermoudle.event.ShipmentResult
import com.idic.ordermoudle.response.SQBDevList
import com.idic.ordermoudle.response.SQBOrderInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val ORDER_INFO = "productDetail"

abstract class BasePaySuccessActivity : BaseFullActivity(),
    ProductDataSource.GetProductCallback {


    open lateinit var mSqbOrderInfo: SQBOrderInfo

    val refundOrder = ArrayList<SQBDevList>()

    private var curSQBDevList: SQBDevList? = null


    fun initData() {
//        GlobalScope.launch {
//            mSqbOrderInfo.devList?.let { list ->
//                list.forEach {
//                    curSQBDevList = it
//                    Injection.provideProductsRepository()
//                        .getProduct(it.id, this@BasePaySuccessActivity)
//                }
//            }
//        }
    }


    override fun onProductLoaded(product: ProductEntity) {

        //计算行数,每行最大数量10,第二行其实为11 第三行21 一次类推
//        XLog.d("找到商品---->$product")
//        val number = product.number
//        val remainder = number % 10
//        val layer = if (remainder == 0) {
//            remainder
//        } else {
//            number / 10 + 1
//        }
//        val aisleService = ARouter.getInstance().navigation(AisleService::class.java)
//        val motorType = aisleService
//            .getMotorType(product.num, number)
//        val screenType = aisleService.getScreenType(product.num)
//        val openLayer = aisleService.isOpenLayer(product.num)
//        val maxLayer = aisleService.getMaxLayer(product.num)
//        XLog.d("开始出货--->number-$number motorType-$motorType product.num-${product.num} screenType-$screenType  maxLayer-$maxLayer")
//        val startShipment = ShipmentHolper.startShipment(
//            layer,
//            number,
//            motorType,
//            product.num,
//            openLayer,
//            screenType,
//            maxLayer
//        )
//        XLog.d("出货完成----->结果:$startShipment")
//        UploadLog.uploadMsg(
//            DeviceInfo.getInstance().deviceId,
//            DeviceInfo.getInstance().merchantId,
//            "${mSqbOrderInfo?.orderId}",
//            product.deviceWayId,
//            ShipmentHolper.getUploadMsg(),
//            startShipment
//        )
//        val configServer = DeviceInfo.getInstance().deviceConfig
//        val newStock = product.stock - 1
//        var uploadProductReq = UploadProductReq(
//            "${product.lockStock}",
//            product.productId,
//            "${product.stock}",
//            product.deviceWayId,
//            "${product.productPrice}",
//            "${product.capacity}"
//        )
//        if (configServer.stockIsPay) {
//            product.stock = newStock
//            Injection.provideProductsRepository().updateProduct(product)
//            uploadProductReq = uploadProductReq.copy(stock = "$newStock")
//        } else {
//            if (startShipment) {
//                product.stock = newStock
//                Injection.provideProductsRepository().updateProduct(product)
//                uploadProductReq = uploadProductReq.copy(stock = "$newStock")
//            } else {
//                curSQBDevList?.let {
//                    refundOrder.add(it)
//                }
//            }
//        }
//        if (!startShipment) {
//            XLog.d("出货失败")
//            shipmentFail()
//        } else {
//            XLog.d("出货成功")
//            shipmentSuccess()
//        }
//        uploadChannelInfo(uploadProductReq)
//        RxRelay.instance.post(ShipmentResult(startShipment))
    }

    override fun onProductNotAvailable() {
        Log.i("http","获取产品信息失败")
        shipmentFail()
    }

    abstract fun shipmentSuccess()

    abstract fun shipmentFail()
}
