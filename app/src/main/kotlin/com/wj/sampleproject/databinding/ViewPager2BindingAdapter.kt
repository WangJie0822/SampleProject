@file:Suppress("unused")

package com.wj.sampleproject.databinding

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.viewpager2.widget.ViewPager2
import cn.wj.common.ext.condition

/*
 * ViewPager DataBinding 适配器
 */

/**
 * 给 [vp] 添加页码切换监听 [scrolled] & [selected] & [changed]
 * > [listener] 为属性变化监听，`DataBinding` 自动实现
 */
@BindingAdapter(
        "android:bind_vp2_change_scrolled",
        "android:bind_vp2_change_selected",
        "android:bind_vp2_change_changed",
        "android:bind_vp2_currentItemAttrChanged",
        requireAll = false
)
fun setViewPagerPageChangeListener(
        vp: ViewPager2,
        scrolled: ((Int, Float, Int) -> Unit)?,
        selected: ((Int) -> Unit)?,
        changed: ((Int) -> Unit)?,
        listener: InverseBindingListener?
) {
    vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            changed?.invoke(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            scrolled?.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            selected?.invoke(position)
            listener?.onChange()
        }
    })
}

/** 将 [vp] 预加载页数设置为 [offscreenPageLimit] */
@BindingAdapter("android:bind_vp2_offscreenPageLimit")
fun setViewPagerOffscreenPageLimit(vp: ViewPager2, offscreenPageLimit: Int?) {
    if (null == offscreenPageLimit) {
        return
    }
    val setLimit = if (offscreenPageLimit < 1) {
        ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
    } else {
        offscreenPageLimit
    }
    vp.offscreenPageLimit = setLimit
}

/** 根据是否平滑滚动 [smoothScroll] 将 [vp] 切换到指定页 [currentItem] */
@BindingAdapter("android:bind_vp2_currentItem", "android:bind_vp2_smoothScroll", requireAll = false)
fun setViewPagerCurrentItem(vp: ViewPager2, currentItem: Int?, smoothScroll: Boolean?) {
    if (null == currentItem) {
        return
    }
    vp.setCurrentItem(currentItem, smoothScroll.condition)
}

/** 获取 [vp] 当前位置 */
@InverseBindingAdapter(attribute = "android:bind_vp2_currentItem")
fun getViewPagerCurrentItem(vp: ViewPager2): Int {
    return vp.currentItem
}

/** 设置 [vp] 是否支持用户输入 [isUserInputEnabled] */
@BindingAdapter("android:bind_vp2_userInputEnabled")
fun setViewPagerScrollable(vp: ViewPager2, isUserInputEnabled: Boolean?) {
    if (null == isUserInputEnabled) {
        return
    }
    vp.isUserInputEnabled = isUserInputEnabled
}

/** 给 [vp] 设置触摸事件监听 */
@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("android:bind_vp2_onTouch")
fun setViewOnTouch(vp: ViewPager2, onTouch: ((MotionEvent) -> Boolean)?) {
    val target = vp.getChildAt(0) ?: return
    if (null == onTouch) {
        target.setOnTouchListener(null)
        return
    }
    target.setOnTouchListener { _, ev ->
        onTouch.invoke(ev)
    }
}