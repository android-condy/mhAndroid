package com.idic.widgetmoudle

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.idic.widgetmoudle.utils.ViewPagerHelperUtils


/**
 * 文 件 名: BannerViewPager
 * 创 建 人: sineom
 * 创建日期: 2019/2/22 16:04
 * 修改时间：
 * 修改备注：
 */

class BannerViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs), View.OnTouchListener {
    /**
     * attrs
     */
    private var mLoopTime = 2000
    private var isLoop = false //是否自动轮播
    private var mSwitchTime: Int
    private var mLoopMaxCount = 1
    private var isTouch = true
    private var isSlide: Boolean = false //是否可以轮播

    private var mChildCount = 0

    var itemCount = 0
        private set

    /**
     * others
     */
    private var mCurrentIndex: Int = 0
    private val mInflater: LayoutInflater
    private val mScreentRect: Rect

    private var getItem: ((Int) -> Fragment)? = null
    /**
     * handle
     */
    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == LOOP_MSG) {
                if (isSlide) {
                    mCurrentIndex = currentItem //重新获取index
                    if (mCurrentIndex >= LOOP_COUNT / 2) {
                        mCurrentIndex++
                    }
                    if (mCurrentIndex >= LOOP_COUNT) {
                        mCurrentIndex = LOOP_COUNT / 2
                    }
                    currentItem = mCurrentIndex
                    if (isLoop) {
                        this.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime.toLong())
                    }

                }
            }
        }
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BannerViewPager)
        isLoop = ta.getBoolean(R.styleable.BannerViewPager_banner_isloop, false)
        mLoopTime = ta.getInteger(R.styleable.BannerViewPager_banner_looptime, 2000)
        mSwitchTime = ta.getInteger(R.styleable.BannerViewPager_banner_switchtime, 600)
        mLoopMaxCount =
            ta.getInteger(R.styleable.BannerViewPager_banner_loop_max_count, mLoopMaxCount)
        isTouch = ta.getBoolean(R.styleable.BannerViewPager_banner_isTouch, true)
        ta.recycle()
        mInflater = LayoutInflater.from(context)
        setOnTouchListener(this)
        ViewPagerHelperUtils.initSwitchTime(getContext(), this, mSwitchTime)

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        mScreentRect = Rect(0, 0, dm.widthPixels, dm.heightPixels)
    }

    /**
     * 判断是否移出可见屏幕外
     *超出屏幕
     * @return
     */
    val isOutVisiableWindow: Boolean
        get() {
            val pos = IntArray(2)
            this.getLocationOnScreen(pos)
            return pos[1] <= 0 || pos[1] > mScreentRect.height() - height
        }

    /**
     * @param itemCount item的个数
     * 开始设置pager
     */
    fun setPagerFragment(itemCount: Int, fm: FragmentManager, item: ((Int) -> Fragment)) {
        getItem = item
        this.itemCount = itemCount
        isSlide = itemCount > mLoopMaxCount
        adapter = CusViewPagerAdapter(itemCount, fm)
        currentItem = if (isSlide) {
            val index = LOOP_COUNT / 2 % itemCount
            //这样能保证从第一页开始
            LOOP_COUNT / 2 - index + itemCount
        } else {
            0
        }
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (isTouch) {
            super.onInterceptTouchEvent(ev)
        } else
            false
    }


    /**
     * 当有触摸时停止
     */
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (!isTouch) return true
        mHandler.removeMessages(LOOP_MSG)
        when (motionEvent.action) {
            MotionEvent.ACTION_UP -> if (isLoop) {
                mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime.toLong())
            }

            else -> {
            }
        }
        return false
    }

    /**
     * 手动停止
     */
    fun stopAnim() {
        if (isLoop) {
            mHandler.removeMessages(LOOP_MSG)
        }
    }

    /**
     * 手动开始
     */
    fun startAnim() {
        if (isLoop) {
            mHandler.removeMessages(LOOP_MSG)
            mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime.toLong())
        }
    }

    /**
     * 收到开始下一页
     *
     * @param mLoopTime 延迟多久后开始
     */
    fun nextPage(mLoopTime: Long) {
        mHandler.removeMessages(LOOP_MSG)
        mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime)
    }

    /**
     * 移除handler的通知
     */
    fun removeMessage() {
        mHandler.removeMessages(LOOP_MSG)
    }

    /**
     * 配置adapter
     */
    private inner class CusViewPagerAdapter(
        var listSize: Int,
        fm: FragmentManager
    ) : FragmentStatePagerAdapter(fm) {

        fun updateListSize(size: Int) {
            listSize = size

        }

        override fun getItem(position: Int): Fragment {
            if (getItem == null) {
                throw Throwable("fun getItem is not null")
            }
            return getItem?.invoke(position % listSize)!!
        }

        override fun getCount(): Int {
            return if (isSlide) {
                listSize + LOOP_COUNT
            } else {
                listSize
            }
        }

        override fun getItemPosition(`object`: Any): Int {
            if (mChildCount > 0) {
                mChildCount--
                return PagerAdapter.POSITION_NONE
            }
            return super.getItemPosition(`object`)
        }

        override fun saveState(): Parcelable? {
            return null
        }
    }

    /**
     * 如果退出了，自动停止，进来则自动开始
     *
     * @param visibility
     */
    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (isLoop) {
            if (visibility == View.VISIBLE) {
                startAnim()
            } else {
                stopAnim()
            }
        }
    }

    override fun detachAllViewsFromParent() {
        super.detachAllViewsFromParent()
        mHandler.removeCallbacksAndMessages(null)
    }

    companion object {
        private val TAG = "BannerViewPager"
        private val LOOP_MSG = 0x1001
        private val LOOP_COUNT = 5000
    }
}
