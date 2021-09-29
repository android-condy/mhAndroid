package com.idic.backstagemoudle.ui

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.idic.backstagemoudle.R
import com.idic.backstagemoudle.data.remote.SpinnerSelectProduct
import com.idic.backstagemoudle.databinding.ActivityStockManagerBinding
import com.idic.backstagemoudle.db.entity.AislesBindGoodsEntity
import com.idic.backstagemoudle.ui.adapter.BindingAdapter
import com.idic.backstagemoudle.ui.adapter.StockAdapter
import com.idic.backstagemoudle.ui.adapter.StockAisleAdapter
import com.idic.backstagemoudle.util.DialogUtil
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.ui.RecyclerViewNoBugLinearLayoutManager
import com.idic.basecommon.utils.CustomObserver
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.httpmoudle.utils.RxHolder
import com.idic.utilmoudle.DKToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_stock_manager.*
import kotlinx.android.synthetic.main.back_start_toolbar.*
import kotlinx.android.synthetic.main.dialog_view.view.*

//输入不同的项目将editText重置
private val changeTag = "null"

class StockManagerActivity : BaseFullActivity(),
    StockManagerModifyCallback {
    override fun save(view: View, aisleEntity: AislesBindGoodsEntity) {
        val gson = Gson()
        val jsonArray = JsonArray()
        val jsonObject = JsonObject()
        jsonObject.addProperty("aisles", aisleEntity.aisles)
        jsonObject.addProperty("stock", aisleEntity.stock)
        jsonObject.addProperty("volume", aisleEntity.volume)
        jsonArray.add(jsonObject)
        var pro_list = gson.toJson(jsonArray)
        Log.i("condy", "=====" + pro_list)
        RetrofitUtil.instance.getDefautlRetrofit()
            .getUpdataAislesGoods(user_id = DialogUtil.getUserId(this@StockManagerActivity), pro_list = pro_list)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(CustomObserver<Any>().apply {
                _onNext {
                    Log.i("condy", "_onNext")
                    var jsonObject = JSON.parseObject(JSON.toJSONString(it))
                    var code = jsonObject.getIntValue("code")
                    if (code == 0) {
                        DKToastUtils.showCustomShort("保存成功")
                    } else {
                        DKToastUtils.showCustomShort(jsonObject.getString("msg"))
                    }
                }
                _onError {
                    DKToastUtils.showCustomShort(it.message)
                }


            })
    }

    override fun onCountClick(view: View, aisleEntity: AislesBindGoodsEntity) {
        val ab = AppCompatDialog(this@StockManagerActivity)
        ab.setCancelable(true)
        var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_view, null)
        view.tv_title.text = "第" + aisleEntity.egg_code_x + "行，第" + aisleEntity.egg_code_y + "列"
        view.et_count.setText(aisleEntity.volume.toString())
        view.et_count.setSelection(view.et_count.text.toString().length)
        view.save.setOnClickListener {
            ab.dismiss()
            aisleEntity.volume = view.et_count.text.toString().toInt()
            stockAdapter?.notifyDataSetChanged()
        }
        ab.setContentView(view)
        ab.show()
    }

    override fun add(view: View, aisleEntity: AislesBindGoodsEntity) {
        aisleEntity.volume++
        stockAdapter?.notifyDataSetChanged()
    }

    override fun sub(view: View, aisleEntity: AislesBindGoodsEntity) {
        if (aisleEntity.volume > 0) {
            aisleEntity.volume--
        }
        stockAdapter?.notifyDataSetChanged()
    }

    private lateinit var binding: ActivityStockManagerBinding
    private val rowDatas = ArrayList<HashMap<String, List<AislesBindGoodsEntity>>>()
    private var stockAdapter: StockAdapter? = null
    private var aislesProducts = ArrayList<AislesBindGoodsEntity>()
    val modifyProduct = ArrayList<SpinnerSelectProduct>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_stock_manager
        )
        binding.isLoading = true
