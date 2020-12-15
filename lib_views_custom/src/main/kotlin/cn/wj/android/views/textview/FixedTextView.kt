package cn.wj.android.views.textview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * 优化的 [AppCompatTextView]
 *
 * - 可设置 [setCompoundDrawables] 尺寸
 *
 * @author 王杰
 */
class FixedTextView
@JvmOverloads
constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr),
        ITextView {

    override val wrapper = TextViewWrapper(this)

    init {
        if (null != attrs) {
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
    }
}
