package com.idic.backstagemoudle.boxtest.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.alibaba.fastjson.JSON
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.idic.backstagemoudle.BR
import com.idic.backstagemoudle.R
import com.idic.backstagemoudle.R.id.tv_upload_aisles
import com.idic.backstagemoudle.boxtest.ShipmentManager
import com.idic.blind.Shipment
import com.idic.backstagemoudle.boxtest.adapter.SpaceItemDecoration
import com.idic.backstagemoudle.boxtest.common.faultCodeDelivery
import com.idic.backstagemoudle.boxtest.data.Aisle
import com.idic.backstagemoudle.db.entity.AislesBindGoodsEntity
import com.idic.backstagemoudle.ui.adapter.AisleFatherAdapter
import com.idic.basecommon.ui.RecyclerViewNoBugLinearLayoutManager
import com.idic.basecommon.utils.CustomObserver
import com.idic.httpmoudle.URL
import com.idic.httpmoudle.utils.HttpUtils
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.utilmoudle.DKToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_stock_manager.*
import kotlinx.android.synthetic.main.activity_test.*
import okhttp3.MediaType
import okhttp3.RequestBody
import serialport_idic.MyFunc
import java.math.BigInteger
import java.util.HashMap
import kotlin.collections.ArrayList

/**
 * 测试activity
 */
class TestActivity : AppCompatActivity(), AisleClickListener {

//    @SuppressLint("StaticFieldLeak")
//    private var mDialogLoading: RxDialogLoading? = null

    private val aisles = ArrayList<Aisle>()

