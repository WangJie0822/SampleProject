@file:Suppress("unused")

package cn.wj.android.databinding.adapter

import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter

/**
 * 设置滚动监听
 */
@BindingAdapter("android:bind_nested_onScrollChange")
fun setOnScrollChangeListener(scorllView: NestedScrollView, onScroll: ((Int, Int) -> Unit)?) {
    scorllView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
        override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
            onScroll?.invoke(scrollY, oldScrollY)
        }
    })
}

/**
 * 设置滚动监听
 */
@BindingAdapter("android:bind_nested_onScrollChange")
fun setOnScrollChangeListener(scorllView: NestedScrollView, onScroll: NestedScrollListener?) {
    scorllView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
        override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
            onScroll?.onScorllChange(scrollY, oldScrollY)
        }
    })
}

interface NestedScrollListener {
    fun onScorllChange(scrollY: Int, oldScrollY: Int)
}