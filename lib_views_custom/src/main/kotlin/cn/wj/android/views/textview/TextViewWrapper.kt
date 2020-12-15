package cn.wj.android.views.textview

import android.graphics.drawable.Drawable
import android.widget.TextView
import kotlin.math.roundToInt

/**
 *
 * - 创建时间：2020/12/15
 *
 * @author 王杰
 */
class TextViewWrapper(private val textView: TextView) {

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
        textView.run {
            setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
        }
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
        textView.run {
            setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
        }
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
        textView.run {
            setCompoundDrawables(compoundDrawables[0], drawable, compoundDrawables[2], compoundDrawables[3])
        }
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
        textView.run {
            setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], drawable)
        }
    }
}