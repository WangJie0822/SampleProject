@file:Suppress("unused")

package cn.wj.android.views.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min

/**
 * 侧边文本导航栏
 *
 * @author 王杰
 */
class SideBarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        /** 默认字符列表 */
        private const val DEFAULT_LETTER = "#,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z"
    }

    /** 文本、字母导航集合 */
    private var letters = arrayListOf<String>()

    /** 当前选中的字母下标 */
    private var selectPos = -1

    /** 默认背景颜色 */
    private val bgColorNormal: Int

    /** 按下背景颜色 */
    private val bgColorPressed: Int

    /** 文字大小 */
    private val textSize: Int

    /** 默认文本颜色 */
    private val textColorNormal: Int

    /** 按下文本颜色 */
    private val textColorPressed: Int

    /** 默认画笔 */
    private val paintNormal: Paint

    /** 选中画笔 */
    private val paintSelect: Paint

    /** 控件高度 */
    private var mHeight: Int = 0

    /** 控件宽度 */
    private var mWidth: Int = 0

    /** 每个文本高度 */
    private var perHeight: Int = 0

    init {
        // 获取布局属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SideBarView, defStyleAttr, 0)
        var lettersStr = typedArray.getString(R.styleable.SideBarView_sbv_letters)
        if (lettersStr.isNullOrBlank()) {
            lettersStr = DEFAULT_LETTER
        }
        letters.clear()
        letters.addAll(lettersStr.split(","))
        bgColorNormal = typedArray.getColor(R.styleable.SideBarView_sbv_background_nor, Color.TRANSPARENT)
        bgColorPressed = typedArray.getColor(R.styleable.SideBarView_sbv_background_press, Color.parseColor("#FFF3F3F3"))
        textSize = typedArray.getDimension(R.styleable.SideBarView_sbv_text_size, 18.sp2px(context)).toInt()
        textColorNormal = typedArray.getColor(R.styleable.SideBarView_sbv_text_color_nor, Color.parseColor("#FF7F7F7F"))
        textColorPressed = typedArray.getColor(R.styleable.SideBarView_sbv_text_color_press, Color.parseColor("#FF7F7F7F"))
        typedArray.recycle()

        // 初始化画笔
        paintNormal = Paint()
        paintNormal.isAntiAlias = true
        paintNormal.color = textColorNormal
        paintNormal.typeface = Typeface.DEFAULT_BOLD
        paintNormal.textSize = textSize.toFloat()
        paintSelect = Paint()
        paintSelect.isAntiAlias = true
        paintSelect.typeface = Typeface.DEFAULT_BOLD
        paintSelect.textSize = textSize.toFloat()
        paintSelect.color = textColorPressed
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 获取控件宽高
        mHeight = height
        mWidth = width
        // 计算每个字母高度
        perHeight = mHeight / letters.size
        // 绘制控件
        for (i in letters.indices) {
            canvas.drawText(
                    letters[i],
                    mWidth / 2 - paintNormal.measureText(letters[i]) / 2,
                    (perHeight * i + perHeight / 2).toFloat(),
                    paintNormal
            )
            if (selectPos == i) {
                canvas.drawText(
                        letters[i],
                        mWidth / 2 - paintNormal.measureText(letters[i]) / 2,
                        (perHeight * i + perHeight / 2).toFloat(),
                        paintSelect
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = resolveMeasure(widthMeasureSpec, true)
        val height = resolveMeasure(heightMeasureSpec, false)
        setMeasuredDimension(width, height)
    }

    /**
     * 处理控件测量
     */
    private fun resolveMeasure(measureSpec: Int, isWidth: Boolean): Int {

        var result: Int
        val padding = if (isWidth) paddingLeft + paddingRight else paddingTop + paddingBottom

        // 获取宽度测量规格中的mode
        val mode = MeasureSpec.getMode(measureSpec)
        // 获取宽度测量规格中的size
        val size = MeasureSpec.getSize(measureSpec)

        when (mode) {
            MeasureSpec.EXACTLY -> result = size
            else -> {
                val textWidth = paintNormal.measureText(letters.maxByOrNull { it.length })
                if (isWidth) {
                    result = if (suggestedMinimumWidth > textWidth) suggestedMinimumWidth else textWidth.toInt()
                    result += padding
                    result = min(result, size)
                } else {
                    result = size
                    result = max(result, size)
                }
            }
        }
        return result
    }

    override fun getSuggestedMinimumWidth() = 25.dip2px(context).toInt()

    @Suppress("RedundantOverride")
    override fun performClick() = super.performClick()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 获取触摸 Y 轴位置
        val x = event.y
        // 计算当前位置
        val position = (x / perHeight).toInt()
        if (position < 0 || position >= letters.size) {
            // 超出控件范围
            if (selectPos == -1) {
                selectPos = 0
            }
            listener?.onLetterReleased(letters[selectPos])
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                setBackgroundColor(bgColorPressed)
                selectPos = position
                listener?.onLetterSelected(letters[selectPos])
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                if (position != selectPos) {
                    //切换到其他字母
                    selectPos = position
                    listener?.onLetterChanged(letters[selectPos])
                    invalidate()
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                performClick()
                setBackgroundColor(bgColorNormal)
                listener?.onLetterReleased(letters[selectPos])
            }
            else -> {
            }
        }
        return true
    }

    private var listener: LetterSelectListener? = null

    fun setOnLetterSelectListener(lis: LetterSelectListener) {
        this.listener = lis
    }

    interface LetterSelectListener {
        fun onLetterSelected(letter: String)

        fun onLetterChanged(letter: String)

        fun onLetterReleased(letter: String)
    }
}