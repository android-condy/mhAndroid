package com.idic.blindbox.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSON
import com.google.gson.JsonObject
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.utils.CustomObserver
import com.idic.basecommon.utils.FilePath
import com.idic.blindbox.R
import com.idic.blindbox.RouterPath
import com.idic.blindbox.databinding.BlindActivityProductDetailBinding
import com.idic.datamoudle.db.entity.NewProductDetail
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.httpmoudle.utils.RxHolder
import com.idic.ordermoudle.response.SQBPayCode
import com.idic.utilmoudle.DKToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.blind_activity_product_detail.*
import java.util.*

@Route(path = RouterPath.DETAIL)
class ProductDetailActivity : BaseFullActivity() {

    @Autowired(name = "sku_code")
    @JvmField
    var sku_code: String? = null
    @Autowired(name = "sku_id")
    @JvmField
    var sku_id: Int = 0

    var productDetail: NewProductDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var contentView = DataBindingUtil.setContentView<BlindActivityProductDetailBinding>(
            this,
            R.layout.blind_activity_product_detail
        )
        contentView.productDetail = NewProductDetail()
        ARouter.getInstance().inject(this)
        initView()
        var map = HashMap<String, String>()
        map.put("sku_code", sku_code!!)
        map.put("id", sku_id.toString())
//        map.put("device_id", "FS1912550188")
        RetrofitUtil.instance.getDefautlRetrofit()
            .getGoodsDetail(map = map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(CustomObserver<Any>().apply {
                _onNext {
                    Log.i("condy", "_onNext")
                    try {
                        var jsonObject = JSON.parseObject(JSON.toJSONString(it))
                        var code = jsonObject.getIntValue("code")
                        if (code == 0) {
                            productDetail = jsonObject.getObject("data", NewProductDetail::class.java)
                            contentView.productDetail = productDetail
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


            })

    }

    override fun onResume() {
        super.onResume()
        bgVideo?.resume()

    }

    override fun onStop() {
        super.onStop()
        bgVideo?.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        bgVideo?.release()
    }

    private fun initView() {

        btnBack.setOnClickListener { finish() }
        btnAliPay.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PAY)
                .withObject("good", productDetail)
                .withInt("type", 0)
                .navigation()
        }
        btnWeChatPay.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PAY)
                .withObject("good", productDetail)
                .withInt("type", 1)
                .navigation()
        }

        bgVideo?.let {
            it.setEnableAudioFocus(false)
            it.release()
            it.setUrl(FilePath.bgVideo)
            it.setLooping(true)
            it.start()
        }
    }
}