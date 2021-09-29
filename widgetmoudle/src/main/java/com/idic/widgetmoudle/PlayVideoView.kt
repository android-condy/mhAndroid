package com.idic.widgetmoudle

import android.content.Context
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.SurfaceTexture
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Surface
import android.view.TextureView
import android.view.WindowManager
import com.elvishew.xlog.XLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


/**
 * 文 件 名: PlayVideoView
 * 创 建 人: sineom
 * 创建日期: 2018/12/7 14:09
 * 修改时间：
 * 修改备注：
 */

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PlayVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextureView(
    context,
    attrs,
    defStyleAttr
), TextureView.SurfaceTextureListener {

    private val tag = "playView"

    private var mVideoWidth: Int = 0//视频宽度
    private var mVideoHeight: Int = 0//视频高度

    val CENTER_CROP_MODE = 1//中心裁剪模式
    val CENTER_MODE = 2//一边中心填充模式

    private var playListener: PlayListener? = null


    private val mScreentRect: Rect

    init {
        surfaceTextureListener = this
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        mScreentRect = Rect(0, 0, dm.widthPixels, dm.heightPixels)
    }

    private var mMediaPlayer: MediaPlayer? = null
    private var surface: Surface? = null


    //视频完整路径
    var videoPath = ""

    //是否循环播放
    var loopPlay = false


    fun setPayListener(listener: PlayListener) {
        playListener = listener
    }

    fun startPlay() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (isAvailable && !videoPath.isNullOrEmpty()) {
                    mMediaPlayer?.let {
                        if (it.isPlaying) {
                            it.stop()
                        }
                        it.release()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                PlayThread().start()
            }
        }
    }

    fun stopPlay() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (isAvailable && !videoPath.isNullOrEmpty()) {
                    mMediaPlayer?.let {
                        it.stop()
                        it.release()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onPause() {
        try {
            mMediaPlayer?.pause()
        } catch (e: Exception) {
        }
    }


    fun onResume() {
        try {
            mMediaPlayer?.start()
        } catch (e: Exception) {
        }
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        this.surface = Surface(surfaceTexture)
        PlayThread().start()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        updateTextureViewSize(CENTER_CROP_MODE)
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        this.surface = null
        try {
            mMediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
        } catch (e: Exception) {
        }
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

    }


    private inner class PlayThread : Thread() {

        @RequiresApi(Build.VERSION_CODES.N)
        override fun run() {
            try {
                val file = File(videoPath)
                if ((!file.exists() && !file.isFile)) {
                    XLog.d("play video path isn't exist path---->$videoPath")
                    sleep(1000)
                    playListener?.error()
                    return
                }
                mMediaPlayer = MediaPlayer()
                mMediaPlayer?.let { mediaPlayer ->
                    mediaPlayer.setDataSource(videoPath)
                    mediaPlayer.setSurface(surface)
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.isLooping = loopPlay
                    mediaPlayer.setOnPreparedListener {
                        XLog.d("play setOnPreparedListener")
                        playListener?.startPlay(it)
                    }
                    mediaPlayer.setOnCompletionListener {
                        XLog.d("play setOnCompletionListener")
                        playListener?.completed()
                    }
                    mediaPlayer.setOnErrorListener { mp, what, extra ->
                        XLog.d("play setOnErrorListener")
                        playListener?.error()
                        true
                    }
                    mediaPlayer.setOnVideoSizeChangedListener { mp, width, height ->
                        mVideoHeight = mediaPlayer.videoHeight
                        mVideoWidth = mediaPlayer.videoWidth
                        updateTextureViewSize(CENTER_MODE)
                    }
                }
            } catch (e: Exception) {
                XLog.d("play error--->${e.message}")
                sleep(1000)
                playListener?.error()
            }
        }
    }

    fun updateTextureViewSize(mode: Int) {
        if (mode == CENTER_MODE) {
            updateTextureViewSizeCenter()
        } else if (mode == CENTER_CROP_MODE) {
            updateTextureViewSizeCenterCrop()
        }
    }

    private fun updateTextureViewSizeCenter() {
        val sx = width.toFloat() / mVideoWidth
        val sy = height.toFloat() / mVideoHeight

        val matrix = Matrix()
        //第1步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((width - mVideoWidth) / 2F, (height - mVideoHeight) / 2F)

        //第2步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(mVideoWidth / width.toFloat(), mVideoHeight / height.toFloat())

        //第3步,等比例放大或缩小,直到视频区的一边和View一边相等.如果另一边和view的一边不相等，则留下空隙
        if (sx >= sy) {
            matrix.postScale(sy, sy, width / 2F, height / 2F)
        } else {
            matrix.postScale(sx, sx, width / 2F, height / 2F)
        }

        setTransform(matrix)
        postInvalidate()
    }

    //重新计算video的显示位置，裁剪后全屏显示
    private fun updateTextureViewSizeCenterCrop() {
        val sx = width.toFloat() / mVideoWidth
        val sy = height.toFloat() / mVideoHeight

        val matrix = Matrix()

        val maxScale = Math.max(sx, sy)

        matrix.preTranslate((width - mVideoWidth) / 2F, (height - mVideoHeight) / 2F)

        //第2步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(mVideoWidth / width.toFloat(), mVideoHeight / height.toFloat())

        //第3步,等比例放大或缩小,直到视频区的一边超过View一边, 另一边与View的另一边相等. 因为超过的部分超出了View的范围,所以是不会显示的,相当于裁剪了.
        matrix.postScale(maxScale, maxScale, width / 2F, height / 2F)//后两个参数坐标是以整个View的坐标系以参考的

        setTransform(matrix)
        postInvalidate()
    }

    interface PlayListener {

        //开始播放
        fun startPlay(mediaPlayer: MediaPlayer)

        fun completed()

        fun error()
    }

}
