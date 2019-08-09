@file:Suppress("unused")

package cn.wj.android.base.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import cn.wj.android.base.constants.IMG_DRAWABLE_MARK
import cn.wj.android.base.constants.IMG_MIPMAP_MARK
import cn.wj.android.base.tools.getIdentifier

/**
 * ImageView DataBinding 适配器
 */

/**
 * 根据资源 id 加载图片
 *
 * @param iv    [ImageView] 对象
 * @param resID 图片资源 id
 */
@BindingAdapter("android:src")
fun src(iv: ImageView, resID: Int) {
    if (0 != resID) {
        iv.setImageResource(resID)
    }
}

/**
 * 根据 id 字符串加载图片
 *
 * @param iv [ImageView] 对象
 * @param res 资源字符串 drawable-anydpi-resource:resId 或 mipmap-resource:resId
 */
@BindingAdapter("android:bind_src")
fun setImageResource(iv: ImageView, res: String) {
    if (res.isNotBlank()) {
        if (res.startsWith(IMG_DRAWABLE_MARK)) {
            val realRes = res.split(":")[1]
            iv.setImageResource(realRes.getIdentifier(iv.context, "drawable"))
        } else if (res.startsWith(IMG_MIPMAP_MARK)) {
            val realRes = res.split(":")[1]
            iv.setImageResource(realRes.getIdentifier(iv.context, "mipmap"))
        }
    }
}