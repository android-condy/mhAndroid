package com.idic.blindbox.home

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.utils.FilePath
import com.idic.blindbox.R
import com.idic.blindbox.RouterPath
import com.idic.blindbox.ad.BannerAdItemFragment
import com.idic.blindbox.adapter.BindingAdapter.loadProductImg
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.db.entity.NewBannerEntity
import com.idic.datamoudle.source.AdDataSource
import com.idic.datamoudle.source.repository.BannersDatasCallBack
import com.idic.datamoudle.utils.HttpActivityExt
import com.ywl5320.libenum.MuteEnum
import com.ywl5320.libmusic.WlMusic
import com.ywl5320.util.RawAssetsUtil
import kotlinx.android.synthetic.main.activity_shipment_success.*


@Route(path = RouterPath.SHIPMENT_SUCCESS)
class ShipmentSuccessActivity : BaseFullActivity(), BannersDatasCallBack
    , AdDataSource.LoadAdDatasCallBack {
    override fun onBannerDataLoaded(banners: List<NewBannerEntity>) {
        val images = ArrayList<BannerEntity>()
        banners.forEach {
            var bannerEntity = BannerEntity()
            bannerEntity.image = it.img_url
            images.add(bannerEntity)
        }
        runOnUiThread {
            adBanner?.let { bannerViewPager ->
                bannerViewPager.removeAllViews()
                bannerViewPager.removeMessage()
                bannerViewPager.setPagerFragment(
                    images.size,
                    supportFragmentManager
                ) {
                    BannerAdItemFragment.newInstance(images[it])
                }
                indicaZoom?.let {
                    it.removeAllViews()
                    it.addPagerData(bannerViewPager)
                    if (images.size > 0) {
                        it.onPageSelected(0)
                    }
                }
            }
        }
    }

    override fun onBannerNotData() {
    }

    @Autowired(name = "url")
    @JvmField
    var imgUrl: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        setContentView(R.layout.activity_shipment_success)
        HttpActivityExt.getGoodsSuccessBanners(this)
        loadProductImg(ivPrdbg, imgUrl)
        Glide.with(this).asGif()
            .load(R.drawable.shipment_success)
            .into(logo2)
        handler.postDelayed({
            ActivityUtils.startActivity(BlindBoxMainActivity::class.java)
            finish()
        }, 10000)
        bgVideo?.let {
            it.setEnableAudioFocus(false)
            it.release()
            it.setUrl(FilePath.bgVideo)
            it.setLooping(true)
            it.start()
        }

        Thread() {
            val wlMusic = WlMusic.getInstance()
            val url = RawAssetsUtil.getRawFilePath(this, R.raw.fujian, "fujian.wav")
            wlMusic.source = url
            wlMusic.volume = 100; //设置音量 65%
            wlMusic.mute = MuteEnum.MUTE_CENTER; //设置立体声（左声道、右声道和立体声）
            wlMusic.prePared()//准备开始
            wlMusic.setOnPreparedListener {
                wlMusic.start() //准备完成开始播放
            }
        }.start()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
        bgVideo?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        bgVideo?.release()
    }

    override fun onBannerLoaded(banners: List<BannerEntity>) {
        runOnUiThread {
            val images = ArrayList<BannerEntity>()
            banners
                .filter { it.type == 4 || it.type == 5 }
                .forEach {
                    when (it.type) {
                        4 -> {
                            loadProductImg(ivPrdbg, it.image)
                        }
                        5 -> {
                            images.add(it)
                            adBanner?.let { bannerViewPager ->
                                bannerViewPager.removeAllViews()
                                bannerViewPager.removeMessage()
                                bannerViewPager.setPagerFragment(
                                    images.size,
                                    supportFragmentManager
                                ) {
                                    BannerAdItemFragment.newInstance(images[it])
                                }
                                indicaZoom?.let {
                                    it.removeAllViews()
                                    it.addPagerData(bannerViewPager)
                                    if (images.size > 0) {
                                        it.onPageSelected(0)
                                    }
                                }
                            }
                        }
                    }
                }
        }

    }

    override fun onAdDataNotAvaliad() {
    }
}