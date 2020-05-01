@file:Suppress("unused")

package cn.wj.android.databinding.adapter

import android.animation.AnimatorInflater
import android.graphics.Color
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import cn.wj.android.base.constants.DEFAULT_CLICK_THROTTLE_MS
import cn.wj.android.base.ext.setOnThrottleClickListener
import cn.wj.android.common.ext.condition

/**
 * View DataBinding 适配器
 */

/**
 * 设置点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 * @param throttle 点击筛选时间，单位 ms
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_throttle", requireAll = false)
fun setViewOnClick(v: View, click: ((View) -> Unit)?, throttle: Long?) {
    val interval = throttle ?: DEFAULT_CLICK_THROTTLE_MS
    v.setOnThrottleClickListener({ click?.invoke(v) }, interval)
}

/**
 * 设置点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_throttle", requireAll = false)
fun setViewOnClick(v: View, click: (() -> Unit)?, throttle: Long?) {
    val interval = throttle ?: DEFAULT_CLICK_THROTTLE_MS
    v.setOnThrottleClickListener({ click?.invoke() }, interval)
}

/**
 * 设置点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_item", "android:bind_onClick_throttle", requireAll = false)
fun <T> setViewOnClick(v: View, click: ((View, T) -> Unit)?, item: T, throttle: Long?) {
    val interval = throttle ?: DEFAULT_CLICK_THROTTLE_MS
    v.setOnThrottleClickListener({ click?.invoke(v, item) }, interval)
}

/**
 * 设置点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_item", "android:bind_onClick_throttle", requireAll = false)
fun <T> setViewOnClick(v: View, click: ((T) -> Unit)?, item: T, throttle: Long?) {
    val interval = throttle ?: DEFAULT_CLICK_THROTTLE_MS
    v.setOnThrottleClickListener({ click?.invoke(item) }, interval)
}

/**
 * 设置长点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onLongClick")
fun setViewOnLongClick(v: View, click: ((View) -> Boolean)?) {
    v.setOnLongClickListener(click)
}

/**
 * 设置长点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onLongClick")
fun setViewOnLongClick(v: View, click: (() -> Boolean)?) {
    v.setOnLongClickListener { click?.invoke() ?: false }
}

/**
 * 设置长点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onLongClick", "android:bind_onLongClick_item")
fun <T> setViewOnLongClick(v: View, click: ((View, T) -> Boolean)?, item: T) {
    v.setOnLongClickListener { click?.invoke(it, item) ?: false }
}

/**
 * 设置长点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onLongClick", "android:bind_onLongClick_item")
fun <T> setViewOnLongClick(v: View, click: ((T) -> Boolean)?, item: T) {
    v.setOnLongClickListener { click?.invoke(item) ?: false }
}

/**
 * 设置 View 显示
 *
 * @param v [View] 对象
 * @param visibility 显示状态
 */
@BindingAdapter("android:bind_visibility")
fun setViewVisibility(v: View, visibility: Int?) {
    if (null == visibility) {
        return
    }
    v.visibility = visibility
}

/**
 * 设置 View 显示
 *
 * @param v [View] 对象
 * @param show 是否显示
 */
@BindingAdapter("android:bind_visibility", "android:bind_visibility_gone", requireAll = false)
fun setViewVisibility(v: View, show: Boolean?, gone: Boolean? = true) {
    v.visibility = if (show.condition) View.VISIBLE else if (gone != false) View.GONE else View.INVISIBLE
}

/**
 * 设置 View 选中
 *
 * @param v [View] 对象
 * @param selected 是否选中
 */
@BindingAdapter("android:bind_selected")
fun setViewSelected(v: View, selected: Boolean?) {
    v.isSelected = selected.condition
}

/**
 * 设置 View 是否允许点击
 *
 * @param v [View] 对象
 * @param enable 是否允许点击
 */
@BindingAdapter("android:bind_enable")
fun setViewEnable(v: View, enable: Boolean?) {
    v.isEnabled = enable.condition
}

/**
 * 设置 View 是否获取焦点
 *
 * @param v [View] 对象
 * @param focusable 是否获取焦点
 */
@BindingAdapter("android:bind_focusable", "android:bind_focusableAttrChanged", requireAll = false)
fun setViewFocusable(v: View, focusable: Boolean?, listener: InverseBindingListener?) {
    v.isFocusable = focusable.condition
    listener?.onChange()
}

/**
 * 获取焦点状态
 *
 * @param v [View] 对象
 *
 * @return 焦点状态
 */
@InverseBindingAdapter(attribute = "android:bind_focusable")
fun getViewFocusable(v: View): Boolean {
    return v.isFocusable
}

/**
 * 设置 View 焦点变化监听
 *
 * @param v [View] 对象
 * @param change 监听回调
 */
@BindingAdapter("android:bind_focus_change", "android:bind_focusableAttrChanged", requireAll = false)
fun setViewFocusableListener(v: View, change: ((Boolean) -> Unit)?, listener: InverseBindingListener?) {
    v.setOnFocusChangeListener { _, hasFocus ->
        change?.invoke(hasFocus)
        listener?.onChange()
    }
}

/**
 * 设置 View 焦点变化监听
 *
 * @param v [View] 对象
 * @param change 监听回调
 */
@BindingAdapter("android:bind_focus_change", "android:bind_focusableAttrChanged", requireAll = false)
fun setViewFocusableListener(v: View, change: ((View, Boolean) -> Unit)?, listener: InverseBindingListener?) {
    v.setOnFocusChangeListener { _, hasFocus ->
        change?.invoke(v, hasFocus)
        listener?.onChange()
    }
}

/**
 * 设置 View 布局监听
 *
 * @param v [View] 对象
 * @param onGlobal 回调
 * @param single 是否只执行一次
 */