//        var list = ArrayList<AislesBindGoodsEntity>()
//        list.add(AislesBindGoodsEntity(1, stock = 1, egg_code_x = 1, egg_code_y = 1, id = 1))
//        var map = HashMap<String, List<AislesBindGoodsEntity>>()
//        map.put("1", list)
//        rowDatas.add(map)
        initView()
    }


    override fun onStop() {
        super.onStop()
//        modifyProducts.clear()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        tvTitle.text = getString(R.string.stockManager)
        val dimension = resources.getDimension(R.dimen.x8).toInt()
        val layoutManager = RecyclerViewNoBugLinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvStockManager.layoutManager = layoutManager
        rvStockManager.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
//        rvStockManager.addItemDecoration(
//                SpaceItemDecoration(
//                    dimension,
//                    dimension,
//                    dimension,
//                    dimension
//                )
//

        stockAdapter = StockAdapter(
            R.layout.item_stock_father1,
            0,
            rowDatas
        ) { holder, datas ->
            val key = datas.keys.first()
            val data = datas[key]!!
            val recyclerView1 =
                holder.itemView.findViewById<RecyclerView>(R.id.rvStockManagerChild)//第1行，共10个产品
            val stockAisleAdapter =
                StockAisleAdapter(R.layout.item_stock_control, 0, data) { holderItem, item ->
                    holderItem.itemView.findViewById<TextView>(R.id.tvChannelNum)
                        .text = "第" + item.egg_code_x + "行，第" + item.egg_code_y + "列"
                    holderItem.itemView.findViewById<TextView>(R.id.tvProductName).text = item.sku_name
                    holderItem.itemView.findViewById<TextView>(R.id.editCapacityCount).text =
                            item.volume.toString()
                    holderItem.itemView.findViewById<TextView>(R.id.editSurplusCount).text =
                            item.stock.toString()
                    Log.i("condy", "item.img_url=" + item.image_url)
                    if (!item.image_url.isNullOrEmpty()) {
                        BindingAdapter.img(holderItem.itemView.findViewById<ImageView>(R.id.image), item.image_url)
                    }
                    holderItem.itemView.findViewById<Button>(R.id.btnCapacityAdd).setOnClickListener {
                        this@StockManagerActivity.add(it, item)
                    }
                    holderItem.itemView.findViewById<TextView>(R.id.editCapacityCount).setOnClickListener {
                        this@StockManagerActivity.onCountClick(it, item)
                    }
                    holderItem.itemView.findViewById<Button>(R.id.btnCapacitySub).setOnClickListener {
                        this@StockManagerActivity.sub(it, item)
                    }
                    holderItem.itemView.findViewById<Button>(R.id.btnProductDetail).setOnClickListener {
                        this@StockManagerActivity.save(it, item)
                    }
                    holderItem.itemView.findViewById<Button>(R.id.btnSurplusAdd).setOnClickListener {
                        item.stock++
                        stockAdapter?.notifyDataSetChanged()
                    }
                    holderItem.itemView.findViewById<Button>(R.id.btnSurpluskSub).setOnClickListener {
                        if (item.stock > 0) {
                            item.stock--
                        }
                        stockAdapter?.notifyDataSetChanged()
                    }
                    holderItem.itemView.findViewById<TextView>(R.id.editSurplusCount).setOnClickListener {
                        val ab = AppCompatDialog(this@StockManagerActivity)
                        ab.setCancelable(true)
                        var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val view = inflater.inflate(R.layout.dialog_view, null)
                        view.tv_title.text = "第" + item.egg_code_x + "行，第" + item.egg_code_y + "列"
                        view.et_count.setText(item.stock.toString())
                        view.et_count.setSelection(view.et_count.text.toString().length)
                        view.save.setOnClickListener {
                            ab.dismiss()
                            item.stock = view.et_count.text.toString().toInt()
                            stockAdapter?.notifyDataSetChanged()
                        }
                        ab.setContentView(view)
                        ab.show()
                    }

                }
            recyclerView1.adapter = stockAisleAdapter
            val layoutManager = RecyclerViewNoBugLinearLayoutManager(this@StockManagerActivity)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            recyclerView1.layoutManager = layoutManager
//                    recyclerView1.addItemDecoration(
//                        SpaceItemDecoration(
//                            dimension,
//                            10,
//                            0,
//                            0
//                        )
//                    )
        }
        rvStockManager.adapter = stockAdapter
//                        GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView1)

//                        recyclerView1.adapter = adapter
//                        weakAdapter[key] = adapter
//                    } else {
//                        recyclerView1.adapter = adapter
//                    }
//                }

//        }

//        btnRowSub.setOnClickListener {
//            val row = tvRow.text.toString().toInt() - 1
//            if (row < 1) return@setOnClickListener
//            tvRow.text = "${row - 1}"
//        }
//
//        btnRowAdd.setOnClickListener {
//            val row = tvRow.text.toString().toInt() + 1
//            tvRow.text = "$row"
//        }
//        btnRowFillUp.setOnClickListener {
//            if (binding.isLoading) return@setOnClickListener
//            startActivity(Intent(this@StockManagerActivity, LoadingDialog::class.java))
////            binding.isLoading = true
//            stockRepository.rowFillUp(tvRow.text.toString(), "${selectContainer?.containerId}")
//                .subscribe(CustomObserver<DKResponse>().apply {
//                    _onNext {
//                        if (it.isSuccess()) {
//                            DKToastUtils.showCustomShort(getString(R.string.modify_success))
//                        } else {
//                            DKToastUtils.showCustomShort(getString(R.string.modify_fail))
//                        }
//                        RxRelay.instance.post(SyncProductEvents())
//                        updateAisles(selectContainer!!)
//                    }
//
//                    _onError {
//                        DKToastUtils.showCustomShort(getString(R.string.modify_fail))
//                        binding.isLoading = false
//                    }
//                })
//        }

//        btnAllFillUp.setOnClickListener {
//            if (binding.isLoading) return@setOnClickListener
//            startActivity(Intent(this@StockManagerActivity, LoadingDialog::class.java))
////            binding.isLoading = true
//            stockRepository.allFillUp()
//                .subscribe(CustomObserver<DKResponse>().apply {
//                    _onNext {
//                        if (it.isSuccess()) {
//                            DKToastUtils.showCustomShort(getString(R.string.modify_success))
//                        } else {
//                            DKToastUtils.showCustomShort(getString(R.string.modify_fail))
//                        }
//                        RxRelay.instance.post(SyncProductEvents())
//                        updateAisles(selectContainer!!)
//                    }
//
//                    _onError {
//                        DKToastUtils.showCustomShort(getString(R.string.modify_fail))
//                        binding.isLoading = false
//                    }
//                })
//        }

        tvSave.setOnClickListener {
            //            if (binding.isLoading) return@setOnClickListener
//            editInput?.let {
//                it.setText("")
//                it.isFocusable = false
//                it.isFocusableInTouchMode = false
//                it.clearFocus()
//                KeyboardUtils.hideSoftInput(it)
//
//            }
//            curTvCount?.isSelected = false
            save()
//            saveModify()
        }
        tvBack.setOnClickListener {
            //            if (modifyProducts.isNotEmpty()) {
//                DialogUtil.createDialog(this,
//                    {
//                        saveModify()
//                        finish()
//                    }, {
//                        finish()
//                    })?.show()
//            } else {
            finish()
//            }
        }
//        editInput?.addTextChangedListener(textWatcher)
//
//        rvStockManager?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (dx > 0 || dy > 0) {
//                    editInput?.let {
//                        it.setText("")
//                        it.isFocusable = false
//                        it.isFocusableInTouchMode = false
//                        it.clearFocus()
//                        KeyboardUtils.hideSoftInput(it)
//
//                    }
//                    curTvCount?.setBackgroundResource(R.drawable.stockmanager_count_normal)
//                }
//            }
//        })
//        if (!SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
//            //已绑定
//            RetrofitUtil.instance.getDefautlRetrofit()
//                .loadSpinnerProduct(HttpUtils.createBody("{\"deviceId\":\"${DeviceInfo.getInstance().deviceId}\"}"))
//                .filter {
//                    val success = it.isSuccess() && it.data != null
//                    if (!success) ToastUtils.showShort("获取产品信息失败")
//                    success
//                }
//                .compose(RxHolder.getResponseListData<SpinnerSelectProduct>())
//                .map { data ->
//                    modifyProduct.clear()
//                    data.forEach { it ->
//                        val pinyinBuf = StringBuffer()
//                        val abbreviationsBuf = StringBuffer()
//                        val productName = it.productName
//                        productName.forEach {
//                            val toPinyin = Pinyin.toPinyin(it.toString(), "")
//                            pinyinBuf.append(toPinyin)
//                            abbreviationsBuf.append(toPinyin.first())
//                        }
//                        it.pinyin = pinyinBuf.toString()
//                        it.abbreviations = abbreviationsBuf.toString()
//                    }
//                    modifyProduct.addAll(data)
//                    data
//                }.compose(RxHolder.IO2Main())
//                .subscribe(CustomObserver<List<SpinnerSelectProduct>>().apply {
//                    _onNext {
//                        stockRepository.getAllContainer(this@StockManagerActivity)
//                    }
//                    _onError {
//                        ToastUtils.showShort("获取产品信息失败")
//                    }
//                })
//        } else {
//            stockRepository.getAllContainer(this@StockManagerActivity)
//        }

        RetrofitUtil.instance.getDefautlRetrofit()
            .getAislesGoods()
            .subscribeOn(Schedulers.io())
            .filter {
                var jsonObject = JSON.parseObject(JSON.toJSONString(it))
                var status = (jsonObject.getIntValue("code") == 0) && !(jsonObject.getString("data").isNullOrEmpty())
                if (status) {

                } else {
//                    runOnUiThread {
//                        binding.isLoading = false
//                        DKToastUtils.showCustomShort("无数据")
//                    }
                }
                status
            }.compose(RxHolder.getResponseListData2<AislesBindGoodsEntity>())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(CustomObserver<ArrayList<AislesBindGoodsEntity>>().apply {
                _onNext {
                    Log.i("condy", "_onNext" + it.toString())
                    if (!it.isEmpty()) {
                        binding.isLoading = false
                        aislesProducts = it
                        var map: HashMap<String, List<AislesBindGoodsEntity>>? =
                            null
                        var x = 0
                        var list: ArrayList<AislesBindGoodsEntity>? = null
                        aislesProducts.forEach {

                            if (x == it.egg_code_x) {
                                list?.add(it)
                            } else {
                                x = it.egg_code_x
                                map = HashMap<String, List<AislesBindGoodsEntity>>()
                                rowDatas.add(map!!)
                                list = ArrayList<AislesBindGoodsEntity>()
                                map?.put(x.toString(), list!!)
                                list?.add(it)
                            }
                        }
                        (rvStockManager.adapter as StockAdapter).setData(rowDatas)
                    }
                }
                _onError {
                    DKToastUtils.showCustomShort(it.message)
                }


            })
    }

