package com.idic.widgetmoudle

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * 文 件 名: RandomNumber
 * 创 建 人: sineom
 * 创建日期: 2019-07-05 11:14
 * 修改时间：
 * 修改备注：
 */
const val loopTime = 1000

class RandomNumber @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr),
    SurfaceHolder.Callback {


    private var paint: Paint

    private var drawJob: Job? = null

    private var curNumber = 0

    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    init {
        holder.addCallback(this)
        paint = Paint().apply {
            isAntiAlias = true
            textSize = 150F
            strokeWidth = 10F
            color = Color.BLACK
        }


    }

    override fun surfaceCreated(holder: SurfaceHolder) {

    }

    private fun drawEmpty() {
        with(paint) {
            isAntiAlias = true
            textSize = 20F
            strokeWidth = 5F
            color = Color.BLACK
        }
        val x45 = resources.getDimension(R.dimen.x80)
        val y50 = resources.getDimension(R.dimen.y1)
        val canvas = holder.lockCanvas()
        canvas.drawPaint(clearPaint)
        canvas.drawText(
            "幸运值正在补充中....",
            (measuredWidth / 2).toFloat() - x45,
            (measuredHeight / 2).toFloat() - y50,
            paint
        )
        holder.unlockCanvasAndPost(canvas)
    }

    fun setRandomData(validData: List<Int>) {

        if (validData.isEmpty()) {
            drawEmpty()
            return
        }

        drawJob = GlobalScope.launch {
            with(paint) {
                isAntiAlias = true
                textSize = 150F
                strokeWidth = 10F
                color = Color.BLACK
            }

            val size = validData.size
            val x45 = resources.getDimension(R.dimen.x45)
            val y50 = resources.getDimension(R.dimen.y1)
            if (size < 1) {
                with(paint) {
                    isAntiAlias = true
                    textSize = 20F
                    strokeWidth = 5F
                    color = Color.BLACK
                }
                val canvas = holder.lockCanvas()
                canvas.drawPaint(clearPaint)
                canvas.drawText(
                    "幸运值正在补充中....",
                    (measuredWidth / 2).toFloat() - x45 * 2,
                    (measuredHeight / 2).toFloat() - y50,
                    paint
                )
                holder.unlockCanvasAndPost(canvas)
                return@launch
            }
            delay(100)
            var str = ""
            val startTime = System.currentTimeMillis()
            var curTime = startTime + loopTime
            while (curTime - startTime <= loopTime) {
                val nextInt = Random.nextInt(size)
                curNumber = validData[nextInt]
                str = "" + curNumber
                val canvas = holder.lockCanvas()
                canvas.drawPaint(clearPaint)
                canvas.drawText(
                    str,
                    (measuredWidth / 2).toFloat() - x45 * str.length,
                    (measuredHeight / 2).toFloat() - y50,
                    paint
                )
                holder.unlockCanvasAndPost(canvas)
                curTime = System.currentTimeMillis()
                delay(50)
            }
        }
    }


    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        drawJob?.cancel()
    }
}