    private var order_byte = byteArrayOf()
    private var aisleFatherAdapter: AisleFatherAdapter? = null
    private val rowDatas = ArrayList<HashMap<String, List<Aisle>>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
//                var list = ArrayList<Aisle>()
//        list.add(Aisle(1,1,1))
//        list.add(Aisle(2,1,2))
//        list.add(Aisle(3,1,3))
//        list.add(Aisle(4,1,4))
//        var map1 = HashMap<String, List<Aisle>>()
//        map1.put("1", list)
//        rowDatas.add(map1)
//        var list2 = ArrayList<Aisle>()
//        list2.add(Aisle(5,2,1))
//        list2.add(Aisle(6,2,2))
//        list2.add(Aisle(7,2,3))
//        list2.add(Aisle(8,2,4))
//        list2.add(Aisle(9,2,5))
//        var map2 = HashMap<String, List<Aisle>>()
//        map2.put("2", list2)
//        rowDatas.add(map2)
//        var list3 = ArrayList<Aisle>()
//        list3.add(Aisle(10,3,1))
//        list3.add(Aisle(11,3,2))
//        list3.add(Aisle(12,3,3))
//        var map3 = HashMap<String, List<Aisle>>()
//        map3.put("3", list3)
//        rowDatas.add(map3)
        initView()
        initData()
    }

    //加载数据
    private fun initData() {
        showLoad("正在获取货道数据,请稍候")
        ShipmentManager.setReceiverHandler(receiveHandler)
        //获取产品数量
        ShipmentManager.getAllAisle()
    }

    private fun initView() {
        btnBack.setOnClickListener {
            finish()
        }
        val layoutManager = RecyclerViewNoBugLinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvAisles.layoutManager = layoutManager

//        val gridLayoutManager = GridLayoutManager(this, 10)
//        rvAisles.addItemDecoration(SpaceItemDecoration(20, 20, 0, 0))
//        rvAisles.layoutManager = gridLayoutManager

        aisleFatherAdapter = AisleFatherAdapter(
            R.layout.item_aisle_father,
            0,
            rowDatas
        ) { holder, datas ->
            val key = datas.keys.first()
            val data = datas[key]!!
            val recyclerView1 =
                holder.itemView.findViewById<RecyclerView>(R.id.rvAisleManagerChild)
            val stockAisleAdapter = AisleAdapter() {
                it.setBinding(BR.click, this@TestActivity)
            }
            recyclerView1.adapter = stockAisleAdapter
            val layoutManager = RecyclerViewNoBugLinearLayoutManager(this@TestActivity)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            recyclerView1.layoutManager = layoutManager
            stockAisleAdapter.updateAllData(data)
        }
        rvAisles.adapter = aisleFatherAdapter
        (rvAisles.adapter as AisleFatherAdapter).setData(rowDatas)

        tv_upload_aisles.setOnClickListener {
            val gson = Gson()
            val jsonArray = JsonArray()
//            aisles.add(Aisle(1, 1, 1))
            aisles.forEach {
                val jsonObject = JsonObject()
                jsonObject.addProperty("device_id", URL.device_id)
                jsonObject.addProperty("egg_code_x", it.row)
                jsonObject.addProperty("egg_code_y", it.column)
                jsonObject.addProperty("aisles", it.number)
                jsonArray.add(jsonObject)
            }
            var pro_list = gson.toJson(jsonArray)
            Log.i("condy", "=====" + pro_list)
            RetrofitUtil.instance.getDefautlRetrofit()
                .getUpLoadAisle(pro_list = pro_list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(CustomObserver<Any>().apply {
                    _onNext {
                        Log.i("condy", "_onNext")
                        var jsonObject = JSON.parseObject(JSON.toJSONString(it))
                        var code = jsonObject.getIntValue("code")
                        if (code == 0) {
                            DKToastUtils.showCustomShort("上送成功")
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
                XLog.d("接收到消息--->${bysStr}")

                if (bytes.last() != 0x0D.toByte()) {
                    XLog.d("数据接收有误--->$bysStr")
                    return@let
                }

                when (bytes[4] * bytes[5]) {
                    0x02.toByte() * 0x42.toByte() -> {
                        //获取货道数量
                        val bigInteger =
                            BigInteger(MyFunc.Byte2Hex(bytes[9]) + MyFunc.Byte2Hex(bytes[10]), 16)
                        val toInt = bigInteger.toInt()
                        var tempColumn = 0
                        var tempRow = 1
                        (0 until toInt).forEach {
                            val row = bytes[11 + it].toInt()
                            if (row == tempRow) {
                                tempColumn += 1
                            } else {
                                tempRow = row
                                tempColumn = 1
                            }
                            aisles.add(
                                Aisle(
                                    it + 1,
                                    row = row,
                                    column = tempColumn
                                )
                            )
                        }
                        var map: HashMap<String, List<Aisle>>? =
                            null
                        var x = 0
                        var list: ArrayList<Aisle>? = null
                        aisles.forEach {

                            if (x == it.row) {
                                list?.add(it)
                            } else {
                                x = it.row
                                map = HashMap<String, List<Aisle>>()
                                rowDatas.add(map!!)
                                list = ArrayList<Aisle>()
                                map?.put(x.toString(), list!!)
                                list?.add(it)
                            }
                        }
                        (rvAisles.adapter as AisleFatherAdapter).setData(rowDatas)
                    }

                    0x04.toByte() * 0x27.toByte() -> {
                        //出货结果
                        if (bytes[bytes.lastIndex - 3] == 0x00.toByte()) {
//                            mDialogLoading?.cancel()
                            DKToastUtils.showCustomShort("出货失败")
                        } else {
                            XLog.d("开始查询出货结果")
                            //出货结果查询指令
                            ShipmentManager.queryShipmentResult(order_byte)
                        }
                    }
                    0x04.toByte() * 0x2a.toByte() -> {
                        //出货结果的查询
                        when (bytes.size - 6) {
                            ShipmentManager.inShipmentLength -> {
                                //正在出货中
                                //出货结果查询指令
                                ShipmentManager.queryShipmentResult(order_byte)
                            }
                            in ShipmentManager.shippedCompleteMinLength..ShipmentManager.shippedCompleteMaxLength -> {
                                //出货结束
//                                mDialogLoading?.cancel()
                                val byte = bytes[bytes.lastIndex - 14]
                                if (byte == 0x00.toByte()) {
                                    DKToastUtils.showCustomShort("出货成功")
                                } else {
                                    val msg = faultCodeDelivery[byte] ?: "未知故障"
                                    DKToastUtils.showCustomShort(msg)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoad(msg: String) {
//        mDialogLoading = RxDialogLoading(this).apply {
//            setLoadingText(msg)
//            setCanceledOnTouchOutside(false)
//            setCancelable(false)
//            show()
//        }
    }

    override fun aisleClick(aisle: Aisle) {
        XLog.d("${aisle.number}号货道开始出货")
        DKToastUtils.showCustomShort("${aisle.number}号货道开始出货,查询出货结果中,请稍候....")
        showLoad("${aisle.number}号货道开始出货,查询出货结果中,请稍候....")
        order_byte = ShipmentManager.startShipment(aisle.number)
        val bysStr = MyFunc.toHexString(order_byte).split(" ").toString()
        XLog.d("订单编号---->${bysStr}")
    }


}