@file:Suppress("unused")

package cn.wj.android.base.databinding

import android.graphics.Color
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.wj.android.base.tools.getIdentifier

/**
 * SwipeRefreshLayout DataBinding 适配器
 */

/**
 * 设置刷新进度条颜色
 *
 * @param srl [SwipeRefreshLayout] 对象
 * @param color 颜色值
 */
@BindingAdapter("android:bind_srl_schemeColors")
fun setSwipeRefreshLayoutSchemeColors(srl: SwipeRefreshLayout, color: Int) {
    srl.setColorSchemeColors(color)
}

/**
 * 设置刷新进度条颜色
 *
 * @param srl [SwipeRefreshLayout] 对象
 * @param colorStr 颜色 id 字符串或颜色值，"," 分隔
 */
@BindingAdapter("android:bind_srl_schemeColors")
fun setSwipeRefreshLayoutSchemeColors(srl: SwipeRefreshLayout, colorStr: String) {
    // 获取颜色集合
    val colorStrLs = colorStr.split(",")
    // 转换后的颜色集合
    val colorLs = arrayListOf<Int>()
    // 统计颜色
    colorStrLs.forEach {
        colorLs.add(
                if (colorStr.contains("#")) {
                    // 颜色值
                    Color.parseColor(it)
                } else {
                    // 颜色 id 字符串
                    it.getIdentifier(srl.context, "color")
                }
        )
    }
    srl.setColorSchemeColors(*colorLs.toIntArray())
}

/**
 * 设置刷新状态
 *
 * @param srl [SwipeRefreshLayout] 对象
 * @param refreshing 是否刷新
 */
@BindingAdapter("android:bind_srl_refreshing")
fun setSwipeRefreshLayoutRefreshing(srl: SwipeRefreshLayout, refreshing: Boolean) {
    srl.isRefreshing = refreshing
}

/**
 * 获取刷新状态
 *
 * @param srl [SwipeRefreshLayout] 对象
 *
 * @return 刷新状态
 */
@InverseBindingAdapter(attribute = "android:bind_srl_refreshing")
fun getSwipeRefreshLayoutRefreshing(srl: SwipeRefreshLayout): Boolean {
    return srl.isRefreshing
}

/**
 * 设置刷新事件
 *
 * @param srl [SwipeRefreshLayout] 对象
 * @param refresh 刷新事件
 * @param listener 刷新状态监听
 */
@BindingAdapter("android:bind_srl_onRefresh", "android:bind_srl_refreshingAttrChanged", requireAll = false)
fun setSwipeRefreshLayoutRefreshListener(
        srl: SwipeRefreshLayout,
        refresh: (() -> Unit)?,
        listener: InverseBindingListener?
) {
    srl.setOnRefreshListener {
        refresh?.invoke()
        listener?.onChange()
    }
}