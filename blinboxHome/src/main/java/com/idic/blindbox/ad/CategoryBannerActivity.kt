package com.idic.blindbox.ad

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View.GONE
import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.boxtest.ShipmentManager
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.events.JpushMessage
import com.idic.basecommon.events.LightType
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.utils.CustomObserver
import com.idic.basecommon.utils.FilePath
import com.idic.blind.Shipment
import com.idic.blindbox.R
import com.idic.blindbox.common.SystemStatus
import com.idic.blindbox.home.BlindBoxMainActivity
import com.idic.blindbox.model.MainModel
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.db.entity.NewBannerEntity
import com.idic.datamoudle.source.AdDataSource
import com.idic.datamoudle.source.repository.BannersDatasCallBack
import com.idic.datamoudle.utils.ConfigSharedPreferences
import com.idic.datamoudle.utils.HttpActivityExt
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.utilmoudle.DKToastUtils
import com.ywl5320.libenum.MuteEnum
import com.ywl5320.libmusic.WlMusic
import com.ywl5320.util.RawAssetsUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.category_activity_banner.*
import kotlinx.android.synthetic.main.category_activity_banner.adBanner
import serialport_idic.MyFunc
import java.util.*
import java.util.concurrent.TimeUnit
import kotlinx.android.synthetic.main.category_activity_banner.ivBannerBg


class CategoryBannerActivity : BaseFullActivity(), AdDataSource.LoadAdDatasCallBack, BannersDatasCallBack {
    private class SystemStatusHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            msg?.let {
                when (it.what) {
                    3 -> {
                        ShipmentManager.querySystemStatus()
                    }
                }
            }
        }
    }

    private var animator: ObjectAnimator? = null

    private val DELAY_TIME = 2000L
    private val msystemStatusHandler = SystemStatusHandler()

    private lateinit var mainModel: MainModel

    //视频广告集合
    private val ad_video: Queue<BannerEntity> = LinkedList<BannerEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_activity_banner)
//        mainModel = obtainViewModel(
//            MainModel::class.java,
//            ViewModelFactory.getInstance()
//        ).also {
//            it.loadAd(this)
//        }
        flExit.setOnClickListener { finish() }
        ShipmentManager.setReceiverHandler(receiveHandler)
        ShipmentManager.querySystemStatus()
        handler.postDelayed(queryTask, 500)
