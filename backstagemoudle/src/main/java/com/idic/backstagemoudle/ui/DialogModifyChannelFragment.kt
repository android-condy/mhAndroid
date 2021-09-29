package com.idic.backstagemoudle.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.promeg.pinyinhelper.Pinyin
import com.idic.backstagemoudle.BR
import com.idic.backstagemoudle.R
import com.idic.backstagemoudle.data.remote.SpinnerSelectProduct
import com.idic.backstagemoudle.db.entity.AisleConfigEntity
import com.idic.backstagemoudle.ui.adapter.BaseAdapter
import com.idic.basecommon.SPKeys
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.events.SyncProductEvents
import com.idic.basecommon.utils.CustomObserver
import com.idic.datamoudle.db.SourceDataBase
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.httpmoudle.request.UploadProductReq
import com.idic.httpmoudle.response.DKResponse
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.httpmoudle.utils.RxHolder
import com.idic.widgetmoudle.adapter.ItemViewHolder
import kotlinx.android.synthetic.main.dialog_modify_productinfo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 文 件 名: DialogModifyChannelFragment
 * 创 建 人: sineom
 * 创建日期: 2018/11/29 11:57
 * 修改时间：
 * 修改备注：
 */

class DialogModifyChannelFragment : BasicDialogFragment(), TextWatcher {

    companion object {

        const val PRODUCTS = "product"

        fun getInstance(aisleConfigEntity: AisleConfigEntity): DialogModifyChannelFragment {
            var dialogModifyChannelFragment = DialogModifyChannelFragment()
            dialogModifyChannelFragment.modifyProduct = aisleConfigEntity
            return dialogModifyChannelFragment
        }
    }

    private var curSpinnerProduct: SpinnerSelectProduct? = null

//    private var adapter: Adapter? = null

    //所搜匹配的产品
    private var matchProduct = ArrayList<SpinnerSelectProduct>()

    //所有产品
    private val SpProducts = ArrayList<SpinnerSelectProduct>()

    private val delayTag = 0x01

    private val delayTime = 1000L

    private val dimMatching = DimMatching()
    private var popuView: PopupWindow? = null

    private var modifyProduct: AisleConfigEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return inflater.inflate(R.layout.dialog_modify_productinfo, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val parcelable = arguments?.getParcelable<AisleConfigEntity>(PRODUCTS)
//        if (parcelable == null) {
//            dismiss()
//            return
//        }

        dialog_title.setOnClickListener {
            KeyboardUtils.hideSoftInput(it)
        }

        val modifyProducts = (activity as StockManagerActivity).modifyProduct
        if (SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
            var spinnerSelectProduct = SpinnerSelectProduct()
            spinnerSelectProduct.productId = modifyProduct!!.productId
            spinnerSelectProduct.productName = modifyProduct!!.productName
            spinnerSelectProduct.productPrice = modifyProduct!!.productPrice
            modifyProducts.clear()
            modifyProducts.add(spinnerSelectProduct)
        }
        if (modifyProducts.isEmpty()) {
            dismiss()
            return
        }

        val name = modifyProduct!!.productName
        SpProducts.clear()
        matchProduct.clear()
        SpProducts.addAll(modifyProducts)
        val spinnerSelectProduct = SpinnerSelectProduct()
        val productId = modifyProduct!!.productId
        spinnerSelectProduct.productName = name
        spinnerSelectProduct.productId = productId
        if (StringUtils.isEmpty(name)) {
            curSpinnerProduct = SpProducts.firstOrNull()
        } else {
            SpProducts.filter { it.productId == productId }.forEach { curSpinnerProduct = it }
            edInputName.hint = name
        }

        edInputName.addTextChangedListener(this)

        val row = modifyProduct!!.rowSeial.toInt()
        val column = modifyProduct!!.columnSeial.toInt()
        dialog_channelNum.text = "货道编号: ${(row - 1) * 10 + column}"
        val status = if (modifyProduct!!.status == "1") "正常" else "异常"
        dialog_channelStatus.text = getString(R.string.dialog_modify_chanelStatus, status)
        dialog_channelRow.text =
            getString(R.string.dialog_modify_chanelRow, modifyProduct!!.goodsWay)
        dialog_channelPrice_ed.setText(modifyProduct!!.productPrice)
        dialog_channelCount_ed.setText("${modifyProduct!!.stock}")
        dialog_channelLock_ed.setText("${modifyProduct!!.lockStock}")
        dialog_channelCapac_ed.setText("${modifyProduct!!.capacity}")
        btnCancel.setOnClickListener {
            dismiss = null
            dismiss()
        }
        btnSubmit.setOnClickListener { it ->
            KeyboardUtils.hideSoftInput(it)
            if (curSpinnerProduct == null) {
                ToastUtils.showShort("请选择商品")
                return@setOnClickListener
            }
            val editCount = dialog_channelCount_ed.text.toString()
            val editLock = dialog_channelLock_ed.text.toString()
            val editCapac = dialog_channelCapac_ed.text.toString()
            val price = dialog_channelPrice_ed.text.toString()
            val split = price.split(".")

            if (editCount.isEmpty() || editLock.isEmpty() || editCapac.isEmpty()) {
                ToastUtils.showShort("请输入正确的数量")
                return@setOnClickListener
            }
            val length = split.getOrNull(1)?.length ?: 0

            if (price.isNullOrEmpty() || split.size > 2 || length > 2) {
                ToastUtils.showShort("请输入正确的价格,精确到分")
                return@setOnClickListener
            }


            modifyProduct!!.capacity = editCapac.toInt()
            modifyProduct!!.tempCapacity = editCapac.toInt()
            modifyProduct!!.lockStock = editLock.toInt()
            modifyProduct!!.tempLockStock = editLock.toInt()
            modifyProduct!!.stock = editCount.toInt()
            modifyProduct!!.tempStock = editCount.toInt()
            if (modifyProduct!!.capacity < modifyProduct!!.stock) {
                ToastUtils.showShort("库存不能大于货道容量")
                return@setOnClickListener
            }
            modifySubmit_spin_kit.visibility = View.VISIBLE

            if (!SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
                RetrofitUtil.instance.getDefautlRetrofit()
                    .modifyChannelInfo(
                        HttpUtils.createBody(
                            UploadProductReq(
                                editLock,
                                "${curSpinnerProduct?.productId}",
                                editCount,
                                modifyProduct!!.wayId,
                                price,
                                editCapac
                            )
                        )
                    ).compose(RxHolder.IO2Main())
                    .filter {
                        val success = it.isSuccess()
                        if (!success) ToastUtils.showShort("修改失败")
                        success
                    }.subscribe(CustomObserver<DKResponse>().apply {
                        _onError {
                            ToastUtils.showShort("修改失败")
                        }
                        _onNext {
                            ToastUtils.showShort("修改成功")
                            modifySubmit_spin_kit.visibility = View.INVISIBLE
                            RxRelay.instance.post(SyncProductEvents())
                            dismiss()
                        }
                    })
            } else {
                GlobalScope.launch {
                    var productEntity = ProductEntity()
                    with(productEntity) {
                        number = modifyProduct!!.channelNumber
                        productName = modifyProduct!!.productName
                        lockStock = modifyProduct!!.lockStock
                        this.productId = modifyProduct!!.productId
                        stock = modifyProduct!!.stock
                        deviceWayId = modifyProduct!!.wayId
                        productPrice = price.toFloat()
                        capacity = modifyProduct!!.capacity
                        categoryId = "1"
                    }
                    SourceDataBase.getInstance(activity!!.application).productDao()
                        .updateProduct(productEntity)
                    launch(Dispatchers.Main) {
                        ToastUtils.showShort("修改成功")
                        modifySubmit_spin_kit.visibility = View.INVISIBLE
                        RxRelay.instance.post(SyncProductEvents())
                        dismiss()
                    }
                }
            }
        }
        ivShowPopu.setOnClickListener { view1 ->
            showPopu(SpProducts)
        }
    }

