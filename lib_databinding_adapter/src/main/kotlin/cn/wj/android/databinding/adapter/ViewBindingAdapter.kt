@file:Suppress("unused")

package cn.wj.android.databinding.adapter

import android.animation.AnimatorInflater
import android.graphics.Color
import android.os.Build
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import cn.wj.android.common.ext.condition
import cn.wj.android.common.ext.orTrue

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
fun setViewOnClick(v: View, click: ((View) -> Unit)?, throttle: Int?) {
    val interval = throttle ?: 1000
    v.setOnClickListener {
        val lastTime = (v.getTag(R.id.databinding_view_click_tag) as? Long) ?: 0L
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > interval) {
            click?.invoke(v)
            v.setTag(R.id.databinding_view_click_tag, currentTime)
        }
    }
}

/**
 * 设置点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_throttle", requireAll = false)
fun setViewOnClick(v: View, click: (() -> Unit)?, throttle: Int?) {
    val interval = throttle ?: 1000
    v.setOnClickListener {
        val lastTime = (v.getTag(R.id.databinding_view_click_tag) as? Long) ?: 0L
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > interval) {
            click?.invoke()
            v.setTag(R.id.databinding_view_click_tag, currentTime)
        }
    }
}

/**
 * 设置点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_item", "android:bind_onClick_throttle", requireAll = false)
fun <T> setViewOnClick(v: View, click: ((View, T) -> Unit)?, item: T, throttle: Int?) {
    val interval = throttle ?: 1000
    v.setOnClickListener {
        val lastTime = (v.getTag(R.id.databinding_view_click_tag) as? Long) ?: 0L
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > interval) {
            click?.invoke(v, item)
            v.setTag(R.id.databinding_view_click_tag, currentTime)
        }
    }
}

/**
 * 设置点击事件
 *
 * @param v [View] 对象
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_item", "android:bind_onClick_throttle", requireAll = false)
fun <T> setViewOnClick(v: View, click: ViewItemClickListener<T>?, item: T, throttle: Int?) {
    val interval = throttle ?: 1000
    v.setOnClickListener {
        val lastTime = (v.getTag(R.id.databinding_view_click_tag) as? Long) ?: 0L
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > interval) {
            click?.onItemClick(item)
            v.setTag(R.id.databinding_view_click_tag, currentTime)
        }
    }
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
 * @param click 点击回调
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_item", "android:bind_onClick_throttle", requireAll = false)
fun <T> setViewOnClick(v: View, click: ((T) -> Unit)?, item: T, throttle: Int?) {
    val interval = throttle ?: 1000
    v.setOnClickListener {
        val lastTime = (v.getTag(R.id.databinding_view_click_tag) as? Long) ?: 0L
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > interval) {
            click?.invoke(item)
            v.setTag(R.id.databinding_view_click_tag, currentTime)
        }
    }
}

/**
 * 设置点击事件
 *
 * @param v [View] 对象
 * @param listener 点击回调
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_throttle", requireAll = false)
fun setViewOnClick(v: View, listener: View.OnClickListener?, throttle: Int?) {
    val interval = throttle ?: 1000
    v.setOnClickListener {
        val lastTime = (v.getTag(R.id.databinding_view_click_tag) as? Long) ?: 0L
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > interval) {
            listener?.onClick(v)
            v.setTag(R.id.databinding_view_click_tag, currentTime)
        }
    }
}

/**
 * 设置点击事件
 *
 * @param v [View] 对象
 * @param listener 点击回调
 */
@BindingAdapter("android:bind_onClick", "android:bind_onClick_throttle", requireAll = false)
fun setViewOnClick(v: View, listener: ViewClickListener?, throttle: Int?) {
    val interval = throttle ?: 1000
    v.setOnClickListener {
        val lastTime = (v.getTag(R.id.databinding_view_click_tag) as? Long) ?: 0L
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > interval) {
            listener?.onClick()
            v.setTag(R.id.databinding_view_click_tag, currentTime)
        }
    }
}

/**
 * View 点击事件
 */
interface ViewClickListener {
    fun onClick()
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
fun setViewVisibility(v: View, show: Boolean?, gone: Boolean?) {
    v.visibility = if (show.orTrue()) View.VISIBLE else if (gone.orTrue()) View.GONE else View.INVISIBLE
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
 */
@BindingAdapter("android:bind_onGlobal")
fun setViewOnGlobal(v: View, onGlobal: ((View) -> Unit)?) {
    v.viewTreeObserver.addOnGlobalLayoutListener {
        onGlobal?.invoke(v)
    }
}

/**
 * 设置 View 布局监听
 *
 * @param v [View] 对象
 * @param onGlobal 回调
 */
@BindingAdapter("android:bind_onGlobal")
fun setViewOnGlobal(v: View, onGlobal: (() -> Unit)?) {
    v.viewTreeObserver.addOnGlobalLayoutListener {
        onGlobal?.invoke()
    }
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
        onTouch?.invoke(v, event, item) ?: false
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
        onTouch?.invoke(ev) ?: false
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
        onTouch?.invoke() ?: false
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
        onTouch?.invoke(ev, item) ?: false
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
        onTouch?.invoke(item) ?: false
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
    if (null == resId) {
        return
    }
    if (0 == resId) {
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
 * @param animatorId 动画列表 id
 */
@BindingAdapter("android:bind_stateListAnimator")
fun setStateListAnimator(v: View, animatorId: Int?) {
    if (null == animatorId) {
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        v.stateListAnimator = AnimatorInflater.loadStateListAnimator(v.context, animatorId)
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