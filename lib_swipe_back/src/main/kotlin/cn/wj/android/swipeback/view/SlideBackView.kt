package cn.wj.android.swipeback.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.core.content.ContextCompat
import cn.wj.android.swipeback.R
import cn.wj.android.swipeback.constants.SwipeBackConfig
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan

/**
 * 侧滑返回 View
 *
 * - 创建时间：2020/1/2
 *
 * @author 王杰
 */
class SlideBackView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(context, attrs, defStyleAttr) {

    /** 圆的画笔 */
    private val mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    /** 路径画笔 */
    private val mPathPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** 贝塞尔曲线 */
    private val mPath = Path()

    /** 切角路径插值器 */
    private val mTangentAngleInterpolator: Interpolator
    /** 进度插值器 */
    private val mProgressInterpolator: Interpolator = DecelerateInterpolator()

    /** 释放动画 */
    private var valueAnimator: ValueAnimator? = null

    /** 圆的 X 轴坐标 */
    private var mCirclePointX = 0f
    /** 圆的 Y 轴坐标 */
    private var mCirclePointY = 0f

    /** 当前进度 */
    var mProgress = 0f

    init {
        /* 初始化画笔 */
        // 圆的画笔
        // 抗锯齿
        mCirclePaint.isAntiAlias = true
        // 防抖动
        mCirclePaint.isDither = true
        // 填充方式
        mCirclePaint.style = Paint.Style.FILL
        // 画笔颜色
        mCirclePaint.color = SwipeBackConfig.bgColor

        // 路径画笔
        // 抗锯齿
        mPathPaint.isAntiAlias = true
        // 防抖动
        mPathPaint.isDither = true
        // 填充方式
        mPathPaint.style = Paint.Style.FILL
        // 画笔颜色
        mPathPaint.color = SwipeBackConfig.bgColor

        /* 初始化差值器 */
        mTangentAngleInterpolator = DecelerateInterpolator()

        if (null == SwipeBackConfig.content) {
            // 加载显示图片
            SwipeBackConfig.content = ContextCompat.getDrawable(getContext(), R.drawable.swipe_vector_keyboard_arrow_left_gray)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val iHeight = (2 * SwipeBackConfig.circleRadius + paddingTop + paddingBottom).roundToInt()
        val iWidth = (SwipeBackConfig.dragWidth * mProgress + paddingStart + paddingEnd).roundToInt()
        val measureWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                width
            }
            MeasureSpec.AT_MOST -> {
                iWidth.coerceAtMost(width)
            }
            else -> {
                iWidth
            }
        }
        val measureHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                height
            }
            MeasureSpec.AT_MOST -> {
                iHeight.coerceAtMost(height)
            }
            else -> {
                iHeight
            }
        }
        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val count = canvas.save()
        val tranY: Float =
                (height - getValueByLine(height.toFloat(), SwipeBackConfig.targetHeight.toFloat(), mProgress)) / 2
        canvas.translate(0f, tranY)
        canvas.drawPath(mPath, mPathPaint)
        // 画圆
        canvas.drawCircle(mCirclePointX, mCirclePointY, SwipeBackConfig.circleRadius, mCirclePaint)
        val content = SwipeBackConfig.content
        if (content != null) {
            canvas.save()
            //剪切矩形区域
            canvas.clipRect(content.bounds)
            //绘制
            content.draw(canvas)
            canvas.restore()
        }
        canvas.restoreToCount(count)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updatePathLayout()
    }

    /**
     * 获取当前值
     *
     * @param start    起点
     * @param end      终点
     * @param progress 进度
     *
     * @return 某一个坐标差值的百分百，计算贝塞尔的关键
     */
    private fun getValueByLine(
            start: Float,
            end: Float,
            progress: Float
    ): Float {
        return start + (end - start) * progress
    }

    private fun updatePathLayout() {
        val progress: Float = mProgressInterpolator.getInterpolation(mProgress)
        // 获取可绘制区域高度宽度
        val w = getValueByLine(0f, SwipeBackConfig.dragWidth.toFloat(), mProgress)
        val h = getValueByLine(height.toFloat(), SwipeBackConfig.targetHeight.toFloat(), mProgress)
        // Y 对称轴的参数，圆的圆心坐标，半径等
        val cPointY = h / 2
        val cPointX = w - SwipeBackConfig.circleRadius
        // 控制点结束 X 坐标
        val endControlX: Float = SwipeBackConfig.targetGravityWidth.toFloat()
        mCirclePointX = cPointX
        mCirclePointY = cPointY

        // 重置
        mPath.reset()
        mPath.moveTo(0f, 0f)

        /* 计算上面部分的结束点和控制点 */
        // 角度转弧度
        val angle = SwipeBackConfig.tangentAngle * mTangentAngleInterpolator.getInterpolation(progress)
        val radian = Math.toRadians(angle.toDouble())
        // X 轴偏移
        val x = (cos(radian) * SwipeBackConfig.circleRadius).toFloat()
        val y = (sin(radian) * SwipeBackConfig.circleRadius).toFloat()
        // 上部分结束点位置
        val tEndPointX = cPointX + x
        val tEndPointY = cPointY - y
        // 控制点 X 坐标变化
        val lControlPointX = getValueByLine(0f, endControlX, progress)
        // 控制点与结束点之间的距离
        val tWidth = tEndPointX - lControlPointX
        // 控制点 Y 轴坐标
        val tHeight = (tWidth * tan(radian)).toFloat()
        val lControlPointY = tEndPointY - tHeight
        // 上面贝塞尔曲线
        mPath.quadTo(lControlPointX, lControlPointY, tEndPointX, tEndPointY)
        // 连接到下面
        mPath.lineTo(tEndPointX, cPointY + cPointY - tEndPointY)
        // 下面贝塞尔曲线
        mPath.quadTo(lControlPointX, cPointY + cPointY - lControlPointY, 0f, h)
        // 更新内容部分Drawable
        updateContentLayout(cPointX, cPointY)
    }

    /**
     * 对内容部分进行测量并设置
     *
     * @param cx     cPointX
     * @param cy     cPointY
     */
    private fun updateContentLayout(
            cx: Float,
            cy: Float
    ) {
        val content = SwipeBackConfig.content
        if (content != null) {
            val margin: Int = SwipeBackConfig.contentMargin
            val l = (cx - SwipeBackConfig.circleRadius + margin).toInt()
            val r = (cx + SwipeBackConfig.circleRadius - margin).toInt()
            val t = (cy - SwipeBackConfig.circleRadius + margin).toInt()
            val b = (cy + SwipeBackConfig.circleRadius - margin).toInt()
            content.setBounds(l, t, r, b)
        }
    }

    /**
     * 设置进度
     *
     * @param progress 进度
     */
    fun setProgress(progress: Float) {
        mProgress = progress
        requestLayout()
    }

    /**
     * 添加释放动作
     */
    fun release() {
        if (valueAnimator == null) {
            val animator = ValueAnimator.ofFloat(mProgress, 0f)
            animator.interpolator = DecelerateInterpolator()
            animator.duration = 200
            animator.addUpdateListener { animation ->
                val value = animation.animatedValue
                if (value is Float) {
                    setProgress(value)
                }
            }
            valueAnimator = animator
        } else {
            valueAnimator?.cancel()
            valueAnimator?.setFloatValues(mProgress, 0f)
        }
        valueAnimator?.start()
    }
}