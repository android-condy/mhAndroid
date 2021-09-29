package com.idic.blindbox.home

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.obtainViewModel
import com.idic.basecommon.utils.FilePath
import com.idic.blindbox.BR
import com.idic.blindbox.R
import com.idic.blindbox.RouterPath
import com.idic.blindbox.ViewModelFactory
import com.idic.blindbox.adapter.ProductAdapter
import com.idic.blindbox.listener.ItemClickListener
import com.idic.blindbox.model.MainModel
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.db.entity.NewProductEntity
import com.idic.datamoudle.db.entity.ProductEntity
import com.idic.datamoudle.source.AdDataSource
import com.idic.datamoudle.source.ProductDataSource
import com.idic.datamoudle.source.repository.ProductDatasCallBack
import com.idic.datamoudle.utils.HttpActivityExt
import com.idic.utilmoudle.ResourcesUtil
import com.idic.widgetmoudle.adapter.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_bind_all_product.*
import java.util.*
import kotlin.collections.ArrayList

class BlindAllProductActivity : BaseFullActivity(),
    AdDataSource.LoadAdDatasCallBack,
    ProductDataSource.LoadProductsCallback, ProductDatasCallBack,
    ItemClickListener {
    override fun onProductDataLoaded(Products: List<NewProductEntity>) {
        (rvAllGood?.adapter as? ProductAdapter)?.setData(Products)
    }

    override fun onProductNotData() {
    }

    //视频广告集合
    private val ad_video: Queue<BannerEntity> = LinkedList<BannerEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind_all_product)
        initView()
        obtainViewModel(
            MainModel::class.java,
            ViewModelFactory.getInstance()
        ).also {
//            it.startAllPrd(true, this)
//            it.loadAd(this)
        }

        HttpActivityExt.getGoods(this)

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
//        if (ad_video.isNotEmpty()) {
//            playLoopVideo()
//        }
    }

    override fun onStop() {
        super.onStop()
//        playVideo?.clearOnStateChangeListeners()
//        playVideo?.release()
        bgVideo?.release()
    }


    private fun initView() {

        btnBack.setOnClickListener { finish() }
        val top = ResourcesUtil.getDimen(R.dimen.y20).toInt()
        rvAllGood.layoutManager = GridLayoutManager(this, 3)
        rvAllGood.addItemDecoration(SpaceItemDecoration(0, top, 0, 0))
        rvAllGood.adapter =
                ProductAdapter(R.layout.blind_product_item, BR.good, ArrayList()) {
                    it.setBinding(BR.click, this@BlindAllProductActivity)
                }

        btnBack.setOnClickListener { finish() }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBannerLoaded(banners: List<BannerEntity>) {
//        runOnUiThread {
//            banners.forEach {
//                if (it.isVideo()) {
//                    ad_video.add(it)
//                }
//            }
//            if (ad_video.isNotEmpty()) {
//                flAdVideo?.visibility = View.VISIBLE
//                playLoopVideo()
//            } else {
//                flAdVideo?.visibility = View.INVISIBLE
//            }
//        }
    }

    override fun onAdDataNotAvaliad() {

    }

    override fun onProductsLoad(products: List<ProductEntity>) {
//        (rvAllGood?.adapter as? ProductAdapter)?.setData(products)
    }

    override fun onProductNotAvailable() {
    }

    override fun onClick(view: View, product: NewProductEntity) {
        ARouter.getInstance().build(RouterPath.DETAIL)
            .withString("sku_code", product.sku_code)
            .withInt("sku_id",product.id)
            .navigation()
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
}