    override fun afterTextChanged(s: Editable) {
        matchProduct.clear()
        dimMatching.removeMessages(delayTag)
        val string = s.toString()
        if (StringUtils.isEmpty(string)) {
            return
        }
        val obtainMessage = dimMatching.obtainMessage()
        obtainMessage.what = delayTag
        obtainMessage.obj = Pinyin.toPinyin(string, "")
        dimMatching.sendMessageDelayed(obtainMessage, delayTime)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }


    private fun showPopu(data: List<SpinnerSelectProduct>) {
        if (data.isEmpty()) return
        popuView?.dismiss()
        KeyboardUtils.hideSoftInput(edInputName)
        ivShowPopu.rotation = 180F
        val view = View.inflate(activity, R.layout.dialog_pop_changeprd, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvContentView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.adapter =
            object : BaseAdapter<SpinnerSelectProduct>(
                R.layout.item_modify_prd,
                BR.channelPrd,
                ArrayList(data)
            ) {
                override fun convert(
                    holder: ItemViewHolder,
                    position: Int,
                    t: SpinnerSelectProduct
                ) {
                    holder.setBinding(variableId, t)
                    holder.itemView.findViewById<TextView>(R.id.tvSpName).setOnClickListener {
                        curSpinnerProduct = t
                        popuView?.dismiss()
                        val productName = t.productName
                        edInputName.setText("")
                        edInputName.hint = productName
                    }
                }
            }

        popuView = PopupWindow(activity)
        popuView?.let {
            it.width = activity!!.resources.getDimension(R.dimen.x700).toInt()
            it.height = activity!!.resources.getDimension(R.dimen.y900).toInt()
            it.contentView = view
            it.isOutsideTouchable = true
            it.showAtLocation(edInputName, Gravity.TOP, 0, 0)
            it.setOnDismissListener {
                ivShowPopu.rotation = 0F
            }
        }
    }

    override fun onStop() {
        super.onStop()
        popuView?.dismiss()
        edInputName.removeTextChangedListener(this)
        dimMatching.removeMessages(delayTag)
        dismiss()
    }

    @SuppressLint("HandlerLeak")
    inner class DimMatching : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                delayTag -> {
                    val msg = msg.obj as String
                    matchProduct.clear()
                    SpProducts.filterTo(matchProduct) {
                        it.pinyin.startsWith(msg, true) || it.abbreviations.startsWith(
                            msg,
                            true
                        )
                    }
                    showPopu(matchProduct)
                }
                else -> {
                }
            }
        }
    }

}
