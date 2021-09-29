package com.idic.widgetmoudle

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout


/**
 * Created by sineom on 2017/10/29.
 */
class ZoomIndicator @JvmOverloads constructor(
    /**
     * normal and logic
     */
    private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(mContext, attrs, defStyleAttr), ViewPager.OnPageChangeListener {

    private val mAlpha_min: Float
    private val mScale_max: Float
    private var mLastPosition: Int = 0
    private val mSelector: Int
    private val mLeftMargin: Int
    private var isFirst = true
    private var mCount = 0
    private val mDismissOpen: Boolean //是否隐藏底部指示器，当“立即体验”的view 出现的时候。
    /**
     * ui
     */
    private val mOpenView: View? = null

    init {
        val ta = mContext.obtainStyledAttributes(attrs, R.styleable.ZoomIndicator)
        mSelector = ta.getResourceId(
            R.styleable.ZoomIndicator_zoom_selector,
            R.drawable.indicator_white_circle
        )
        mLeftMargin = ta.getDimension(R.styleable.ZoomIndicator_zoom_leftmargin, 15f).toInt()
        mAlpha_min = ta.getFloat(R.styleable.ZoomIndicator_zoom_alpha_min, 0.5f)
        mScale_max = ta.getFloat(R.styleable.ZoomIndicator_zoom_max, 1.5f)
        mDismissOpen = ta.getBoolean(R.styleable.ZoomIndicator_zoom_dismiss_open, false)
        gravity = Gravity.CENTER
        ta.recycle()
    }


    fun addPagerData(viewPager: BannerViewPager) {
        mCount = viewPager.itemCount
        //这里加小圆点
        for (i in 0 until mCount) {
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (i == mCount - 1) { //防止indicator的宽度为wrap_content，放大后被遮盖住的问题
                params.setMargins(mLeftMargin, 0, mLeftMargin, 0)
            } else {
                params.setMargins(mLeftMargin, 0, 0, 0)
            }

            val imageView = ImageView(mContext)
            /*    if (mSelector == 0){
                }else {
                }*/
            imageView.setBackgroundResource(mSelector)
            imageView.layoutParams = params
            imageView.alpha = mAlpha_min

            addView(imageView)
        }
        viewPager.addOnPageChangeListener(this)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (isFirst) {
            isFirst = false
            if (getChildAt(0) != null) {
                targetViewAnim(getChildAt(0), ANIM_OUT)
            }

        }

    }

    override fun onPageSelected(position: Int) {
        /*  viewPagerSeleted(mBannerStyle.getRealPosition(mMode,position));
        //用于glide是否显示 openview
        mBannerStyle.getPageSeleted(position);*/
        showStartView(position % mCount)
        viewPagerSeleted(position % mCount)
    }


    /**
     * 用于viewpager 滑动时，底部圆点的状态显示
     *
     * @param position
     */
    private fun viewPagerSeleted(position: Int) {
        val lastView: View?
        if (mLastPosition >= 0) {
            lastView = getChildAt(mLastPosition)
            if (lastView != null) {
                lastView.isSelected = false
                targetViewAnim(lastView, ANIM_IN)
            }
        }
        val currentView = getChildAt(position)
        if (currentView != null) {
            currentView.isSelected = true
            targetViewAnim(currentView, ANIM_OUT)
        }
        mLastPosition = position
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    /**
     * 显示最后一页的状态
     *
     * @param position
     */
    @SuppressLint("ObjectAnimatorBinding")
    private fun showStartView(position: Int) {
        // 最后一页
        if (position == mCount - 1) {
            if (mOpenView != null) {
                mOpenView.visibility = View.VISIBLE
                val animator = ObjectAnimator.ofFloat(
                    mOpenView,
                    "alpha", 0F, 1F
                )
                animator.duration = 500
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.start()
                if (mDismissOpen) {
                    visibility = View.GONE
                }
            }
        } else {
            if (mOpenView != null) {

                mOpenView.visibility = View.GONE

                if (mDismissOpen) {
                    visibility = View.VISIBLE
                }
            }
        }
    }


    /**
     * 用于小圆点的放大缩小
     *
     * @param view
     * @param type
     */
    private fun targetViewAnim(view: View, type: Int) {
        val animatorSet = AnimatorSet()
        var scaleX: ObjectAnimator? = null
        var scaleY: ObjectAnimator? = null
        var alpha: ObjectAnimator? = null
        if (type == ANIM_OUT) {
            scaleX = ObjectAnimator.ofFloat(view, "scaleX", SCALE_MIN, mScale_max)
            scaleY = ObjectAnimator.ofFloat(view, "scaleY", SCALE_MIN, mScale_max)
            alpha = ObjectAnimator.ofFloat(view, "alpha", mAlpha_min, ALPHA_MAX)
            animatorSet.duration = ANIM_OUT_TIME.toLong()
        } else {
            scaleX = ObjectAnimator.ofFloat(view, "scaleX", mScale_max, SCALE_MIN)
            scaleY = ObjectAnimator.ofFloat(view, "scaleY", mScale_max, SCALE_MIN)
            alpha = ObjectAnimator.ofFloat(view, "alpha", ALPHA_MAX, mAlpha_min)
            animatorSet.duration = ANIM_IN_TIME.toLong()
        }
        animatorSet.play(scaleX).with(scaleY).with(alpha)

        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.start()

    }

    companion object {
        private val TAG = "zsr"
        /**
         * const
         */
        private val ANIM_IN = 0x1001
        private val ANIM_OUT = 0x1002
        private val ALPHA_MAX = 1.0f

        private val SCALE_MIN = 1.0f
        private val ANIM_OUT_TIME = 400
        private val ANIM_IN_TIME = 300
    }


}
