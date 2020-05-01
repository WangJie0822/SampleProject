package cn.wj.android.views.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.ceil

/**
 * 宽度铺满屏幕的 ImageView
 *
 * - 创建时间：2020-02-06
 *
 * @author 王杰
 */
@Suppress("unused")
class ResizableImageView
@JvmOverloads
constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatImageView(context, attrs, defStyleAttr) {
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (null != drawable) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            // 高度根据使得图片的宽度充满屏幕计算而得
            val height = ceil(width.toDouble() * drawable.intrinsicHeight.toDouble() / drawable.intrinsicWidth.toDouble()).toInt()
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}