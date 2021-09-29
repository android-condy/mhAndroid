package com.idic.blindbox.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.*
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.boxtest.ShipmentManager
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.events.JpushMessage
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.events.SyncProductEvents
import com.idic.basecommon.events.SyncProductResult
import com.idic.basecommon.utils.ARouterConfig
import com.idic.basecommon.utils.CustomObserver
import com.idic.basecommon.utils.FilePath
import com.idic.blind.Shipment
import com.idic.blindbox.R
import com.idic.blindbox.ad.BannerAdItemFragment
import com.idic.blindbox.ad.CategoryBannerActivity
import com.idic.blindbox.adapter.BindingAdapter
import com.idic.blindbox.common.SystemStatus
import com.idic.blindbox.databinding.ActivityBlindBoxMainBinding
import com.idic.blindbox.model.MainModel
import com.idic.blindbox.service.DownloadService
import com.idic.blindbox.service.Permission
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.db.entity.NewBannerEntity
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.datamoudle.source.AdDataSource
import com.idic.datamoudle.source.ProductDataSource
import com.idic.datamoudle.source.repository.BannersDatasCallBack
import com.idic.datamoudle.utils.ConfigSharedPreferences
import com.idic.datamoudle.utils.DownloadVideoTask
import com.idic.datamoudle.utils.HttpActivityExt
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.utilmoudle.AppUtil
import com.idic.utilmoudle.DKToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_blind_box_main.*
import kotlinx.coroutines.*
import serialport_idic.MyFunc
import java.io.*
import java.net.URL
import java.net.URLConnection
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


