@file:Suppress("unused")

package cn.wj.android.databinding.adapter

import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter

/**
 * 设置滚动监听
 */
@BindingAdapter("android:bind_nested_onScrollChange")
fun setOnScrollChangeListener(scrollView: NestedScrollView, onScroll: ((Int, Int) -> Unit)?) {
    scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
        onScroll?.invoke(scrollY, oldScrollY)
    })
}

/**
 * 设置滚动监听
 */
@BindingAdapter("android:bind_nested_onScrollChange")
fun setOnScrollChangeListener(scorllView: NestedScrollView, onScroll: NestedScrollListener?) {
    scorllView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
        onScroll?.onScrollChange(scrollY, oldScrollY)
    })
}

interface NestedScrollListener {
    fun onScrollChange(scrollY: Int, oldScrollY: Int)
}