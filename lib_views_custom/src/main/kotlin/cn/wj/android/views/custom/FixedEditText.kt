package cn.wj.android.views.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import kotlin.math.roundToInt

/**
 * 可设置 Drawable 尺寸的 EditText
 *
 * @author 王杰
 */
class FixedEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.FixedEditText)

            val dStart = a.getDrawable(R.styleable.FixedEditText_fet_drawableStart)
            if (dStart != null) {
                val wStart = a.getDimension(R.styleable.FixedEditText_fet_drawableStartWidth, dStart.intrinsicWidth.toFloat())
                val hStart = a.getDimension(R.styleable.FixedEditText_fet_drawableStartHeight, dStart.intrinsicHeight.toFloat())
                setTextViewDrawableStart(dStart, wStart, hStart)
            }

            val dEnd = a.getDrawable(R.styleable.FixedEditText_fet_drawableEnd)
            if (dEnd != null) {
                val wEnd = a.getDimension(R.styleable.FixedEditText_fet_drawableEndWidth, dEnd.intrinsicWidth.toFloat())
                val hEnd = a.getDimension(R.styleable.FixedEditText_fet_drawableEndHeight, dEnd.intrinsicHeight.toFloat())
                setTextViewDrawableEnd(dEnd, wEnd, hEnd)
            }

            val dTop = a.getDrawable(R.styleable.FixedEditText_fet_drawableTop)
            if (dTop != null) {
                val wTop = a.getDimension(R.styleable.FixedEditText_fet_drawableTopWidth, dTop.intrinsicWidth.toFloat())
                val hTop = a.getDimension(R.styleable.FixedEditText_fet_drawableTopHeight, dTop.intrinsicHeight.toFloat())
                setTextViewDrawableTop(dTop, wTop, hTop)
            }

            val dBottom = a.getDrawable(R.styleable.FixedEditText_fet_drawableBottom)
            if (dBottom != null) {
                val wBottom = a.getDimension(R.styleable.FixedEditText_fet_drawableBottomWidth, dBottom.intrinsicWidth.toFloat())
                val hBottom = a.getDimension(R.styleable.FixedEditText_fet_drawableBottomHeight, dBottom.intrinsicHeight.toFloat())
                setTextViewDrawableBottom(dBottom, wBottom, hBottom)
            }

            if (a.getBoolean(R.styleable.FixedEditText_fet_phoneFormat, false)) {
                // 格式化手机号
                addTextChangedListener(object : TextWatcher {

                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        try {
                            if (s == null || s.isEmpty())
                                return
                            val sb = StringBuilder()
                            for (i in s.indices) {
                                if (i != 3 && i != 8 && s[i] == ' ') {
                                    continue
                                } else {
                                    sb.append(s[i])
                                    if ((sb.length == 4 || sb.length == 9) && sb[sb.length - 1] != ' ') {
                                        sb.insert(sb.length - 1, ' ')
                                    }
                                }
                            }
                            if (sb.toString() != s.toString()) {
                                var index = start + 1
                                if (sb[start] == ' ') {
                                    if (before == 0) {
                                        index++
                                    } else {
                                        index--
                                    }
                                } else {
                                    if (before == 1) {
                                        index--
                                    }
                                }
                                setText(sb.toString())
                                setSelection(index)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun afterTextChanged(s: Editable) {
                    }
                })
            }

            a.recycle()
        }
        // 自动获取焦点
        isFocusableInTouchMode = true
        isFocusable = true
    }

    /**
     * 设置 TextView 右侧图片
     *
     * @param drawable 图片
     * @param width 图片宽度
     * @param height 图片高度
     */
    private fun setTextViewDrawableEnd(drawable: Drawable, width: Float, height: Float) {
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
    private fun setTextViewDrawableStart(drawable: Drawable, width: Float, height: Float) {
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
    private fun setTextViewDrawableTop(drawable: Drawable, width: Float, height: Float) {
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
    private fun setTextViewDrawableBottom(drawable: Drawable, width: Float, height: Float) {
        if (width > 0f && height > 0f) {
            drawable.setBounds(0, 0, width.roundToInt(), height.roundToInt())
        }
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], drawable)
    }
}
