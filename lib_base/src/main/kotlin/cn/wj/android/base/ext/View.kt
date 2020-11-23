@file:Suppress("unused")
@file:JvmName("ViewExt")

package cn.wj.android.base.ext

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import cn.wj.android.base.R
import cn.wj.android.base.constants.DEFAULT_CLICK_THROTTLE_MS
import cn.wj.android.base.tools.getStatusBarHeight

/** 判断并返回当前 [View] 是否被覆盖 */
fun View.isViewCovered(): Boolean {

    /** 获取 View 在父布局中的位置 */
    fun indexOfViewInParent(view: View, parent: ViewGroup): Int {
        var index = 0
        while (index < parent.childCount) {
            if (parent.getChildAt(index) === view)
                break
            index++
        }
        return index
    }

    var currentView = this

    val currentViewRect = Rect()
    val partVisible = currentView.getGlobalVisibleRect(currentViewRect)
    val totalHeightVisible = currentViewRect.bottom - currentViewRect.top >= this.measuredHeight
    val totalWidthVisible = currentViewRect.right - currentViewRect.left >= this.measuredWidth
    val totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible
    if (!totalViewVisible) {
        // 如果视图的任何部分被其父视图的任何部分剪切，返回true
        return true
    }

    while (currentView.parent is ViewGroup) {
        val currentParent = currentView.parent as ViewGroup
        if (currentParent.visibility != View.VISIBLE) {
            // 如果视图的父视图不可见，则返回true
            return true
        }

        val start = indexOfViewInParent(currentView, currentParent)
        for (i in start + 1 until currentParent.childCount) {
            val viewRect = Rect()
            this.getGlobalVisibleRect(viewRect)
            val otherView = currentParent.getChildAt(i)
            val otherViewRect = Rect()
            otherView.getGlobalVisibleRect(otherViewRect)
            if (Rect.intersects(viewRect, otherViewRect)) {
                // 如果视图与其哥哥相交(已覆盖)，返回true
                return true
            }
        }
        currentView = currentParent
    }
    return false
}

/** 隐藏软键盘 */
fun View?.hideSoftKeyboard() {
    if (this == null) {
        return
    }
    this.clearFocus()
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(this.windowToken, 0)
}

/**
 * 根据间隔时间[interval]处理点击事件[onClick]
 * > [interval] 默认值 [DEFAULT_CLICK_THROTTLE_MS]
 *
 * > 这里的 View 只用于快速点击判断，只有两次点击间隔超过[interval]，[onClick]事件才会响应
 */
@JvmOverloads
inline fun View.disposeThrottleClick(onClick: () -> Unit, interval: Long = DEFAULT_CLICK_THROTTLE_MS) {
    if (interval > 0) {
        val lastTime = (this.getTag(R.id.base_view_click_tag) as? Long) ?: 0L
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > interval) {
            onClick.invoke()
            this.setTag(R.id.base_view_click_tag, currentTime)
        }
    } else {
        onClick.invoke()
    }
}

/**
 * 设置点击事件
 * > 在间隔时间内重复点击事件将被过滤
 *
 * > [interval] 间隔事件，默认[DEFAULT_CLICK_THROTTLE_MS]
 */
@JvmOverloads
inline fun View.setOnThrottleClickListener(crossinline onClick: () -> Unit, interval: Long = DEFAULT_CLICK_THROTTLE_MS) {
    this.setOnClickListener {
        this.disposeThrottleClick({
            onClick.invoke()
        }, interval)
    }
}

/**
 * 通过给 [View] 增加 **状态栏高度** 的高度并增加相同高度的 [View.getPaddingTop]，
 * 来适应延伸到状态栏底部的布局
 * > [getStatusBarHeight] 获取状态栏高度
 */
fun View.fitsStatusBar() {
    post {
        layoutParams = layoutParams.apply {
            height = measuredHeight + getStatusBarHeight()
        }
        setPadding(paddingLeft, paddingTop + getStatusBarHeight(), paddingRight, paddingBottom)
    }
}