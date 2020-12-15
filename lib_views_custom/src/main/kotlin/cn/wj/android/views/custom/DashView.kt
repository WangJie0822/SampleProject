package cn.wj.android.views.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import cn.wj.android.views.dip2px
import cn.wj.android.views.textview.R

/**
 * 虚线控件
 *
 * @author 王杰
 */
class DashView
@JvmOverloads
constructor(
        context: Context,
        attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {

        private const val TAG = "DashView"
        private const val DEFAULT_DASH_WIDTH = 100
        private const val DEFAULT_LINE_WIDTH = 100
        private const val DEFAULT_LINE_HEIGHT = 10
        private const val DEFAULT_LINE_COLOR = 0x9E9E9E

        /** 虚线方向 - 水平方向 */
        private const val HORIZONTAL = LinearLayout.HORIZONTAL

        /** 虚线方向 - 垂直方向 */
        private const val VERTICAL = LinearLayout.VERTICAL

        /** 默认为水平方向 */
        private const val DEFAULT_DASH_ORIENTATION = HORIZONTAL
    }

    /** 间距宽度 */
    private var dashWidth = 3.dip2px(context)

    /** 线段高度 */
    private var lineHeight = 1.dip2px(context)

    /** 线段宽度 */
    private var lineWidth = 3.dip2px(context)

    /** 线段颜色 */
    private var lineColor = Color.parseColor("#cccccc")

    /** 方向 */
    private val dashOrientation: Int

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var widthSize: Int = 0
    private var heightSize: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DashView)
        dashWidth = typedArray.getDimension(R.styleable.DashView_dashWidth, DEFAULT_DASH_WIDTH.toFloat())
        lineHeight = typedArray.getDimension(R.styleable.DashView_lineHeight, DEFAULT_LINE_HEIGHT.toFloat())
        lineWidth = typedArray.getDimension(R.styleable.DashView_lineWidth, DEFAULT_LINE_WIDTH.toFloat())
        lineColor = typedArray.getColor(R.styleable.DashView_lineColor, DEFAULT_LINE_COLOR)
        dashOrientation = typedArray.getInteger(R.styleable.DashView_dashOrientation, DEFAULT_DASH_ORIENTATION)
        mPaint.color = lineColor
        mPaint.strokeWidth = lineHeight
        typedArray.recycle()
        LinearLayout.HORIZONTAL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthSize = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        heightSize = MeasureSpec.getSize(heightMeasureSpec - paddingTop - paddingBottom)
        Log.d(TAG, "onMeasure: $widthSize----$heightSize")
        Log.d(TAG, "dashOrientation: $dashOrientation")
        if (dashOrientation == HORIZONTAL) {
            // 不管在布局文件中虚线高度设置为多少，虚线的高度统一设置为实体线段的高度
            setMeasuredDimension(widthSize, lineHeight.toInt())
        } else {
            setMeasuredDimension(lineHeight.toInt(), heightSize)
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (dashOrientation) {
            VERTICAL -> drawVerticalLine(canvas)
            else -> drawHorizontalLine(canvas)
        }
    }

    /**
     * 画水平方向虚线
     *
     * @param canvas 画布对象
     */
    private fun drawHorizontalLine(canvas: Canvas) {
        var totalWidth = 0f
        canvas.save()
        val pts = floatArrayOf(0f, 0f, lineWidth, 0f)
        // 在画线之前需要先把画布向下平移半个线段高度的位置，目的就是为了防止线段只画出一半的高度
        // 因为画线段的起点位置在线段左下角
        canvas.translate(0f, lineHeight / 2)
        while (totalWidth <= widthSize) {
            canvas.drawLines(pts, mPaint)
            canvas.translate(lineWidth + dashWidth, 0f)
            totalWidth += lineWidth + dashWidth
        }
        canvas.restore()
    }

    /**
     * 画竖直方向虚线
     *
     * @param canvas 画布对象
     */
    private fun drawVerticalLine(canvas: Canvas) {
        var totalWidth = 0f
        canvas.save()
        val pts = floatArrayOf(0f, 0f, 0f, lineWidth)
        // 在画线之前需要先把画布向右平移半个线段宽度的位置，目的就是为了防止线段只画出一半的宽度
        // 因为画线段的起点位置在线段左下角
        canvas.translate(lineHeight / 2, 0f)
        while (totalWidth <= heightSize) {
            canvas.drawLines(pts, mPaint)
            canvas.translate(0f, lineWidth + dashWidth)
            totalWidth += lineWidth + dashWidth
        }
        canvas.restore()
    }
}
