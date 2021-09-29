package com.idic.backstagemoudle.ui

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.blankj.utilcode.util.*
import com.idic.backstagemoudle.BR
import com.idic.backstagemoudle.R
import com.idic.backstagemoudle.boxtest.view.BlindMainActivity
import com.idic.backstagemoudle.db.DeviceConfigDataBase
import com.idic.backstagemoudle.ui.adapter.BackStageAdapter
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.SPKeys
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.events.UploadLogEvent
import com.idic.basecommon.showBottomBar
import com.idic.utilmoudle.DKToastUtils
import com.idic.widgetmoudle.adapter.SpaceItemDecoration
import kotlinx.android.synthetic.main.back_start_toolbar.*
import kotlinx.android.synthetic.main.backstage_activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val LAND_ITEM_COUNT = 4
private const val PORTRAIT_ITEM_COUNT = 3
private const val UPLOAD_TIME = "uploadTime" //3600000一个小时

class BackStageHomeActivity :
    BaseFullActivity(),
    BackStageItemListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backstage_activity_home)

//        //初始化化数据库，填充数据
//        DeviceConfigDataBase.getInstance()
        Utils.init(application)
        tvTitle.text = getString(R.string.backStage)
        tvSave.visibility = View.GONE
        val stringArray = resources.getStringArray(R.array.backstage_item).toList() as ArrayList

//        if (!SPUtils.getInstance().getString(SPKeys.DEVICES_KEY).isNullOrEmpty()) {
//            stringArray.remove(getString(R.string.deviceActive))
//        }
        val columns = if (ScreenUtils.isLandscape()) {
            LAND_ITEM_COUNT
        } else {
            PORTRAIT_ITEM_COUNT
        }

        rvBackItem.layoutManager = GridLayoutManager(this, columns)
        val dimension = resources.getDimension(R.dimen.x8).toInt()
        rvBackItem.addItemDecoration(
            SpaceItemDecoration(
                dimension,
                dimension,
                dimension,
                dimension
            )
        )
        rvBackItem.adapter =
            BackStageAdapter(R.layout.backstage_item,
                BR.title,
                ArrayList<String>().apply {
                    addAll(stringArray)
                }) {
                it.setBinding(BR.click, this)
            }
        tvBack.setOnClickListener {
            finish()
        }
        //放开为强制更新
//        startActivity(Intent(this, CheckUpdateActivity::class.java))
    }


    override fun onResume() {
        super.onResume()
        showBottomBar()
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            KeyboardUtils.hideSoftInput(this@BackStageHomeActivity)
        }
        tvVersion.text = getString(R.string.app_version, AppUtils.getAppVersionName())
    }

    override fun itemClick(title: String) {
        when (title) {
            getString(R.string.stockManager) -> {
                startActivity(Intent(this, StockManagerActivity::class.java))
            }
            getString(R.string.deviceConfig) -> {
                startActivity(Intent(this, DeviceConfigActivity::class.java))
            }
            getString(R.string.deviceCheck) -> {
                startActivity(Intent(this, BlindMainActivity::class.java))
            }
            getString(R.string.soldHome) -> {
                startActivity(Intent(this, CheckVersionActivity::class.java))
            }
            getString(R.string.checkUpdate) -> {
                startActivity(Intent(this, CheckUpdateActivity::class.java))
            }
            getString(R.string.sysSetting) -> {
                val i = Intent(Settings.ACTION_SETTINGS)
                startActivity(i)
                AppUtils.exitApp()
            }
            getString(R.string.exitApp) -> {
//                finish()
                AppUtils.exitApp()
            }
            getString(R.string.deviceActive) -> {
                //设备激活
                startActivity(Intent(this, DeviceActiveActivity::class.java))
            }
            getString(R.string.uploadLog) -> {
                System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
                //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续5次点击
                mHits[mHits.size - 1] = SystemClock.uptimeMillis()
                if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
                    DKToastUtils.showCustomLong("日志上传功能以启动,请勿重复点击")
                    RxRelay.instance.post(UploadLogEvent())
                }
//                val availableByPing = NetworkUtils.isAvailableByPing()
//                val lastUploadTime = SPUtils.getInstance().getLong(UPLOAD_TIME, 0)
//                val currentTimeMillis = System.currentTimeMillis()
//                if (currentTimeMillis - lastUploadTime < 3600000) {
//                    DKToastUtils.showCustomLong("日志上传过快,请稍后再试")
//                    if (!BuildConfig.DEBUG) {
//                        return
//                    }
//                }
//                if (availableByPing) {


//                    SPUtils.getInstance().put(UPLOAD_TIME, currentTimeMillis)
//                } else {
//                    DKToastUtils.showCustomLong("上传日志失败,网络未连接")
//                }
            }
        }
    }
}
