@file:Suppress("unused")

package cn.wj.android.views.custom

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import kotlin.math.sqrt

/** 蒙版形状 - 圆形 */
const val MASKING_SHAPE_CIRCLE = 0

/** 蒙版形状 - 矩形 */
const val MASKING_SHAPE_RECTANGULAR = 1

/** 引导位置 - 左 */
const val GUIDE_GRAVITY_LEFT = 0

/** 引导位置 - 右 */
const val GUIDE_GRAVITY_RIGHT = 1

/** 引导位置 - 上 */
const val GUIDE_GRAVITY_TOP = 2

/** 引导位置 - 下 */
const val GUIDE_GRAVITY_BOTTOM = 3

/** 引导位置 - 左上 */
const val GUIDE_GRAVITY_LEFT_TOP = 4

/** 引导位置 - 左下 */
const val GUIDE_GRAVITY_LEFT_BOTTOM = 5

/** 引导位置 - 右上 */
const val GUIDE_GRAVITY_RIGHT_TOP = 6

/** 引导位置 - 右下 */
const val GUIDE_GRAVITY_RIGHT_BOTTOM = 7

/**
 * 蒙版 View
 *
 * - 创建时间：2020-02-17
 *
 * @author 王杰
 */
class MaskingView
private constructor(context: Context)
    : RelativeLayout(context) {

    private lateinit var agency: MaskingAgency

    /** 屏幕宽度 单位：px */
    internal val screenWidth: Int = context.resources.displayMetrics.widthPixels

    /** 屏幕高度 单位：px */
    internal val screenHeight: Int = context.resources.displayMetrics.heightPixels

    init {
        setOnClickListener {
            agency.clickListener?.invoke(this)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        agency.onDraw(canvas)
    }

    /**
     * 显示蒙版
     */
    fun show() {
        agency.show(this)
    }

    /**
     * 隐藏蒙版
     */
    fun hide() {
        agency.hide(this)
    }

    /**
     * 重置状态
     */
    fun restoreState() {
        agency.restoreState()
    }

    class Builder(val activity: Activity) {

        private val agency: MaskingAgency = MaskingAgency()

        fun setBgColor(@ColorInt bgColor: Int): Builder {
            agency.bgColor = bgColor
            return this
        }

        fun setShape(shape: Int): Builder {
            agency.shape = shape
            return this
        }

        fun setRadius(radius: Int): Builder {
            agency.radius = radius
            return this
        }

        fun setOffset(offsetX: Int, offsetY: Int): Builder {
            agency.offsetX = offsetX
            agency.offsetY = offsetY
            return this
        }

        fun setGravity(gravity: Int): Builder {
            agency.gravity = gravity
            return this
        }

        fun setTargetView(target: View): Builder {
            agency.targetView = target
            return this
        }

        fun setGuideView(guide: View): Builder {
            agency.guideView = guide
            return this
        }

        fun setOnMaskingClickListener(listener: OnMaskingClickListener): Builder {
            agency.clickListener = listener
            return this
        }

        fun build(): MaskingView {
            return MaskingView(activity).apply {
                this.agency = this@Builder.agency
                this.agency.view = this
            }
        }
    }

    companion object {

        fun newBuilder(activity: Activity): Builder {
            return Builder(activity)
        }
    }
}

/**
 * 蒙版 View 代理类
 */
class MaskingAgency
    : ViewTreeObserver.OnGlobalLayoutListener {

    /** 蒙版 View */
    internal lateinit var view: MaskingView

    /** 目标 View */
    internal var targetView: View? = null

    /** 目标 View 宽度 */
    internal var targetViewWidth: Int = 0

    /** 目标 View 高度 */
    internal var targetViewHeight: Int = 0

    /** 标记 - 是否已测量 */
    internal var measured = false

    /** 蒙版背景 Bitmap */
    internal var bgBitmap: Bitmap? = null

    /** 蒙版背景颜色 */
    internal var bgColor: Int = Color.parseColor("#C0000000")

    /** 背景画笔 */
    internal val bgPaint: Paint = Paint()

    /** 目标区域画笔 */
    internal val targetPaint: Paint = Paint().apply {
        //透明效果
        val porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        xfermode = porterDuffXfermode
        isAntiAlias = true
    }

    /** 蒙版形状 默认圆形*/
    internal var shape: Int = MASKING_SHAPE_RECTANGULAR

    /** 目标 View 半径 */
    internal var radius: Int = 0

    /** 中心点坐标 */
    internal var center: IntArray? = null

    /** 右上角坐标 */
    internal var location: IntArray? = null

    /** 引导 View */
    internal var guideView: View? = null

    /** 偏移量 */
    internal var offsetX: Int = 0
    internal var offsetY: Int = 0

    /** 引导 View 重心 */
    internal var gravity: Int = GUIDE_GRAVITY_BOTTOM

    /** 点击事件 */
    internal var clickListener: OnMaskingClickListener? = null

    override fun onGlobalLayout() {
        if (measured) {
            return
        }
        if (targetView!!.height > 0 && targetView!!.width > 0) {
            measured = true
            targetViewWidth = targetView!!.width
            targetViewHeight = targetView!!.height
        }
        // 获取targetView的中心坐标
        if (center == null) {
            // 获取右上角坐标
            location = IntArray(2)
            targetView!!.getLocationInWindow(location)
            center = IntArray(2)
            // 获取中心坐标
            center!![0] = location!![0] + targetView!!.width / 2
            center!![1] = location!![1] + targetView!!.height / 2
        }
        // 获取targetView外切圆半径
        if (radius == 0) {
            radius = getTargetViewRadius()
        }

        // 显示引导 View
        showGuideView()
    }

    fun onDraw(canvas: Canvas) {
        if (null == targetView || !measured) {
            // 未设置目标 View 或未测量
            return
        }

        // 绘制背景
        drawBackground(canvas)
    }

    /**
     * 绘制蒙版背景
     */
    private fun drawBackground(canvas: Canvas) {
        // 创建背景 Bitmap
        bgBitmap = try {
            Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
        if (null == bgBitmap) {
            return
        }

        // 创建缓存画布
        val tempCanvas = Canvas(bgBitmap!!)

        // 背景画笔
        bgPaint.color = bgColor

        // 绘制屏幕背景
        tempCanvas.drawRect(0f, 0f, tempCanvas.width.toFloat(), tempCanvas.height.toFloat(), bgPaint)

        if (null == center || null == location) {
            return
        }

        if (shape == MASKING_SHAPE_CIRCLE) {
            // 圆形
            tempCanvas.drawCircle(center!![0].toFloat(), center!![1].toFloat(), radius.toFloat(), targetPaint)
        } else if (shape == MASKING_SHAPE_RECTANGULAR) {
            // 矩形
            val rect = RectF()
            rect.left = location!![0] + 5.toFloat()
            rect.top = center!![1] - targetViewHeight / 2 + 1.toFloat()
            rect.right = location!![0] + targetViewWidth - 5.toFloat()
            rect.bottom = center!![1] + targetViewHeight / 2 - 1.toFloat()
            tempCanvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), targetPaint)
        }

        // 绘制到屏幕
        canvas.drawBitmap(bgBitmap!!, 0f, 0f, bgPaint)
        bgBitmap!!.recycle()
    }

    /**
     * 获得targetView 的半径
     */
    private fun getTargetViewRadius(): Int {
        if (measured) {
            val size: IntArray = getTargetViewSize()
            val x = size[0]
            val y = size[1]
            return (sqrt(x * x + y * y.toDouble()) / 2).toInt()
        }
        return -1
    }

    /**
     * 获得targetView 的宽高
     */
    private fun getTargetViewSize(): IntArray {
        val location = intArrayOf(-1, -1)
        if (measured) {
            location[0] = targetView!!.width
            location[1] = targetView!!.height
        }
        return location
    }

    /**
     * 显示引导 View
     */
    private fun showGuideView() {
        if (null == guideView) {
            return
        }

        var layoutParams = guideView!!.layoutParams
        if (null == layoutParams || layoutParams !is RelativeLayout.LayoutParams) {
            layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        }
        val start = center!![0] - targetViewWidth / 2
        val end = center!![0] + targetViewWidth / 2
        val top = center!![1] - targetViewHeight / 2
        val bottom = center!![1] + targetViewHeight / 2
        when (gravity) {
            GUIDE_GRAVITY_LEFT -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                layoutParams.setMargins(0, top + offsetY, view.screenWidth - start + offsetX, 0)
            }
            GUIDE_GRAVITY_LEFT_TOP -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                layoutParams.setMargins(0, 0, view.screenWidth - start + offsetX, view.screenHeight - top + offsetY)
            }
            GUIDE_GRAVITY_LEFT_BOTTOM -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                layoutParams.setMargins(0, bottom + offsetY, view.screenWidth - start + offsetX, 0)
            }
            GUIDE_GRAVITY_RIGHT -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                layoutParams.setMargins(end + offsetX, top + offsetY, 0, 0)
            }
            GUIDE_GRAVITY_RIGHT_TOP -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                layoutParams.setMargins(end + offsetX, 0, 0, view.screenHeight - top + offsetY)
            }
            GUIDE_GRAVITY_RIGHT_BOTTOM -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                layoutParams.setMargins(end + offsetX, bottom + offsetY, 0, 0)
            }
            GUIDE_GRAVITY_TOP -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                layoutParams.setMargins(start + offsetX, 0, 0, view.screenHeight - top + offsetY)
            }
            else -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                layoutParams.setMargins(start + offsetX, bottom + offsetY, 0, 0)
            }
        }
        view.removeAllViews()
        view.addView(guideView, layoutParams)
    }

    /**
     * 显示蒙版
     */
    fun show(masking: MaskingView) {
        targetView?.viewTreeObserver?.addOnGlobalLayoutListener(this)

        masking.setBackgroundColor(Color.TRANSPARENT)
        masking.bringToFront() //设置在最上层

        ((masking.context as? Activity)?.window?.decorView as? FrameLayout)?.addView(masking)
    }

    /**
     * 隐藏蒙版
     */
    fun hide(masking: MaskingView) {
        targetView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        masking.removeAllViews()
        ((masking.context as? Activity)?.window?.decorView as? FrameLayout)?.removeView(masking)
        restoreState()
    }

    /**
     * 重置状态
     */
    fun restoreState() {
        offsetY = 0
        offsetX = offsetY
        radius = 0
        measured = false
        center = null
        location = null
    }
}

typealias OnMaskingClickListener = (MaskingView) -> Unit