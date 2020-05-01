package cn.wj.android.views.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.roundToInt

/**
 * 可设置 Drawable 尺寸的 TextView
 *
 * @author 王杰
 */
class FixedTextView : AppCompatTextView {
    
    constructor(context: Context) : this(context, null)
    
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.textViewStyle)
    
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (attrs != null) {
            init(attrs)
        }
    }
    
    fun init(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FixedTextView)
        
        // 左侧 Drawable
        val dStart = a.getDrawable(R.styleable.FixedTextView_ftv_drawableStart)
        if (dStart != null) {
            val wStart = a.getDimension(R.styleable.FixedTextView_ftv_drawableStartWidth, dStart.intrinsicWidth.toFloat())
            val hStart = a.getDimension(R.styleable.FixedTextView_ftv_drawableStartHeight, dStart.intrinsicHeight.toFloat())
            setTextViewDrawableStart(dStart, wStart, hStart)
        }
        
        // 右侧 Drawable
        val dEnd = a.getDrawable(R.styleable.FixedTextView_ftv_drawableEnd)
        if (dEnd != null) {
            val wEnd = a.getDimension(R.styleable.FixedTextView_ftv_drawableEndWidth, dEnd.intrinsicWidth.toFloat())
            val hEnd = a.getDimension(R.styleable.FixedTextView_ftv_drawableEndHeight, dEnd.intrinsicHeight.toFloat())
            setTextViewDrawableEnd(dEnd, wEnd, hEnd)
        }
        
        // 顶部 Drawable
        val dTop = a.getDrawable(R.styleable.FixedTextView_ftv_drawableTop)
        if (dTop != null) {
            val wTop = a.getDimension(R.styleable.FixedTextView_ftv_drawableTopWidth, dTop.intrinsicWidth.toFloat())
            val hTop = a.getDimension(R.styleable.FixedTextView_ftv_drawableTopHeight, dTop.intrinsicHeight.toFloat())
            setTextViewDrawableTop(dTop, wTop, hTop)
        }
        
        // 底部 Drawable
        val dBottom = a.getDrawable(R.styleable.FixedTextView_ftv_drawableBottom)
        if (dBottom != null) {
            val wBottom = a.getDimension(R.styleable.FixedTextView_ftv_drawableBottomWidth, dBottom.intrinsicWidth.toFloat())
            val hBottom = a.getDimension(R.styleable.FixedTextView_ftv_drawableBottomHeight, dBottom.intrinsicHeight.toFloat())
            setTextViewDrawableBottom(dBottom, wBottom, hBottom)
        }
        
        a.recycle()
    }
    
    /**
     * 设置 TextView 右侧图片
     *
     * @param drawable 图片
     * @param width 图片宽度
     * @param height 图片高度
     */
    fun setTextViewDrawableEnd(drawable: Drawable?, width: Float = 0f, height: Float = 0f) {
        if (null == drawable) {
            return
        }
        if (width > 0f && height > 0f) {
            drawable.setBounds(0, 0, width.roundToInt(), height.roundToInt())
        }
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
    }
    
    /**
     * 设置 TextView 左侧图片
     *
     * @param drawable 图片
     * @param width 图片宽度
     * @param height 图片高度
     */
    fun setTextViewDrawableStart(drawable: Drawable?, width: Float = 0f, height: Float = 0f) {
        if (null == drawable) {
            return
        }
        if (width > 0f && height > 0f) {
            drawable.setBounds(0, 0, width.roundToInt(), height.roundToInt())
        }
        setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
    }
    
    /**
     * 设置 TextView 顶部图片
     *
     * @param drawable 图片
     * @param width 图片宽度
     * @param height 图片高度
     */
    fun setTextViewDrawableTop(drawable: Drawable?, width: Float = 0f, height: Float = 0f) {
        if (null == drawable) {
            return
        }
        if (width > 0f && height > 0f) {
            drawable.setBounds(0, 0, width.roundToInt(), height.roundToInt())
        }
        setCompoundDrawables(compoundDrawables[0], drawable, compoundDrawables[2], compoundDrawables[3])
    }
    
    /**
     * 设置 TextView 下方图片
     *
     * @param drawable 图片
     * @param width 图片宽度
     * @param height 图片高度
     */
    fun setTextViewDrawableBottom(drawable: Drawable?, width: Float = 0f, height: Float = 0f) {
        if (null == drawable) {
            return
        }
        if (width > 0f && height > 0f) {
            drawable.setBounds(0, 0, width.roundToInt(), height.roundToInt())
        }
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], drawable)
    }
}
