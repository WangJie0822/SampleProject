package com.wj.sampleproject.databinding

import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.CollapsingToolbarLayout

/**
 * CollapsingToolbarLayout DataBinding 适配器
 *
 * - 创建时间：2020/11/20
 *
 * @author 王杰
 */

/**
 * 为 [ctl] 设置折叠时标题文本颜色 [color]
 */
@BindingAdapter("android:bind_ctl_collapsedTitleTextColor")
fun setCollapsedTitleTextColor(ctl: CollapsingToolbarLayout, @ColorInt color: Int?) {
    if (null == color) {
        return
    }
    ctl.setCollapsedTitleTextColor(color)
}

/**
 * 为 [ctl] 设置展开时标题文本颜色 [color]
 */
@BindingAdapter("android:bind_ctl_expandedTitleColor")
fun setExpandedTitleColor(ctl: CollapsingToolbarLayout, @ColorInt color: Int?) {
    if (null == color) {
        return
    }
    ctl.setExpandedTitleColor(color)
}