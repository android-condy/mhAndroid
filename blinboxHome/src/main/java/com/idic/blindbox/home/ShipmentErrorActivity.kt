package com.idic.blindbox.home

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.idic.backstagemoudle.boxtest.ShipmentManager
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.obtainViewModel
import com.idic.basecommon.utils.FilePath
import com.idic.blindbox.Config
import com.idic.blindbox.R
import com.idic.blindbox.RouterPath
import com.idic.blindbox.ViewModelFactory
import com.idic.blindbox.ad.BannerAdItemFragment
import com.idic.blindbox.adapter.BindingAdapter
import com.idic.blindbox.model.MainModel
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.db.entity.NewBannerEntity
import com.idic.datamoudle.source.AdDataSource
import com.idic.datamoudle.source.repository.BannersDatasCallBack
import com.idic.datamoudle.utils.HttpActivityExt
import kotlinx.android.synthetic.main.activity_shipment_error.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Route(path = RouterPath.SHIPMENT_ERROR)
class ShipmentErrorActivity : BaseFullActivity(), BannersDatasCallBack, AdDataSource.LoadAdDatasCallBack {
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
        setContentView(R.layout.activity_shipment_error)
//        obtainViewModel(
//            MainModel::class.java,
//            ViewModelFactory.getInstance()
//        ).also {
//            it.loadAd(this, "4")
//            it.loadAd(this, "5")
//        }
        initView()
        HttpActivityExt.getGoodsSuccessBanners(this)
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

    private fun initView() {

        Glide.with(this).asGif()
            .load(R.drawable.shipment_error)
            .into(logo2)
        handler.postDelayed({
            toMain()
        }, 10000)

        BindingAdapter.loadProductImg(ivPrdbg, imgUrl)
        btnBack.setOnClickListener {
            toMain()
        }
        bgVideo?.let {
            it.setEnableAudioFocus(false)
            it.release()
            it.setUrl(FilePath.bgVideo)
            it.setLooping(true)
            it.start()
        }
    }

    override fun onResume() {
        super.onResume()
        bgVideo?.resume()
        GlobalScope.launch {
            ShipmentManager.setErrorHandler(null)
            ShipmentManager.setReceiverHandler(null)
            ShipmentManager.openMaintenanceMode()
            delay(1000)
            ShipmentManager.closeMaintenanceMode()
        }
    }


    private fun toMain() {
        ActivityUtils.startActivity(BlindBoxMainActivity::class.java)
        finish()
    }

    override fun onBannerLoaded(banners: List<BannerEntity>) {


    }

    override fun onAdDataNotAvaliad() {
    }
}