@file:Suppress("unused")

package com.wj.sampleproject.databinding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import cn.wj.android.common.ext.orFalse
import cn.wj.android.databinding.adapter.IMG_RESOURCE_MARK
import cn.wj.android.databinding.adapter.setImageResource
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

/**
 * ImageView DataBinding 适配器
 *
 * @author 王杰
 */

/**
 * 加载图片
 *
 * @param iv     [ImageView] 对象
 * @param imgUrl 图片 Url
 * @param placeholder 占位图片
 * @param default 默认图片
 */
@BindingAdapter(
        "android:bind_iv_img_url",
        "android:bind_iv_img_placeholder",
        "android:bind_iv_img_default",
        "android:bind_iv_img_circle",
        requireAll = false
)
fun setImageViewUrl(
        iv: ImageView,
        imgUrl: String?,
        placeholder: Drawable?,
        default: Drawable?,
        circle: Boolean?
) {
    if (imgUrl.isNullOrBlank()) {
        if (null != default) {
            iv.setImageDrawable(default)
        }
        return
    }
    val options = RequestOptions().apply {
        if (null != placeholder) {
            placeholder(placeholder)
        }
        if (null != default) {
            error(default)
        }
        if (circle.orFalse()) {
            circleCrop()
        }
    }
    Glide.with(iv.context)
            .load(imgUrl)
            .apply(options)
            .into(iv)
}

/**
 * 加载图片
 *
 * @param iv     [ImageView] 对象
 * @param path 图片地址
 * @param placeholder 占位图片
 * @param default 默认图片
 */
@BindingAdapter(
        "android:bind_iv_img_path",
        "android:bind_iv_img_placeholder",
        "android:bind_iv_img_default",
        "android:bind_iv_img_circle",
        requireAll = false
)
fun setImageViewPath(
        iv: ImageView,
        path: String?,
        placeholder: Drawable?,
        default: Drawable?,
        circle: Boolean?
) {
    if (path.isNullOrBlank()) {
        if (null != default) {
            iv.setImageDrawable(default)
        }
        return
    }
    val options = RequestOptions().apply {
        if (null != placeholder) {
            placeholder(placeholder)
        }
        if (null != default) {
            error(default)
        }
        if (circle.orFalse()) {
            circleCrop()
        }
    }
    if (path.isNullOrBlank()) {
        Glide.with(iv.context)
                .load(default)
    } else {
        Glide.with(iv.context)
                .load(File(path))
    }
            .apply(options)
            .into(iv)
}

/**
 * 加载图片
 *
 * @param iv     [ImageView] 对象
 * @param img 图片路径
 * @param placeholder 占位图片
 * @param default 默认图片
 */
@BindingAdapter(
        "android:bind_iv_img",
        "android:bind_iv_img_placeholder",
        "android:bind_iv_img_default",
        "android:bind_iv_img_circle",
        requireAll = false
)
fun setImageViewImg(iv: ImageView, img: String?, placeholder: Drawable?, default: Drawable?, circle: Boolean?) {
    if (img.isNullOrBlank()) {
        if (null != default) {
            iv.setImageDrawable(default)
        }
        return
    }
    if (img.contains("http:") || img.contains("https:")) {
        // url
        setImageViewUrl(iv, img, placeholder, default, circle)
    } else if (img.contains(IMG_RESOURCE_MARK)) {
        // Resource
        setImageResource(iv, img)
    } else {
        // path
        setImageViewPath(iv, img, placeholder, default, circle)
    }
}