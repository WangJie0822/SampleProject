@file:Suppress("unused")

package cn.wj.android.databinding.adapter

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import cn.wj.android.common.tools.parseColor

/**
 * TextView DataBinding 适配器
 */

/**
 * 设置 TextView 文本变化监听
 *
 * @param tv     [TextView] 对象
 * @param before beforeTextChanged 回调
 * @param on onTextChanged 回调
 * @param after afterTextChanged 回调
 */
@BindingAdapter(
        "android:bind_tv_textChange_before",
        "android:bind_tv_textChange_on",
        "android:bind_tv_textChange_after",
        requireAll = false
)
fun addTextChangedListener(
        tv: TextView,
        before: ((CharSequence?, Int, Int, Int) -> Unit)?,
        on: ((CharSequence?, Int, Int, Int) -> Unit)?,
        after: ((Editable?) -> Unit)?
) {
    tv.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            before?.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            on?.invoke(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
            after?.invoke(s)
        }
    })
}

/**
 * 设置 TextView 文本颜色
 *
 * @param tv     [TextView] 对象
 * @param color 颜色值
 */
@BindingAdapter("android:bind_tv_textColor")
fun setTextColor(tv: TextView, @ColorInt color: Int?) {
    if (null == color) {
        return
    }
    tv.setTextColor(color)
}

/**
 * 设置 TextView 文本颜色
 *
 * @param tv     [TextView] 对象
 * @param colorStr 颜色值
 */
@BindingAdapter("android:bind_tv_textColor")
fun setTextColor(tv: TextView, colorStr: String?) {
    if (colorStr.isNullOrBlank()) {
        return
    }
    val color = colorStr.parseColor() ?: return
    tv.setTextColor(color)
}

/**
 * 设置 TextView 文本
 *
 * @param tv     [TextView] 对象
 * @param resId 文本资源 id
 */
@BindingAdapter("android:bind_tv_text")
fun setText(tv: TextView, @StringRes resId: Int?) {
    if (null == resId) {
        return
    }
    tv.setText(resId)
}

/**
 * 设置 TextView 文本
 *
 * @param tv     [TextView] 对象
 * @param cs CharSequence 对象
 */
@BindingAdapter("android:bind_tv_text")
fun setText(tv: TextView, cs: CharSequence?) {
    tv.text = cs
}

/**
 * 设置 TextView 最大行数
 *
 * @param tv     [TextView] 对象
 * @param maxLines 最大行数
 */
@BindingAdapter("android:bind_tv_maxLines")
fun setMaxLines(tv: TextView, maxLines: Int?) {
    if (null == maxLines) {
        return
    }
    tv.maxLines = maxLines
}

/**
 * 设置重心
 */
@BindingAdapter("android:bind_tv_gravity")
fun setGravity(tv: TextView, gravity: Int?) {
    if (null == gravity) {
        return
    }
    tv.gravity = gravity
}