//    private fun saveModify() {
//        var failCount = 0
//        GlobalScope.launch {
//            DKToastUtils.showCustomLong("正在保存同步数据中,请稍等")
//            if (!SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
//                startActivity(Intent(this@StockManagerActivity, LoadingDialog::class.java))
//            }
//            modifyProducts.filter {
//                val b = it.tempCapacity < it.tempStock
//                if (b) {
//                    DKToastUtils.showCustomLong("库存不能大于货道容量")
//                }
//                !b
//            }.forEach { aisleEntity ->
//                if (SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
//                    var productEntity = ProductEntity()
//                    with(productEntity) {
//                        number = 1
//                        productName = aisleEntity.productName
//                        lockStock = aisleEntity.tempLockStock
//                        productId = aisleEntity.productId
//                        stock = aisleEntity.tempStock
//                        deviceWayId = aisleEntity.wayId
//                        productPrice = aisleEntity.productPrice.toFloat()
//                        capacity = aisleEntity.tempCapacity
//                        categoryId = "1"
//                    }
//                    SourceDataBase.getInstance(application).productDao()
//                        .updateProduct(productEntity)
//                } else {
//                    var complete = false
//                    stockRepository.uploadStock(aisleEntity)
//                        .subscribe(CustomObserver<DKResponse>().apply {
//                            _onNext {
//                                if (!it.isSuccess()) {
//                                    ++failCount
//                                }
//                                complete = true
//                            }
//                            _onError {
//                                ++failCount
//                                complete = true
//                            }
//                        })
//
//                    while (!complete) {
//                        delay(10)
//                    }
//                }
//            }
//            if (failCount > 0) {
//                DKToastUtils.showCustomShort("$failCount 个修改失败")
//            }
//
//            RxRelay.instance.post(SyncProductEvents())
//            runOnUiThread {
//                updateAisles(selectContainer!!)
//            }
//        }
//    }

