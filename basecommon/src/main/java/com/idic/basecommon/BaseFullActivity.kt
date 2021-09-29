package com.idic.basecommon

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import com.dueeeke.videoplayer.player.AbstractPlayer
import com.dueeeke.videoplayer.player.VideoView
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Job

//默认轮询的时间
const val delayqueryTime = 2000L

open class BaseFullActivity : AppCompatActivity(), VideoView.OnStateChangeListener {

    val disposables = ArrayList<Disposable>()

    val jobs = ArrayList<Job>()

    //双击
    val mHits = LongArray(2)

    //是否监听信号强度
    private var listenerSignal = false
    private var mTelephonyManager: TelephonyManager? = null
    private var mListener: PhoneStatListener? = null

    //信号强度
    protected var mGsmSignalStrength: Int = 0
    private var mNetWorkBroadCastReciver: NetWorkBroadCastReciver? = null

    protected var mediaPlayer: MediaPlayer? = null

    //背景视频
    protected var backgroundVideo: VideoView<AbstractPlayer>? = null

    //轮播的视频
    protected var loopVideoView: VideoView<AbstractPlayer>? = null

    protected val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("condy",this@BaseFullActivity.localClassName)
        hideBottomUIMenu()
        hideBottomBar()
    }

    override fun onResume() {
        super.onResume()
        try {
            hideBottomUIMenu()
            mediaPlayer?.start()
            if (listenerSignal) {
                //重新注册信号监听
                mTelephonyManager?.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
            }
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            mediaPlayer?.pause()
            if (listenerSignal) {
                //用户不在当前页面时，停止监听
                mTelephonyManager?.listen(mListener, PhoneStateListener.LISTEN_NONE)
            }
        } catch (e: Exception) {
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            mediaPlayer?.release()
        } catch (e: Exception) {
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            mediaPlayer?.release()
            jobs.forEach { it.cancel() }
            disposables.forEach { it.dispose() }
            disposables.clear()
            if (listenerSignal) {
                unregisterReceiver(mNetWorkBroadCastReciver)
            }
        } catch (e: Exception) {
        }
    }

    //启用监听信号强度
    protected fun listenerSignalChange() {
        listenerSignal = true
        //获取telephonyManager
        mTelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        //开始监听
        mListener = PhoneStatListener()
        /**由于信号值变化不大时，监听反应不灵敏，所以通过广播的方式同时监听wifi和信号改变更灵敏 */
        mNetWorkBroadCastReciver = NetWorkBroadCastReciver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION)
        registerReceiver(mNetWorkBroadCastReciver, intentFilter)
    }

    inner class PhoneStatListener : PhoneStateListener() {
        //获取信号强度
        override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
            super.onSignalStrengthsChanged(signalStrength)

            when (mTelephonyManager!!.networkType) {
                TelephonyManager.NETWORK_TYPE_LTE -> {
                    //这个ltedbm 是4G信号的值
                    val signalinfo = signalStrength.toString()
                    val parts = signalinfo.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    mGsmSignalStrength = Integer.parseInt(parts[9])
                    if (mGsmSignalStrength > 2147000) {
                        mGsmSignalStrength = Integer.parseInt(parts[4])
                    }
                    Log.d("NetWorkUtil", "网络：LTE 信号强度：$mGsmSignalStrength======Detail:$signalinfo")
                }
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_HSUPA,
                TelephonyManager.NETWORK_TYPE_UMTS -> {
                    //这个dbm 是2G和3G信号的值
                    val asu = signalStrength.gsmSignalStrength
                    mGsmSignalStrength = -113 + 2 * asu
                }
                else -> {
                    //这个dbm 是2G和3G信号的值
                    val asu = signalStrength.gsmSignalStrength
                    mGsmSignalStrength = -113 + 2 * asu
                }
            }
            //获取网络信号强度
            //获取0-4的5种信号级别，越大信号越好,但是api23开始才能用
            //            int level = signalStrength.getLevel();

            //网络信号改变时，获取网络信息
//            if (isFastMobileNetwork()) {
//                val signalInfo = signalStrength.toString()
//                val params = signalInfo.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                mGsmSignalStrength = Integer.parseInt(params[9])
//            } else {
//                mGsmSignalStrength = signalStrength.gsmSignalStrength
//                val asu = signalStrength.gsmSignalStrength
//                mGsmSignalStrength = -113 + 2 * asu
//            }
            getNetWorkInfo()
        }
    }

    /**
     * 判断网络速度
     */
    private fun isFastMobileNetwork(): Boolean {
        return mTelephonyManager?.networkType == TelephonyManager.NETWORK_TYPE_LTE
    }

    //接收网络状态改变的广播
    internal inner class NetWorkBroadCastReciver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            getNetWorkInfo()
        }
    }

    /**
     * 获取网络的信息
     */
    private fun getNetWorkInfo() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivityManager.activeNetworkInfo
        if (info != null && info.isAvailable) {
            when (info.type) {
                ConnectivityManager.TYPE_WIFI -> {
                    //wifi
                    val manager =
                        applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val connectionInfo = manager.connectionInfo
                    val rssi = connectionInfo.rssi
                    setSignal(getSignalLevel(rssi))
                }
                ConnectivityManager.TYPE_MOBILE -> {
                    //移动网络,可以通过TelephonyManager来获取具体细化的网络类型
//                    val signalInfo = signalStrength.toString()
//                    val params = signalInfo.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
//                    if (isFastMobileNetwork()) {
//                        Integer.parseInt(params[11]);
//                    } else {
//                    }
                    setSignal(getSignalLevel(mGsmSignalStrength))
                }
            }
        } else {
            Log.d("NetWorkUtil", "没有可用网络")
            setSignal(getSignalLevel(0))
        }
    }

    private fun getSignalLevel(signal: Int): Int {

        return when {
            signal > -44 -> 0
            signal >= -97 -> 5
            signal >= -105 -> 4
            signal >= -110 -> 3
            signal >= -120 -> 2
            signal >= -140 -> 1
            else -> 0
        }
    }

    //重写该方法可以设置信号强度
    open fun setSignal(level: Int) {

    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    @SuppressLint("ObsoleteSdkInt")
    protected fun hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT in 12..18) { // lower api
            val v = this.window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView = window.decorView
            val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
            decorView.systemUiVisibility = uiOptions
        }
    }

    override fun onPlayStateChanged(playState: Int) {
        if (playState == VideoView.STATE_PLAYBACK_COMPLETED || playState == VideoView.STATE_ERROR) {
            playLoopVideo()
        }
    }

    open fun playLoopVideo() {

    }

    override fun onPlayerStateChanged(playerState: Int) {
    }


}
