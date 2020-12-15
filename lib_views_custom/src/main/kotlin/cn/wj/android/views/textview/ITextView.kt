package cn.wj.android.views.textview

import android.graphics.drawable.Drawable

/**
 *
 * - 创建时间：2020/12/15
 *
 * @author 王杰
 */
interface ITextView {

    val wrapper: TextViewWrapper

    /**
     * 设置 TextView 左侧图片
     *
     * @param drawable 图片
     * @param width 图片宽度
     * @param height 图片高度
     */
    fun setTextViewDrawableStart(drawable: Drawable?, width: Float = 0f, height: Float = 0f) {
        wrapper.setTextViewDrawableStart(drawable, width, height)
    }

    /**
     * 设置 TextView 右侧图片
     *
     * @param drawable 图片
     * @param width 图片宽度
     * @param height 图片高度
     */
    fun setTextViewDrawableEnd(drawable: Drawable?, width: Float = 0f, height: Float = 0f) {
        wrapper.setTextViewDrawableEnd(drawable, width, height)
    }

    /**
     * 设置 TextView 顶部图片
     *
     * @param drawable 图片
     * @param width 图片宽度
     * @param height 图片高度
     */
    fun setTextViewDrawableTop(drawable: Drawable?, width: Float = 0f, height: Float = 0f) {
        wrapper.setTextViewDrawableTop(drawable, width, height)
    }

    /**
     * 设置 TextView 下方图片
     *
     * @param drawable 图片
     * @param width 图片宽度
     * @param height 图片高度
     */
    fun setTextViewDrawableBottom(drawable: Drawable?, width: Float = 0f, height: Float = 0f) {
        wrapper.setTextViewDrawableBottom(drawable, width, height)
    }
}