//    override fun onContainers(containerEntity: List<ContainerConfigEntity>) {
//        (rvContainer?.adapter as? ContainerAdapter)?.setData(containerEntity)
//        updateAisles(containerEntity.first())
//    }
//
//    override fun onContainersNotAvailable() {
//        binding.apply {
//            isLoading = false
//            isEmpty = true
//        }
//
//    }
//
//    override fun onProductsLoad(products: List<AisleConfigEntity>) {
//        runOnUiThread {
//            try {
//                rowDatas.clear()
//                weakAdapter.clear()
//                rvStockManager.removeAllViews()
//
//                val row = selectContainer!!.rows.toInt()
//                // TODO: 2019-09-05 优化循环
//                (1..row).forEach {
//                    val datas = HashMap<String, List<AisleConfigEntity>>()
//                    val childData = ArrayList<AisleConfigEntity>()
//                    products.forEach { aisle ->
//                        aisle.tempCapacity = aisle.capacity
//                        aisle.tempLockStock = aisle.lockStock
//                        aisle.tempStock = aisle.stock
//                        if (aisle.rowSeial == "$it") {
//                            childData.add(aisle)
//                        }
//                    }
//                    if (childData.isNotEmpty()) {
//                        datas["$it"] = childData
//                        rowDatas.add(datas)
//                    }
//                }
//                (rvStockManager?.adapter as? StockAdapter)?.setData(rowDatas)
//                modifyProducts.clear()
//                binding.apply {
//                    isLoading = false
//                    isEmpty = false
//                }
//            } catch (e: Exception) {
//                XLog.e("货柜数据加载失败", e)
//                binding.apply {
//                    isLoading = false
//                    isEmpty = true
//                }
//            }
//        }
//    }
//
//    override fun onProductNotAvailable() {
//        binding.apply {
//            isLoading = false
//            isEmpty = true
//        }
//        (rvStockManager?.adapter as? AisleAdapter)?.setData(ArrayList())
//    }
//
//    override fun onClick(view: View, containerEntity: ContainerConfigEntity) {
//        if (selectContainer == containerEntity) return
//
//        if (modifyProducts.isNotEmpty()) {
//            DialogUtil.createDialog(this,
//                {
//                    saveModify()
//                    updateAisles(containerEntity)
//                }, {
//                    updateAisles(containerEntity)
//                })?.show()
//        } else {
//            updateAisles(containerEntity)
//        }
//
//
//    }
//
//    override fun onScreenClick(view: View, containerEntity: ContainerConfigEntity) {
//    }
//
//    override fun onLayerOpenClick(view: View, containerEntity: ContainerConfigEntity) {
//    }
//
//    private fun updateAisles(containerEntity: ContainerConfigEntity) {
//        selectContainer = containerEntity
//        modifyProducts.clear()
//        tvSave.text = getString(R.string.save, modifyProducts.size)
//        stockRepository.getProductsForContainer(selectContainer!!.containerId, this)
//        rvContainer?.adapter?.notifyDataSetChanged()
//        binding.apply {
//            isLoading = true
//            isEmpty = false
//        }
//    }

    //增加按钮的点击事件
