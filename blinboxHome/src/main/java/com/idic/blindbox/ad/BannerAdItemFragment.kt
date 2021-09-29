package com.idic.blindbox.ad

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idic.blindbox.R
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.widgetmoudle.BannerViewPager
import com.idic.widgetmoudle.PlayVideoView
import com.idic.widgetmoudle.utils.GlideUtil
import kotlinx.android.synthetic.main.category_item_viewpager_fragment.*

/**
 * 文 件 名: PageItemFragment
 * 创 建 人: sineom
 * 创建日期: 2019/2/27 17:08
 * 修改时间：
 * 修改备注：
 */

//展示失败延迟时间
private val DEFAULT_DELAY_TIME = 500L

class BannerAdItemFragment :
    Fragment() {

    private var bannerData: BannerEntity? = null

    //是否可见
    private var isVisibleToUser = false

    //父控件
    private var viewPager: BannerViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        savedInstanceState?.let {
//            bannerData = it.getParcelable(IMG_KEY)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.let {
            with(it.javaClass) {
                when {
                    isAssignableFrom(BannerViewPager::class.java) -> {
                        viewPager = it as BannerViewPager
                    }
                }
            }
        }
        return inflater.inflate(R.layout.category_item_viewpager_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bannerData?.let { data ->
            if (data.isVideo()) {
                playVideo?.videoPath = data.localPath
                playVideo?.setPayListener(object : PlayVideoView.PlayListener {
                    override fun startPlay(mediaPlayer: MediaPlayer) {
                        if (isVisibleToUser) {
                            viewPager?.let {
                                if (it.itemCount < 2) {
                                    mediaPlayer.isLooping = true
                                }
                            }
                            activity?.runOnUiThread {
                                playVideo.visibility = View.VISIBLE
                                ivBannerImg.visibility = View.INVISIBLE
                            }
                            mediaPlayer.start()
                            val duration = mediaPlayer.duration
                            if (duration > 0) {
                                viewPager?.nextPage(mediaPlayer.duration.toLong())
                            }
                        }
                    }

                    override fun completed() {
                        if (isVisibleToUser) {
                            viewPager?.nextPage(DEFAULT_DELAY_TIME)
                        }
                    }

                    override fun error() {
                        if (isVisibleToUser) {
                            activity?.runOnUiThread {
                                playVideo.visibility = View.INVISIBLE
                                GlideUtil.loadImg(ivBannerImg, R.drawable.loading_img)
                            }
                            viewPager?.let {
                                if (it.itemCount < 2) {
                                    try {
                                        Thread.sleep(3000)
                                    } catch (e: Exception) {
                                    }
                                    playVideo.startPlay()
                                } else {
                                    it.nextPage(DEFAULT_DELAY_TIME)
                                }
                            }

                        }
                    }

                })
            } else {
//                GlideUtil.loadImg(ivBannerImg, data.localPath)
                GlideUtil.loadImg(ivBannerImg, data.image)
                if (isVisibleToUser) {
                    viewPager?.nextPage(data.playTime * 1000)
                }
                playVideo.visibility = View.GONE
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        if (isVisibleToUser) {
            //当前页面可见是开始轮播
            bannerData?.let { data ->
                if (data.isVideo()) {
                    //video
                    playVideo?.startPlay()
                } else if (data.isImage()) {
                    //img
                    viewPager?.nextPage(data.playTime * 1000)
                }
                return
            }
            viewPager?.nextPage(DEFAULT_DELAY_TIME)
        }
    }

    companion object {

        fun newInstance(BannerData: BannerEntity): Fragment {
            val itemFragment = BannerAdItemFragment()
            itemFragment.bannerData = BannerData
            return itemFragment
        }
    }
}