//        listener()
        RxRelay.instance.post(LightType.PURPLE_LIGHT)
        HttpActivityExt.getMainBanners(this@CategoryBannerActivity)
    }

    override fun onStop() {
        super.onStop()
        adBanner_video?.release()
        handler.removeCallbacks(queryTask)
        msystemStatusHandler.removeMessages(3)
        RxRelay.instance.post(LightType.WHITE_LIGHT)
        finish()
    }

    private fun listener() {
        RxRelay.instance.toObserver(JpushMessage::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .throttleLast(10, TimeUnit.SECONDS)
            .subscribe(CustomObserver<JpushMessage>().apply {
                _subscribe {
                    //                    disposables.add(it)
                }
                _onNext {
                    when (it.jPushType) {
//                        JpushMessage.JPushType.UPDATEBANNER -> {
//                            ActivityUtils.finishActivity(CategoryBannerActivity::class.java)
//                            startService(
//                                Intent(
//                                    this@CategoryBannerActivity,
//                                    DownloadService::class.java
//                                )
//                            )
////                            mainModel.loadAd(this@CategoryBannerActivity)
//                        }
                    }
                }
            })
    }

    override fun onBannerLoaded(banners: List<BannerEntity>) {
        if (banners.isEmpty()) {
            finish()
            return
        }
        banners.filterTo(ad_video) { it.isVideo() }
        if (ad_video.isEmpty()) {
            finish()
            return
        }

    }

    override fun playLoopVideo() {
        Log.i("condy", "视频地址=" + ConfigSharedPreferences.getSharedPreferencesVideoAdrress(this))
        adBanner_video?.let {
            it.setEnableAudioFocus(false)
            it.release()
            it.setUrl(ConfigSharedPreferences.getSharedPreferencesVideoAdrress(this))
            it.setLooping(true)
            it.start()
        }
    }

    override fun onBannerNotData() {
    }

    override fun onBannerDataLoaded(banners: List<NewBannerEntity>) {
        var flag = false
        banners.forEach {
            if (it.flag == 2) {
                flag = true
            }
        }
        if (flag) {
            runOnUiThread {
                playLoopVideo()
            }
            adBanner.visibility = GONE
            ivBannerBg.visibility = GONE
        } else {
            adBanner_video.visibility = GONE
            val images = ArrayList<BannerEntity>()
            banners.forEach {
                var bannerEntity = BannerEntity()
                bannerEntity.image = it.img_url
                images.add(bannerEntity)
                adBanner?.let { bannerViewPager ->
                    bannerViewPager.removeAllViews()
                    bannerViewPager.removeMessage()
                    bannerViewPager.setPagerFragment(
                        images.size,
                        supportFragmentManager
                    ) {
                        Log.i("condy", "postion=" + it)
                        BannerAdItemFragment.newInstance(images[it])
                    }
                    bannerViewPager.startAnim()
                }
            }
        }
    }

    override fun onAdDataNotAvaliad() {
        finish()
    }

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
                if (bytes.last() != 0x0D.toByte() || bytes.size < 6) {
                    XLog.d("数据接收有误--->$bysStr")
                    return@let
                }
                when (bytes[4] * bytes[5]) {
                    0x01.toByte() * 0x2D.toByte() -> {
                        if (bytes.size < 24) return@let
                        if (bytes[24] == 0x00.toByte()) {
                            ShipmentManager.setReceiverHandler(null)
                            ShipmentManager.setErrorHandler(null)
                            //有人
                            Thread() {
                                val wlMusic = WlMusic.getInstance()
                                wlMusic.stop()
                                val url = RawAssetsUtil.getRawFilePath(
                                    this@CategoryBannerActivity,
                                    R.raw.welcome,
                                    "welcome.wav"
                                )
                                wlMusic.source = url
                                wlMusic.volume = 100; //设置音量 65%
                                wlMusic.mute = MuteEnum.MUTE_CENTER; //设置立体声（左声道、右声道和立体声）
                                wlMusic.prePared()//准备开始
                                wlMusic.setOnPreparedListener {
                                    wlMusic.start() //准备完成开始播放
                                }
                            }.start()
                            finish()
                        }
                    }

                    0x01.toByte() * 0x27.toByte() -> {
                        //系统状态
                        var system_msg = ""
                        val msg = when (bytes[bytes.lastIndex - 3]) {
                            SystemStatus.INITCOMPLETE.byte -> {
                                system_msg = "初始化完成"
                            }
                            SystemStatus.STANDBY.byte -> {
                                system_msg = "待机中"
                            }
                            SystemStatus.SELLING.byte -> {
                                system_msg = "售卖中"
                            }
                            SystemStatus.MAINTENANCE.byte -> {
                                system_msg = "维护中"
                            }
                            SystemStatus.UPGRADE.byte -> {
                                system_msg = "升级中"
                            }
                            SystemStatus.STOP_IN.byte -> {
                                system_msg = "停止中"
                            }
                            else -> {
                                system_msg = "未知状态"
                            }
                        }
                        RetrofitUtil.instance.getDefautlRetrofit()
                            .uploadSystemMessage(status = "待机页", msg = system_msg + ":" + bysStr)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe()
                        msystemStatusHandler.sendEmptyMessageDelayed(3, 5 * 60 * 1000)
                    }
                }
            }
        }
    }

    private val queryTask = object : Runnable {
        override fun run() {
            XLog.d("发送红外指令")
            ShipmentManager.getSensor()
            handler.postDelayed(this, DELAY_TIME)
        }

    }
}
