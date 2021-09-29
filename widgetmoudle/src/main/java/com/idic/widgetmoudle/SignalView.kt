package com.idic.widgetmoudle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View

/**
 * 文 件 名: SignalView
 * 创 建 人: sineom
 * 创建日期: 2018/11/27 17:36
 * 修改时间：
 * 修改备注：
 */

/** 自定义信号强弱显示控件
 *
 * Created by sineom on 2017/8/10.
 */

class SignalView : View {
    private var rectOffset: Int = 0
    private var signalValue: Int = 0
    private var signalType: String? = null
    private lateinit var mPaint: Paint
    private var mRectHeight: Int = 0
    private var mRectWidth: Int = 0
    private var mRectCount: Int = 4

    private var mSignalLowColor: Int = 0
    private var mSignalMiddleColor: Int = 0
    private var mSignalHighColor: Int = 0
    private var mRectBorderColor: Int = 0
    private var mRectBorderWidth: Int = 0
    private var mSignalTypeTextColor: Int = 0
    private var mSignalTypeTextSize: Float = 0F

    private var offset: Int = 4

    // 在Java代码中实例化使用控件
    constructor(context: Context) : super(context) {}

    /** 在XML代码中使用控件自动回调
     * 获取XML中自定义属性
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SignalView)
        // 信号柱数目,默认5条
        mRectCount = ta.getInt(R.styleable.SignalView_signalCount, 5)
        // 信号柱间隔，默认4dp
        rectOffset = ta.getInt(R.styleable.SignalView_signalRectInterval, 3)
        // 信号柱宽度，默认1dp
        mRectBorderWidth = ta.getInt(R.styleable.SignalView_rectBorderWidth, 1)
        // 信号柱边框颜色，默认黑色
        mRectBorderColor = ta.getColor(R.styleable.SignalView_rectBorderColor, resources.getColor(R.color.white))
        // 弱信号颜色，默认红色
        mSignalLowColor = ta.getColor(R.styleable.SignalView_signalLowColor, resources.getColor(R.color.colorRed))
        // 中等信号颜色，默认黄色
        mSignalMiddleColor =
            ta.getColor(R.styleable.SignalView_signalMiddleColor, resources.getColor(R.color.colorCar))
        // 强信号颜色，默认绿色
        mSignalHighColor =
            ta.getColor(R.styleable.SignalView_signalHighColor, resources.getColor(R.color.white))

        // 信号类型文本字体颜色，默认黑色
        mSignalTypeTextColor =
            ta.getColor(R.styleable.SignalView_signalTypeTextColor, resources.getColor(R.color.colorBlack))
        // 信号类型文本字体大小，默认12sp
        mSignalTypeTextSize = ta.getDimension(R.styleable.SignalView_signalTypeTextSize, 14f)
        ta.recycle()

        mPaint = Paint()
        mPaint.isAntiAlias = true
        offset = context.resources.getDimension(R.dimen.x4).toInt()
    }

    fun setSignalValue(signalValue: Int) {

        if (this.signalValue == signalValue) return

        this.signalValue = signalValue
        // 如果值大于信号柱，抛出异常
        if (signalValue > mRectCount) {
            this.signalValue = mRectCount
        }
        // 更新值后，重新绘制SignalView
        this.invalidate()
    }

    fun setSignalTypeText(signalType: String) {
        this.signalType = signalType
    }


    // 当View大小改变时，获取View相关属性，初始化
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRectHeight = height
        mRectWidth = width / mRectCount
    }

    /** 测量
     * 如果不覆写onMeasure方法，SignalView值支持EXACTY模式(具体值或match_parent)
     * 不支持AT_MOST模式，即wrap_content
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        var height = 0
        // 测量模式，从xml可知
        val specMode = View.MeasureSpec.getMode(heightMeasureSpec)
        // 测量大小,从xml中获取
        val specSize = View.MeasureSpec.getSize(heightMeasureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            height = specSize
        } else {
            height = context.resources.getDimension(R.dimen.y50).toInt()
            // wrap_content模式，选择最小值
            if (specMode == View.MeasureSpec.AT_MOST) {
                height = Math.min(height, specSize)
            }
        }
        mRectHeight = height
        return height
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        var width = 0
        // 测量模式，从xml可知
        val specMode = View.MeasureSpec.getMode(widthMeasureSpec)
        // 测量大小,从xml中获取
        val specSize = View.MeasureSpec.getSize(widthMeasureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            width = specSize
        } else {
            width = context.resources.getDimension(R.dimen.x80).toInt()
            // wrap_content模式，选择最小值
            if (specMode == View.MeasureSpec.AT_MOST) {
                width = Math.min(width, specSize)
            }
        }
        return width
    }

    /**绘制
     * 绘制信号条，根据强弱显示不同颜色
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制信号类型文字
        if (!TextUtils.isEmpty(signalType)) {
            mPaint?.let {
                it.color = mSignalTypeTextColor
                it.textSize = mSignalTypeTextSize
                it.strokeWidth = 1f
                it.style = Paint.Style.FILL
            }

            canvas.drawText(signalType!!, 0f, mSignalTypeTextSize, mPaint)
        }

        mPaint.strokeWidth = mRectBorderWidth.toFloat()
        // 绘制信号条，根据强弱显示不同颜色
        for (i in 0 until mRectCount) {
            // 前signalValue信号柱绘制实心颜色
            if (i < signalValue) {
                when (signalValue) {
                    1 -> {
                        mPaint.color = mSignalLowColor
                    }
                    in 2..3 -> {
                        mPaint.color = mSignalMiddleColor
                    }
                    4 -> {
                        mPaint.color = mSignalHighColor
                    }
                    else -> {

                    }
                }
                mPaint.style = Paint.Style.FILL
            } else {
                mPaint.color = mRectBorderColor
                mPaint.style = Paint.Style.STROKE
            }
            // 绘制信号矩形柱
            canvas.drawRect(
                (mRectWidth * i + rectOffset).toFloat() + offset,
                (mRectHeight.toDouble() * (mRectCount - i).toDouble() * 0.2).toFloat(),
                (mRectWidth * (i + 1)).toFloat(),
                mRectHeight.toFloat(), mPaint
            )
        }
        if (signalValue == 0) {
            mPaint.color = mSignalLowColor
            mPaint.style = Paint.Style.STROKE
            mPaint.strokeWidth = 3f
            canvas.drawLine(0F, 0F, mRectWidth * 2F, mRectHeight.toFloat() / 2, mPaint)
            canvas.drawLine(
                0F, mRectHeight.toFloat() / 2,
                mRectWidth * 2F, 0F, mPaint
            )
        }
    }
}