@BindingAdapter("android:bind_onGlobal", "android:bind_onGlobal_single", requireAll = false)
fun setViewOnGlobal(v: View, onGlobal: ((View) -> Unit)?, single: Boolean?) {
    v.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            onGlobal?.invoke(v)
            if (single.condition) {
                v.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}

/**
 * 设置 View 布局监听
 *
 * @param v [View] 对象
 * @param onGlobal 回调
 */
@BindingAdapter("android:bind_onGlobal", "android:bind_onGlobal_single", requireAll = false)
fun setViewOnGlobal(v: View, onGlobal: (() -> Unit)?, single: Boolean?) {
    v.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            onGlobal?.invoke()
            if (single.condition) {
                v.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}

/**
 * 设置 View 触摸监听
 *
 * @param v [View] 对象
 * @param onTouch 回调
 */
@BindingAdapter("android:bind_onTouch", "android:bind_onTouch_item")
fun <T> setViewOnTouch(v: View, onTouch: ((View, MotionEvent, T) -> Boolean)?, item: T) {
    v.setOnTouchListener { _, event ->
        onTouch?.invoke(v, event, item).condition
    }
}

/**
 * 设置 View 触摸监听
 *
 * @param v [View] 对象
 * @param onTouch 回调
 */
@BindingAdapter("android:bind_onTouch")
fun setViewOnTouch(v: View, onTouch: ((View, MotionEvent) -> Boolean)?) {
    v.setOnTouchListener(onTouch)
}

/**
 * 设置 View 触摸监听
 *
 * @param v [View] 对象
 * @param onTouch 回调
 */
@BindingAdapter("android:bind_onTouch")
fun setViewOnTouch(v: View, onTouch: ((MotionEvent) -> Boolean)?) {
    v.setOnTouchListener { _, ev ->
        onTouch?.invoke(ev).condition
    }
}

/**
 * 设置 View 触摸监听
 *
 * @param v [View] 对象
 * @param onTouch 回调
 */
@BindingAdapter("android:bind_onTouch")
fun setViewOnTouch(v: View, onTouch: (() -> Boolean)?) {
    v.setOnTouchListener { _, _ ->
        onTouch?.invoke().condition
    }
}

/**
 * 设置 View 触摸监听
 *
 * @param v [View] 对象
 * @param onTouch 回调
 */
@BindingAdapter("android:bind_onTouch", "android:bind_onTouch_item")
fun <T> setViewOnTouch(v: View, onTouch: ((MotionEvent, T) -> Boolean)?, item: T) {
    v.setOnTouchListener { _, ev ->
        onTouch?.invoke(ev, item).condition
    }
}

/**
 * 设置 View 触摸监听
 *
 * @param v [View] 对象
 * @param onTouch 回调
 */
@BindingAdapter("android:bind_onTouch", "android:bind_onTouch_item")
fun <T> setViewOnTouch(v: View, onTouch: ((T) -> Boolean)?, item: T) {
    v.setOnTouchListener { _, _ ->
        onTouch?.invoke(item).condition
    }
}

/**
 * 根据字符串颜色值设置背景色
 *
 * @param v [View] 对象
 * @param color 颜色值，如：**#FFFFFF**
 */
@BindingAdapter("android:bind_background")
fun setBackground(v: View, color: String?) {
    if (!color.isNullOrEmpty()) {
        v.setBackgroundColor(Color.parseColor(color))
    }
}

/**
 * 根据资源 id 设置背景
 *
 * @param v [View] 对象
 * @param resId 背景资源 id
 */
@BindingAdapter("android:bind_background")
fun setBackgroundRes(v: View, resId: Int?) {
    if (null == resId || 0 == resId) {
        v.background = null
    } else {
        v.setBackgroundResource(resId)
    }
}

/**
 * 设置 View 海拔高度
 * - 仅 API >= LOLLIPOP 有效
 *
 * @param elevation 高度 单位:dp
 */
@BindingAdapter("android:bind_elevation")
fun setElevation(v: View, elevation: Float?) {
    if (null == elevation) {
        return
    }
    ViewCompat.setElevation(v, elevation)
}

/**
 * 设置 View 动画列表
 * - 仅 API >= LOLLIPOP 有效
 *
 * @param anmatorId 动画列表 id
 */
@BindingAdapter("android:bind_stateListAnimator")
fun setStateListAnimator(v: View, anmatorId: Int?) {
    if (null == anmatorId) {
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        v.stateListAnimator = AnimatorInflater.loadStateListAnimator(v.context, anmatorId)
    }
}

/**
 * 设置 View 透明度
 */
@BindingAdapter("android:bind_alpha")
fun setAlpha(v: View, alpha: Float?) {
    if (null == alpha) {
        return
    }
    v.alpha = alpha
}

/**
 * 设置点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_item", "android:bind_onClick_throttle", requireAll = false)
fun <T> setViewOnClick(v: View, click: ViewItemClickListener<T>?, item: T, throttle: Long?) {
    val interval = throttle ?: DEFAULT_CLICK_THROTTLE_MS
    v.setOnThrottleClickListener({ click?.onItemClick(item) }, interval)
}

/**
 * View 点击事件
 */
interface ViewItemClickListener<T> {
    fun onItemClick(item: T)
}

/**
 * 设置点击事件
 *
 * @param v [View] 对象
 * @param listener 点击回调
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_throttle", requireAll = false)
fun setViewOnClick(v: View, listener: ViewClickListener?, throttle: Long?) {
    val interval = throttle ?: DEFAULT_CLICK_THROTTLE_MS
    v.setOnThrottleClickListener({ listener?.onClick() }, interval)
}

/**
 * View 点击事件
 */
interface ViewClickListener {
    fun onClick()
}