//    override fun add(view: View, aisleEntity: AisleConfigEntity) {
//        val parent = (view.parent.parent.parent as View)
//        when (view.id) {
//            R.id.btnCapacityAdd -> {
//                val editText =
//                    (view.parent as View).findViewById<TextView>(R.id.editCapacityCount)
//                var strCount = editText.text.toString()
//                val count = try {
//                    strCount.toInt() + 1
//                } catch (e: Exception) {
//                    aisleEntity.tempCapacity
//                }
//                aisleEntity.tempCapacity = count
//                editText.setText("$count")
//                parent.isActivated = isModify(aisleEntity)
//            }
//            R.id.btnLockStockAdd -> {
//                val editText =
//                    (view.parent as View).findViewById<TextView>(R.id.editLockStockCount)
//                var strCount = editText.text.toString()
//                if (strCount.isEmpty()) {
//                    strCount = editText.hint.toString()
//                }
//                var count = strCount.toInt() + 1
//                if (count > aisleEntity.tempCapacity) {
//                    count = aisleEntity.tempCapacity
//                }
//                aisleEntity.tempLockStock = count
//                editText.setText("$count")
//                parent.isActivated = isModify(aisleEntity)
//            }
//            R.id.btnStockAdd -> {
//                val editText =
//                    (view.parent as View).findViewById<TextView>(R.id.editStockCount)
//                var strCount = editText.text.toString()
//                if (strCount.isEmpty()) {
//                    strCount = editText.hint.toString()
//                }
//                var count = strCount.toInt() + 1
//                if (count > aisleEntity.tempCapacity) {
//                    count = aisleEntity.tempCapacity
//                }
//                aisleEntity.tempStock = count
//                editText.setText("$count")
//                parent.isActivated = isModify(aisleEntity)
//            }
//        }
//        updateModifyData(parent, aisleEntity)
//    }

