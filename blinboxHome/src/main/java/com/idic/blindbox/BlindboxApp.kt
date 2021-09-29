package com.idic.blindbox

import android.app.Application
import com.dueeeke.videoplayer.exo.ExoMediaPlayerFactory
import com.dueeeke.videoplayer.player.VideoViewConfig
import com.dueeeke.videoplayer.player.VideoViewManager
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.idic.basecommon.BaseApp

/**
 * 文 件 名: App
 * 创 建 人: sineom
 * 创建日期: 2019-08-08 14:01
 * 修改时间：
 * 修改备注：
 */

class BlindboxApp : BaseApp() {

    override fun initModuleApp(application: Application) {

        VideoViewManager.setConfig(
            VideoViewConfig.newBuilder()
                .setPlayerFactory(ExoMediaPlayerFactory.create())
                .build()
        )
    }

    override fun initModuleData(application: Application) {
    }


}