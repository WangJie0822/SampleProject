@file:Suppress("unused")

package cn.wj.android.databinding.adapter

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

/**
 * ImageView DataBinding 适配器
 */

/**
 * 根据资源 id 加载图片
 *
 * @param iv    [ImageView] 对象
 * @param resId 图片资源 id
 */
@BindingAdapter("android:bind_src")
fun src(iv: ImageView, @DrawableRes resId: Int?) {
    if (null != resId && 0 != resId) {
        iv.setImageResource(resId)
    }
}

/**
 * 根据 id 字符串加载图片
 *
 * @param iv [ImageView] 对象
 * @param res 资源字符串 drawable-anydpi-resource:resId 或 mipmap-resource:resId
 */
@BindingAdapter("android:bind_src")
fun setImageResource(iv: ImageView, res: String?) {
    if (null != res && res.isNotBlank()) {
        if (res.startsWith(IMG_DRAWABLE_MARK)) {
            val realRes = res.split(":")[1]
            iv.setImageResource(realRes.getIdentifier(iv.context, "drawable"))
        } else if (res.startsWith(IMG_MIPMAP_MARK)) {
            val realRes = res.split(":")[1]
            iv.setImageResource(realRes.getIdentifier(iv.context, "mipmap"))
        }
    }
}