//    //减少按钮的点击事件
//    override fun sub(view: View, aisleEntity: AisleConfigEntity) {
//        val parent = (view.parent.parent.parent as View)
//        when (view.id) {
//            R.id.btnCapacitySub -> {
//                val editText =
//                    (view.parent as View).findViewById<TextView>(R.id.editCapacityCount)
//                var strCount = editText.text.toString()
//                var count = strCount.toInt() - 1
//                if (count < 1) count = 0
//                aisleEntity.tempCapacity = count
//                editText.setText("$count")
//            }
//            R.id.btnLockStockSub -> {
//                val editText =
//                    (view.parent as View).findViewById<TextView>(R.id.editLockStockCount)
//                var strCount = editText.text.toString()
//                if (strCount.isEmpty()) {
//                    strCount = editText.hint.toString()
//                }
//                var count = strCount.toInt() - 1
//                if (count < 1) count = 0
//                aisleEntity.tempLockStock = count
//                editText.setText("$count")
//            }
//            R.id.btnStockSub -> {
//                val editText =
//                    (view.parent as View).findViewById<TextView>(R.id.editStockCount)
//                var strCount = editText.text.toString()
//                if (strCount.isEmpty()) {
//                    strCount = editText.hint.toString()
//                }
//                var count = strCount.toInt() - 1
//                if (count < 1) count = 0
//                aisleEntity.tempStock = count
//                editText.setText("$count")
//
//            }
//        }
//        parent.isActivated = isModify(aisleEntity)
//        updateModifyData(parent, aisleEntity)
//    }

//    override fun onCountClick(view: View, aisleEntity: AisleConfigEntity) {
//        val group = (view.parent.parent as View)
//        group.findViewById<TextView>(R.id.editCapacityCount)
//            .setBackgroundResource(R.drawable.stockmanager_count_normal)
//        group.findViewById<TextView>(R.id.editLockStockCount)
//            .setBackgroundResource(R.drawable.stockmanager_count_normal)
//        group.findViewById<TextView>(R.id.editStockCount)
//            .setBackgroundResource(R.drawable.stockmanager_count_normal)
//        view.setBackgroundResource(R.drawable.stockmanager_count_select)
//        view as TextView
//        curTvCount = view
//        curAisle = aisleEntity
//        editInput?.let {
//            it.setText("")
//            it.isFocusable = true
//            it.isFocusableInTouchMode = true
//            it.requestFocus()
//            KeyboardUtils.showSoftInput(it)
//        }
//    }

    override fun productDetail(view: View, aisleEntity: AislesBindGoodsEntity) {
//        val dialogModifyChannelFragment = DialogModifyChannelFragment.getInstance(aisleEntity)
////        val bundle = Bundle()
////        bundle.putParcelable(DialogModifyChannelFragment.PRODUCTS, aisleEntity)
////        dialogModifyChannelFragment.arguments = bundle
//        val position =
//            rvStockManager.getChildAdapterPosition(view.parent.parent.parent.parent.parent as View)
//        dialogModifyChannelFragment.setDismissListener {
//            runOnUiThread {
//                rvStockManager.smoothScrollToPosition(position)
//                var childView =
//                    rvStockManager.findViewHolderForAdapterPosition(position)?.itemView as? ConstraintLayout
//                if (null == childView) {
//                    return@runOnUiThread
//                }
//                (0..childView.childCount).filter {
//                    childView.getChildAt(it) is RecyclerView
//                }.forEach {
//                    var recyclerView = childView.getChildAt(it) as RecyclerView
//                    var childAdapterPosition =
//                        recyclerView.getChildAdapterPosition(view.parent.parent.parent as View)
//                    recyclerView.adapter?.notifyItemChanged(childAdapterPosition)
//                }
//            }
//        }
//        dialogModifyChannelFragment.show(supportFragmentManager, "")
    }

    //更新修改的集合和计时器
