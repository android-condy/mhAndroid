package com.idic.backstagemoudle.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.arialyy.annotations.Upload
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.idic.backstagemoudle.R
import com.idic.backstagemoudle.db.entity.InitData
import com.idic.backstagemoudle.db.entity.UploadPrd
import com.idic.basecommon.SPKeys
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.events.SyncProductResult
import com.idic.datamoudle.db.SourceDataBase
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.utilmoudle.AppUtil
import com.idic.utilmoudle.DKToastUtils
import com.idic.widgetmoudle.dialog.LoadingDialog
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_device_active.*
import kotlinx.android.synthetic.main.back_start_toolbar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class DeviceActiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_active)
        tvTitle.text = getString(R.string.deviceActive)
        tvSave.visibility = View.GONE
        tvBack.setOnClickListener { finish() }
        btnActive.setOnClickListener {
            var code = etCode.text.toString()

            if (code.isNullOrEmpty()) {
                DKToastUtils.showCustomLong("请输入激活码")
                return@setOnClickListener
            }
            startActivity(Intent(this@DeviceActiveActivity, LoadingDialog::class.java))
            GlobalScope.launch {
                var allProducts =
                    SourceDataBase.getInstance(application).productDao().getAllProducts()
                val datas = ArrayList<UploadPrd>()
                allProducts.forEach {
                    var uploadPrd = UploadPrd()
                    uploadPrd.productName = it.productName
                    uploadPrd.price = "${(it.productPrice * 100).toInt()}"
                    uploadPrd.stock = "${it.stock}"
                    uploadPrd.capacity = "${it.capacity}"
                    uploadPrd.sort = "${it.number}"
                    datas.add(uploadPrd)
                }
                val initData = InitData().apply {
                    key = code
                    productList = datas
                }
                RetrofitUtil.instance.getDefautlRetrofit().initProduct(
                    HttpUtils.createBody(initData)
                ).filter {
                    it.isSuccess()
                }.subscribeBy({

                }, {
                    RxRelay.instance.post(SyncProductResult(true))
                }, {
                    if (it.isSuccess()) {
                        SPUtils.getInstance().put(SPKeys.DEVICES_KEY, code, true)
                        AppUtil.relaunch()
                    }
                })
            }

        }
    }

}
