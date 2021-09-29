package com.idic.blindbox.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.alibaba.fastjson.JSON
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder
import com.allenliu.versionchecklib.v2.builder.UIData
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener
import com.blankj.utilcode.util.AppUtils
import com.bumptech.glide.Glide
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.obtainViewModel
import com.idic.basecommon.utils.CustomObserver
import com.idic.basecommon.utils.FilePath
import com.idic.blindbox.Config
import com.idic.blindbox.R
import com.idic.blindbox.ViewModelFactory
import com.idic.blindbox.ad.BannerAdItemFragment
import com.idic.blindbox.model.MainModel
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.db.entity.NewBannerEntity
import com.idic.datamoudle.db.entity.NewProductDetail
import com.idic.datamoudle.source.AdDataSource
import com.idic.datamoudle.source.repository.BannersDatasCallBack
import com.idic.datamoudle.utils.HttpActivityExt
import com.idic.httpmoudle.utils.RetrofitUtil
import com.idic.utilmoudle.DKToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_blind_help.*
import java.io.File

class BlindHelpActivity : BaseFullActivity(), AdDataSource.LoadAdDatasCallBack, BannersDatasCallBack {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blind_help)
//        obtainViewModel(
//            MainModel::class.java,
//            ViewModelFactory.getInstance()
//        ).also {
//            it.loadAd(this, "5")
//        }
        Glide.with(this)
            .asGif()
            .load(R.drawable.help)
            .into(icon)
        ivBack.setOnClickListener {
            finish()
        }
        bgVideo?.let {
            it.setEnableAudioFocus(false)
            it.release()
            it.setUrl(FilePath.bgVideo)
            it.setLooping(true)
            it.start()
        }


        HttpActivityExt.getCustomerServiceButtomBanner(this)

    }

    override fun onResume() {
        super.onResume()
        bgVideo?.resume()
    }

    override fun onStop() {
        super.onStop()
        bgVideo?.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        AllenVersionChecker.getInstance().cancelAllMission()
    }

    override fun onBannerLoaded(banners: List<BannerEntity>) {

    }

    override fun onAdDataNotAvaliad() {
    }
}