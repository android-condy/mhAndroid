package com.idic.login.ui.activity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.elvishew.xlog.XLog
import com.idic.basecommon.BaseFullActivity
import com.idic.datamoudle.db.SourceDataBase
import com.idic.datamoudle.db.entity.CategoryEntity
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.login.R
import com.idic.utilmoudle.AppUtil
import com.idic.utilmoudle.DKToastUtils
import kotlinx.android.synthetic.main.activity_config.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfigActivity : BaseFullActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        initView()
    }

    private fun initView() {

        val shipmentAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.contianer,
            R.layout.simple_spinner_item
        )
        spinnerContainer.adapter = shipmentAdapter
        tvStep.setOnClickListener {
            KeyboardUtils.hideSoftInput(it)
        }
        btnClean.setOnClickListener {
            GlobalScope.launch {
                DKToastUtils.showCustomLong("开始清空配置")
                SourceDataBase.getInstance(application).productDao().removeAllProducts()
                DKToastUtils.showCustomLong("清空完成")
            }
        }

        btnComplete.setOnClickListener {
            AppUtil.relaunch()
        }
        btnSubmit.setOnClickListener {
            DKToastUtils.showCustomLong("开始加载配置信息")
            val products = ArrayList<ProductEntity>()
            var containerId = spinnerContainer.selectedItem as String
            (2 until table.childCount).forEach {

                val viewGroup = table.getChildAt(it) as ViewGroup
                //层数
                val layerView = viewGroup.getChildAt(0) as TextView
                //货道数量
                val layerCountView = viewGroup.getChildAt(1) as EditText
                //库存
                val stockView = viewGroup.getChildAt(2) as EditText
                //容量
                val capacityView = viewGroup.getChildAt(3) as EditText

                val layer = layerView.text.toString().toInt()

                val layerCount = layerCountView.text.toString().toInt()

                val stock = stockView.text.toString().toInt()

                val capacity = capacityView.text.toString().toInt()

                if (stock > capacity) {
                    return@forEach
                }

                (1..layerCount).forEach { coloumn ->
                    val number = (layer - 1) * 10 + coloumn
                    val productEntity = ProductEntity().apply {
                        this.num = containerId.toInt()
                        this.stock = stock
                        this.capacity = capacity
                        this.number = number
                        this.deviceWayId = "$number"
                        this.productId = "$number"
                        this.productPrice = 0.01F
                        this.productName = "商品$number"
                        this.categoryId = "1"
                        this.productPrice = 0.01F
                        this.goodsWay = "$number"
                        this.localImg = ""
                        this.productImg = ""
                    }
                    products.add(productEntity)
                }
            }
            GlobalScope.launch {
                val categoryEntity =
                    CategoryEntity("测试类目", "test", "test", "test", 1, "1")
                SourceDataBase.getInstance(application).categoryDao()
                    .insertCategories(arrayListOf(categoryEntity))
                SourceDataBase.getInstance(application).productDao().insertProducts(products)
                DKToastUtils.showCustomLong("加载完成")
            }
        }
    }
}