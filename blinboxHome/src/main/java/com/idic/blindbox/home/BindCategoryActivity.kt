package com.idic.blindbox.home

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.idic.basecommon.BaseFullActivity
import com.idic.blindbox.Config
import com.idic.basecommon.obtainViewModel
import com.idic.basecommon.utils.FilePath
import com.idic.blindbox.*
import com.idic.blindbox.ViewModelFactory
import com.idic.blindbox.adapter.CategoryAdapter
import com.idic.blindbox.listener.CategoryItemClick
import com.idic.blindbox.model.MainModel
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.db.entity.CategoryEntity
import com.idic.datamoudle.db.entity.NewCategoryEntity
import com.idic.datamoudle.db.entity.NewProductEntity
import com.idic.datamoudle.source.AdDataSource
import com.idic.datamoudle.source.ProductDataSource
import com.idic.datamoudle.source.repository.CategoryDatasCallBack
import com.idic.datamoudle.source.repository.ProductDatasCallBack
import com.idic.datamoudle.utils.HttpActivityExt
import com.idic.widgetmoudle.PlayVideoView
import kotlinx.android.synthetic.main.activity_bind_category.*
import java.util.*
import kotlin.collections.ArrayList

class BindCategoryActivity : BaseFullActivity(),
    ProductDataSource.LoadProductCategory,
    AdDataSource.LoadAdDatasCallBack, CategoryDatasCallBack,
    CategoryItemClick {

    private var mainModel: MainModel? = null

    //视频广告集合
    private val ad_video: Queue<BannerEntity> = LinkedList<BannerEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind_category)
        initView()
//        mainModel = obtainViewModel(
//            MainModel::class.java,
//            ViewModelFactory.getInstance()
//        ).also {
//            mainModel = it
//            it.start(this)
//            it.loadAd(this)
//        }

        HttpActivityExt.getGoodsCategory(this)
    }

    override fun onCategoryDataLoaded(category: List<NewCategoryEntity>) {
        Log.i("condy", "products=" + category)
        (rvCategory.adapter as CategoryAdapter).setData(category)

    }

    override fun onCategoryNotData() {

    }

    override fun onResume() {
        super.onResume()
        video?.let {
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
        video?.release()
    }


    private fun initView() {
//        video.setVideoURI(Uri.parse(Config.BG_VIDEO_URI))
//        video.setOnPreparedListener {
//            mediaPlayer = it
//            it.isLooping = true
//            it.start()
//        }
        rvCategory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvCategory.adapter =
                CategoryAdapter(ArrayList()) {
                    it.setBinding(BR.click, this@BindCategoryActivity)
                }
        btnBack.setOnClickListener {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBannerLoaded(banners: List<BannerEntity>) {
        runOnUiThread {
            banners.forEach {
                if (it.isVideo()) {
                    ad_video.add(it)
                }
            }
//            if (ad_video.isNotEmpty()) {
//                flAdVideo?.visibility = View.VISIBLE
//                playLoopVideo()
//            } else {
//                flAdVideo?.visibility = View.INVISIBLE
//            }
//            playVideo.videoPath = videoLocalPath
//            playVideo?.setPayListener(object : PlayVideoView.PlayListener {
//                override fun startPlay(mediaPlayer: MediaPlayer) {
//                    mediaPlayer.start()
//                }
//
//                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//                override fun completed() {
//                    playVideo.startPlay()
//                }
//
//                override fun error() {
//                    playVideo.startPlay()
//                }
//            })
//            playVideo.startPlay()
        }
    }

    override fun onAdDataNotAvaliad() {

    }

    override fun onCategoryLoaded(categorice: List<CategoryEntity>) {
//        val cates = ArrayList<CategoryEntity>()
//        categorice.filterTo(cates) { !it.categoryName.contains("其他") }
//        (rvCategory.adapter as CategoryAdapter).setData(cates)
    }

    override fun onCategoryNotAvailabled() {
        (rvCategory.adapter as CategoryAdapter).setData(ArrayList())
    }

    override fun categoryClick(categoryEntity: NewCategoryEntity) {

        ARouter.getInstance().build(RouterPath.CATEGORY_GOOD)
            .withInt("id", categoryEntity.id!!)
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