@Route(path = ARouterConfig.BLIND_BOX)
class BlindBoxMainActivity : BaseFullActivity(),
    ProductDataSource.LoadProductsCallback, AdDataSource.LoadAdDatasCallBack,
    BannersDatasCallBack {
    companion object {

        val BANNER_TAG = 0x01
        val HEART_BEAT_TAG = 5000L * 60
        var count = 0
        val TO_BANNER_TIME = 60000L
    }

    //跳转到广告页面
    private class BannerHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            msg?.let {
                when (it.what) {
                    BANNER_TAG -> {
                        ActivityUtils.startActivity(CategoryBannerActivity::class.java)
                    }
                }
            }
        }
    }

    private class SystemStatusHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            msg?.let {
                when (it.what) {
                    2 -> {
                        ShipmentManager.querySystemStatus()
                    }
                }
            }
        }
    }


    private class SyncHandler(val activity: BlindBoxMainActivity) : Handler() {

        override fun handleMessage(msg: Message) {
            RetrofitUtil.instance.getDefautlRetrofit()
                .getSyncSystemStatus(version = AppUtils.getAppVersionCode().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(CustomObserver<Any>().apply {
                    _onNext {
                        try {
                            var json = JSON.parseObject(JSON.toJSONString(it))
//                            DKToastUtils.showCustomShort("数据="+JSON.toJSONString(it))
                            if (json.getIntValue("code") == 0) {
                                count = 0
                            }
                            if (json.getJSONObject("data").getIntValue("restart") == 1) {//1重启，0不重启
                                removeMessages(count)
                                AppUtil.reShunDown()
                            }
                            if (json.getJSONObject("data").getIntValue("update") == 1) {//1更新，0更新
                                activity.checkNewVersionHandler.sendEmptyMessage(1)
                            }
                        } catch (e: Exception) {
                        } finally {
                        }
                    }
                    _onError {

                    }


                })

            if (count == 3) {
                removeMessages(count)
                AppUtil.reShunDown()
            } else {
                sendEmptyMessageDelayed(++count, HEART_BEAT_TAG)
            }

        }
    }

    private var canPlay = AtomicBoolean(true)
    private val bannerHandler = BannerHandler()
    private val msystemStatusHandler = SystemStatusHandler()
    private val syncHandler = SyncHandler(this)

    private var isUpdate = false
    var mainModel: MainModel? = null
        private set



    private var loadADJob: Job? = null
    public val checkNewVersionHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            checkNewVersion()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityBlindBoxMainBinding>(
            this,
            R.layout.activity_blind_box_main
        )
        ShipmentManager.open("/dev/ttyS2", "9600")
        initView()

        syncHandler.sendEmptyMessageDelayed(++count, 10000)




        applyForPermission()
    }

    override fun onResume() {
        super.onResume()
        bgVideo?.let {
            it.setEnableAudioFocus(false)
            it.release()
            it.setUrl(FilePath.bgVideo)
            it.setLooping(true)
            it.start()
        }
        loadADJob?.cancel()
        loadADJob = GlobalScope.launch(Dispatchers.IO) {
            delay(1000)
            HttpActivityExt.getButtomBanners(object : BannersDatasCallBack {
                override fun onBannerDataLoaded(banners: List<NewBannerEntity>) {
                    BindingAdapter.loadProductImg(ivPrd1, banners[0]?.img_url)


                    BindingAdapter.loadProductImg(ivPrd2, banners[1]?.img_url)
                }

                override fun onBannerNotData() {
                }
            })
            HttpActivityExt.getMainBanners(this@BlindBoxMainActivity)
        }
        bannerHandler.removeMessages(BANNER_TAG)
        bannerHandler.sendEmptyMessageDelayed(BANNER_TAG, TO_BANNER_TIME)

        msystemStatusHandler.removeMessages(2)
        bannerHandler.sendEmptyMessageDelayed(2, 6 * 1000)
        ShipmentManager.setReceiverHandler(receiveHandler)
        ShipmentManager.querySystemStatus()

//Crash测试代码

//        CrashReport.testJavaCrash();


    }

    private fun loadAd() {
        mainModel?.let { model ->
            model.loadAd(object : AdDataSource.LoadAdDatasCallBack {
                override fun onBannerLoaded(banners: List<BannerEntity>) {
//                    ad_video.clear()
                    runOnUiThread {
                        val images = ArrayList<BannerEntity>()
                        banners.forEach {
                            if (it.isVideo()) {
//                                ad_video.add(it)
                            } else {
                                images.add(it)
                            }
                            adBanner?.let { bannerViewPager ->
                                bannerViewPager.removeAllViews()
                                bannerViewPager.removeMessage()
                                bannerViewPager.setPagerFragment(
                                    images.size,
                                    supportFragmentManager
                                ) {
                                    BannerAdItemFragment.newInstance(images[it])
                                }
                                bannerViewPager.startAnim()
                            }
                        }
                        bannerHandler.removeMessages(BANNER_TAG)
                        bannerHandler.sendEmptyMessageDelayed(BANNER_TAG, TO_BANNER_TIME)
                    }
                }

                override fun onAdDataNotAvaliad() {
                }

            })
            model.loadAd(this@BlindBoxMainActivity, "2")
            model.loadAd(this@BlindBoxMainActivity, "3")
        }
    }

    override fun onPause() {
        super.onPause()
        bannerHandler.removeMessages(BANNER_TAG)
        msystemStatusHandler.removeMessages(2)
//        ShipmentManager.setReceiverHandler(null)
    }

    override fun onStop() {
        loadADJob?.cancel()
        super.onStop()
//        playVideo?.clearOnStateChangeListeners()
//        playVideo?.release()
        bgVideo?.release()
        bannerHandler.removeMessages(BANNER_TAG)
        msystemStatusHandler.removeMessages(2)
//        ShipmentManager.setReceiverHandler(null)
    }

    private fun applyForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            for (i in 0 until Permission.ALL.size) {
                val i1: Int = ContextCompat.checkSelfPermission(
                    applicationContext,
                    Permission.ALL.get(i)
                )
                if (i1 != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有授予该权限，就去提示用户请求
                    startRequestPermission()
                }
            }
        }
    }

    /**
     * 开始提交请求权限
     */
    private fun startRequestPermission() {
        ActivityCompat.requestPermissions(this, Permission.ALL, 321)
    }

    override fun onDestroy() {
        loadADJob?.cancel()
        super.onDestroy()
//        bannerHandler.removeCallbacks(playMusicTime)
    }


    private fun listener() {
        RxRelay.instance.toObserver(SyncProductEvents::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(CustomObserver<SyncProductEvents>().apply {
                _subscribe {
                    disposables.add(it)
                }
                _onNext {
                    runOnUiThread {
                    }
                    mainModel?.startAllPrd(true, this@BlindBoxMainActivity)
                }
            })
        RxRelay.instance.toObserver(JpushMessage::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .throttleLast(10, TimeUnit.SECONDS)
            .subscribe(CustomObserver<JpushMessage>().apply {
                _subscribe {
                    disposables.add(it)
                }
                _onNext {
                    when (it.jPushType) {
                        JpushMessage.JPushType.UPDATEALLPRODUCT -> {
                            mainModel?.startAllPrd(true, this@BlindBoxMainActivity)
                        }
                        JpushMessage.JPushType.UPDATEBANNER -> {
                            mainModel?.removeAllAd()
                            ActivityUtils.finishActivity(CategoryBannerActivity::class.java)
                            startService(
                                Intent(
                                    this@BlindBoxMainActivity,
                                    DownloadService::class.java
                                )
                            )
//                            GlobalScope.launch {
//                                delay(1000)
//                                loadAd()
//                            }
                        }
                    }
                }
            })
    }

    private fun initView() {
        tvVersion.text = AppUtils.getAppVersionName()
        iv2AllProduct.setOnClickListener {
            if (isUpdate) {
                DKToastUtils.showCustomLong("正在更新，请稍等...")
                return@setOnClickListener
            }
            playMusic()
            ActivityUtils.startActivity(BlindAllProductActivity::class.java)
        }
        ivPrd1.setOnClickListener {
            if (isUpdate) {
                DKToastUtils.showCustomLong("正在更新，请稍等...")
                return@setOnClickListener
            }
            playMusic()
            ActivityUtils.startActivity(BlindAllProductActivity::class.java)
        }
        ivPrd2.setOnClickListener {
            if (isUpdate) {
                DKToastUtils.showCustomLong("正在更新，请稍等...")
                return@setOnClickListener
            }
            playMusic()
            ActivityUtils.startActivity(BlindAllProductActivity::class.java)
        }
        btn2Service.setOnClickListener {
            if (isUpdate) {
                DKToastUtils.showCustomLong("正在更新，请稍等...")
                return@setOnClickListener
            }
            playMusic()
            ActivityUtils.startActivity(BlindHelpActivity::class.java)
        }
        btn2Pay.setOnClickListener {
            if (isUpdate) {
                DKToastUtils.showCustomLong("正在更新，请稍等...")
                return@setOnClickListener
            }
            playMusic()
            ActivityUtils.startActivity(BlindAllProductActivity::class.java)
        }
        iv2Backstage.setOnClickListener {
            if (isUpdate) {
                DKToastUtils.showCustomLong("正在更新，请稍等...")
                return@setOnClickListener
            }
            System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
            //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续5次点击
            mHits[mHits.size - 1] = SystemClock.uptimeMillis()
            if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
                ARouter.getInstance().build(ARouterConfig.BACKSTAGE_HOME).navigation()
            }
        }
    }

    private fun playMusic() {
//        if (canPlay.get()) {
//            val wlMusic = WlMusic.getInstance()
//            val url = RawAssetsUtil.getRawFilePath(
//                this@BlindBoxMainActivity,
//                R.raw.welcome,
//                "welcome.wav"
//            )
//            wlMusic.source = url
//            wlMusic.volume = 100; //设置音量 65%
//            wlMusic.mute = MuteEnum.MUTE_CENTER; //设置立体声（左声道、右声道和立体声）
//            wlMusic.prePared()//准备开始
//            wlMusic.setOnPreparedListener {
//                wlMusic.start() //准备完成开始播放
//            }
//            canPlay.set(false)
//            bannerHandler.postDelayed(playMusicTime, 120000)
//        }
    }

    override fun onProductsLoad(products: List<ProductEntity>) {
        RxRelay.instance.post(SyncProductResult(true))
    }

    override fun onProductNotAvailable() {
//        playVideo?.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBannerLoaded(banners: List<BannerEntity>) {
        runOnUiThread {
            banners.forEach {
                when (it.type) {
                    1 -> {
                    }
                    2 -> {
                        BindingAdapter.loadProductImg(ivPrd1, it.image)
                    }
                    3 -> {
                        BindingAdapter.loadProductImg(ivPrd2, it.image)
                    }
                }
            }
        }
    }

    override fun onAdDataNotAvaliad() {

    }

    override fun playLoopVideo() {
//        val video = ad_video.poll()
//        if (video != null) {
//            ad_video.add(video)
//            playVideo?.let {
//                it.setEnableAudioFocus(false)
//                it.release()
//                it.setUrl(video.image)
//                it.clearOnStateChangeListeners()
//                it.addOnStateChangeListener(this)
//                it.start()
//            }
//        }
    }


    private val playMusicTime = Runnable { canPlay.set(true) }


    override fun onBannerDataLoaded(banners: List<NewBannerEntity>) {
        val images = ArrayList<BannerEntity>()
        banners?.forEach {
            //            if (it.isVideo()) {
//            } else {
            var bannerEntity = BannerEntity()
            bannerEntity.image = it.img_url
            images.add(bannerEntity)
            var value = ConfigSharedPreferences.getSharedPreferences(baseContext)
            if (it.flag == 2 && (value < it.flash_version)) {//有视频
                ConfigSharedPreferences.saveSharedPreferences(Utils.getApp(), it.flash_version)
                DownloadVideoTask.downloadVideo2(it.video_url)
            }
        }
        adBanner?.let { bannerViewPager ->
            bannerViewPager.removeAllViews()
            bannerViewPager.removeMessage()
            bannerViewPager.setPagerFragment(
                images.size,
                supportFragmentManager
            ) {
                BannerAdItemFragment.newInstance(images[it])
            }
            bannerViewPager.startAnim()
//            }
        }
        bannerHandler.removeMessages(BANNER_TAG)
        bannerHandler.sendEmptyMessageDelayed(BANNER_TAG, TO_BANNER_TIME)
    }

    override fun onBannerNotData() {
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
                when (bytes[4] * bytes[5]) {
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
                            .uploadSystemMessage(status = "主页", msg = system_msg + ":" + bysStr)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe()
                        msystemStatusHandler.sendEmptyMessageDelayed(2, 5 * 60 * 1000)
                    }
                    else -> {
                    }
                }
            }
        }
    }


    private fun checkNewVersion() {
        RetrofitUtil.instance.getDefautlRetrofit()
            .requestVersion().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(CustomObserver<Any>().apply {
                _onNext {

                    var json = JSON.parseObject(JSON.toJSONString(it))
                    if (json.getIntValue("version") > AppUtils.getAppVersionCode()) {
                        isUpdate = true
                        Thread(Runnable { downloadApk(json.getString("url")) }).start()
                    } else {
                        isUpdate = false
                    }
                }

                _onError {
                    isUpdate = false
                }


            })
    }


    /**
     * 下载APK
     */
    private fun downloadApk(url: String) {
        if (url == null || url.isEmpty()) {
            isUpdate = false
            return
        }
        try {
            val localPath = FilePath.stand_Video + File.separator + "app-release.apk"
            val fileAPK = File(localPath)
            if (fileAPK.exists()) {
                fileAPK.delete()
            }
            val startTime = System.currentTimeMillis()
            Log.i("DOWNLOAD", "startTime=$startTime")
            //获取文件名
            val myURL = URL(url)
            val conn: URLConnection = myURL.openConnection()
            conn.connect()
            val `is`: InputStream = conn.getInputStream()
            val fileSize: Int = conn.getContentLength() //根据响应获取文件大小
            Log.i("DOWNLOAD", "总大小=$fileSize")
            if (fileSize <= 0) throw RuntimeException("无法获知文件大小 ")
            if (`is` == null) throw RuntimeException("stream is null")
            val file1 = File(localPath)
            if (!file1.exists()) {
                file1.createNewFile()
            }
            Log.i("DOWNLOAD", "file1=" + file1.absolutePath);
            //把数据存入路径+文件名
            val fos = FileOutputStream(file1)
            val buf = ByteArray(1024)
            var downLoadFileSize = 0L
            Log.i("DOWNLOAD", "downLoadFileSize=" + downLoadFileSize);
            do {
                //循环读取
                val numread: Int = `is`.read(buf)
                if (numread == -1) {
                    break
                }
                fos.write(buf, 0, numread)
                downLoadFileSize += numread
                runOnUiThread {
                    var progress = (downLoadFileSize * 100) / fileSize
                    update_progress.visibility = View.VISIBLE
                    update_progress.text = "正在更新，请稍等..." + progress.toString() + "%"
                    tvVersion.text = (progress.toString())
                    if (progress >= 100) {
                        update_progress.visibility = View.GONE
                    }
                }
                //更新进度条
            } while (true)

            `is`.close()
            var rootUtils = RootUtils()
            val inResult: String =
                rootUtils.execRootCmd("pm install -r ${file1!!.absolutePath}" + "&&" + "am start -n com.idic/" + ActivityUtils.getLauncherActivity())
        } catch (ex: java.lang.Exception) {
            Log.i("DOWNLOAD", "ex=" + ex.message);
        } finally {
            isUpdate = false
        }

    }

}