@file:Suppress("unused")

package com.wj.sampleproject.databinding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import cn.wj.android.databinding.adapter.IMG_RESOURCE_MARK
import cn.wj.android.databinding.adapter.setImageResource
import coil.load
import java.io.File

/**
 * ImageView DataBinding 适配器
 *
 * @author 王杰
 */

/**
 * 为 [iv] 设置网络图片 [imgUrl] 显示，占位图为 [placeholder]
 * > 当 [imgUrl] 为空或者加载失败时，显示 [default]
 *
 * > [placeholder] [default] 可使用资源类型 android:bind_params="@{@drawable/app_drawable_id}"
 */
@BindingAdapter(
        "android:bind_iv_img_url",
        "android:bind_iv_img_placeholder",
        "android:bind_iv_img_default",
        requireAll = false
)
fun setImageViewUrl(
        iv: ImageView,
        imgUrl: String?,
        placeholder: Drawable?,
        default: Drawable?
) {
    iv.load(imgUrl) {
        if (null != placeholder) {
            placeholder(placeholder)
        }
        if (null != default) {
            error(default)
        }
    }
}

/**
 * 为 [iv] 设置本地图片 [path] 显示，占位图为 [placeholder]
 * > 当 [path] 为空或者加载失败时，显示 [default]
 *
 * > [placeholder] [default] 可使用资源类型 android:bind_params="@{@drawable/app_drawable_id}"
 */
@BindingAdapter(
        "android:bind_iv_img_path",
        "android:bind_iv_img_placeholder",
        "android:bind_iv_img_default",
        requireAll = false
)
fun setImageViewPath(
        iv: ImageView,
        path: String?,
        placeholder: Drawable?,
        default: Drawable?
) {

    if (path.isNullOrBlank()) {
        if (null != default) {
            iv.setImageDrawable(default)
        }
        return
    }
    iv.load(File(path)) {
        if (null != placeholder) {
            placeholder(placeholder)
        }
        if (null != default) {
            error(default)
        }
    }
}

/**
 * 为 [iv] 设置图片 [img] 显示，占位图为 [placeholder]
 * > 根据 [img] 数据类型加载不同类型数据
 *
 * > [placeholder] [default] 可使用资源类型 android:bind_params="@{@drawable/app_drawable_id}"
 *
 * @see setImageViewUrl
 * @see setImageViewPath
 * @see setImageResource
 */
@BindingAdapter(
        "android:bind_iv_img",
        "android:bind_iv_img_placeholder",
        "android:bind_iv_img_default",
        requireAll = false
)
fun setImageViewImg(
        iv: ImageView,
        img: String?,
        placeholder: Drawable?,
        default: Drawable?
) {
    if (img.isNullOrBlank()) {
        if (null != default) {
            iv.setImageDrawable(default)
        }
        return
    }
    if (img.contains("http:") || img.contains("https:")) {
        // url
        setImageViewUrl(iv, img, placeholder, default)
    } else if (img.contains(IMG_RESOURCE_MARK)) {
        // Resource
        setImageResource(iv, img)
    } else {
        // path
        setImageViewPath(iv, img, placeholder, default)
    }
}