//    private fun updateModifyData(
//        parent: View,
//        aisleEntity: AisleConfigEntity
//    ) {
//        if (parent.isActivated) {
//            if (!modifyProducts.contains(aisleEntity)) {
//                modifyProducts.add(aisleEntity)
//            }
//        } else {
//            modifyProducts.remove(aisleEntity)
//        }
//        tvSave.text = getString(R.string.save, modifyProducts.size)
//    }
//
//
//    private fun isModify(aisleEntity: AisleConfigEntity): Boolean {
//        return aisleEntity.tempCapacity != aisleEntity.capacity ||
//                aisleEntity.tempLockStock != aisleEntity.lockStock ||
//                aisleEntity.tempStock != aisleEntity.stock
//    }
//
//    //用于接收键盘输入的
//    private val textWatcher = object : TextWatcher {
//        override fun afterTextChanged(s: Editable?) {
//            s?.let { editable ->
//                val inputText = editable.toString()
//                if (inputText.isEmpty()) return@let
//                var count = inputText.toInt()
//                curTvCount?.let {
//                    val parent = (it.parent.parent.parent as View)
//                    when (it.id) {
//                        R.id.editCapacityCount -> {
//                            curAisle!!.tempCapacity = count
//                        }
//                        R.id.editLockStockCount -> {
//
//                            if (count > curAisle!!.tempCapacity) {
//                                count = curAisle!!.tempCapacity
//                            }
//                            curAisle!!.tempLockStock = count
//                        }
//                        R.id.editStockCount -> {
//                            if (count > curAisle!!.tempCapacity) {
//                                count = curAisle!!.tempCapacity
//                            }
//                            curAisle!!.tempStock = count
//                        }
//                    }
//                    curTvCount?.text = "$count"
//                    parent.isActivated = isModify(curAisle!!)
//                    updateModifyData(parent, curAisle!!)
//                }
//            }
//        }
//
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//        }
//
//    }
    private fun save() {
        val gson = Gson()
        val jsonArray = JsonArray()
        aislesProducts.forEach {
            val jsonObject = JsonObject()
            jsonObject.addProperty("aisles", it.aisles)
            jsonObject.addProperty("stock", it.stock)
            jsonObject.addProperty("volume", it.volume)
            jsonArray.add(jsonObject)
        }
        var pro_list = gson.toJson(jsonArray)
        Log.i("condy", "=====" + pro_list)
        RetrofitUtil.instance.getDefautlRetrofit()
            .getUpdataAislesGoods(user_id = DialogUtil.getUserId(this@StockManagerActivity), pro_list = pro_list)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(CustomObserver<Any>().apply {
                _onNext {
                    Log.i("condy", "_onNext")
                    var jsonObject = JSON.parseObject(JSON.toJSONString(it))
                    var code = jsonObject.getIntValue("code")
                    if (code == 0) {
                        DKToastUtils.showCustomShort("保存成功")
                    } else {
                        DKToastUtils.showCustomShort(jsonObject.getString("msg"))
                    }
                }
                _onError {
                    DKToastUtils.showCustomShort(it.message)
                }


            })
    }


}
