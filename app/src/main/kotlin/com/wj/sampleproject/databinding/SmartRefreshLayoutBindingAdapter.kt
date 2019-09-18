@file:Suppress("unused")

package com.wj.sampleproject.databinding

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState

/**
 * SmartRefreshLayout DataBinding 适配器
 */

/**
 * 扩展属性 是否正在刷新
 */
var SmartRefreshLayout.refreshing: Boolean
    get() = state == RefreshState.Refreshing
    set(value) {
        if (refreshing && value) {
            return
        }
        if (!refreshing && !value) {
            return
        }
        if (value) {
            if (state == RefreshState.None) {
                autoRefresh(200, 300, 1.5f, false)
            } else {
                autoRefresh()
            }
        } else {
            finishRefresh()
        }
    }

/**
 * 设置刷新监听
 *
 * @param onRefresh 刷新监听
 */
@BindingAdapter("android:bind_smart_onRefresh", "android:bind_smart_refreshingAttrChanged", requireAll = false)
fun setSmartRefreshLayoutOnRefresh(srl: SmartRefreshLayout,
                                   onRefresh: (() -> Unit)?,
                                   listener: InverseBindingListener?) {
    srl.setOnRefreshListener {
        listener?.onChange()
        onRefresh?.invoke()
    }
}

/**
 * 设置刷新状态
 *
 * @param refreshing 刷新状态
 */
@BindingAdapter("android:bind_smart_refreshing")
fun setSmartRefreshLayoutRefresh(srl: SmartRefreshLayout, refreshing: Boolean) {
    srl.refreshing = refreshing
}

/**
 * 获取刷新状态
 */
@InverseBindingAdapter(attribute = "android:bind_smart_refreshing")
fun getSmartRefreshLayoutRefresh(srl: SmartRefreshLayout): Boolean {
    return srl.refreshing
}

/**
 * 设置是否允许刷新
 *
 * @param enable 是否允许刷新
 */
@BindingAdapter("android:bind_smart_refresh_enable")
fun setSmartRefreshLayoutRefreshEnable(srl: SmartRefreshLayout, enable: Boolean) {
    srl.setEnableRefresh(enable)
}

/**
 * 扩展属性 是否正在加载更多
 */
var SmartRefreshLayout.loadmore: Boolean
    get() = state == RefreshState.Loading
    set(value) {
        if (loadmore && value) {
            return
        }
        if (!loadmore && !value) {
            return
        }
        if (value) {
            autoLoadMore()
        } else {
            finishLoadMore()
        }
    }

/**
 * 设置加载更多监听
 *
 * @param onLoadmore 加载更多监听
 */
@BindingAdapter("android:bind_smart_onLoadMore", "android:bind_smart_loadMoreAttrChanged", requireAll = false)
fun setSmartRefreshLayoutOnLoadMore(srl: SmartRefreshLayout,
                                    onLoadmore: (() -> Unit)?,
                                    listener: InverseBindingListener?) {
    srl.setOnLoadMoreListener {
        listener?.onChange()
        onLoadmore?.invoke()
    }
}

/**
 * 设置加载更多状态
 *
 * @param loadmore 是否加载更多
 */
@BindingAdapter("android:bind_smart_loadMore")
fun setSmartRefreshLayoutLoadMore(srl: SmartRefreshLayout, loadmore: Boolean) {
    srl.loadmore = loadmore
}

/**
 * 获取加载更多状态
 */
@InverseBindingAdapter(attribute = "android:bind_smart_loadMore")
fun getSmartRefreshLayoutLoadMore(srl: SmartRefreshLayout): Boolean {
    return srl.loadmore
}

/**
 * 设置是否允许加载更多
 *
 * @param enable 是否允许加载更多
 */
@BindingAdapter("android:bind_smart_loadMore_enable")
fun setSmarRefreshLayoutLoadMoreEnable(srl: SmartRefreshLayout, enable: Boolean) {
    srl.setEnableLoadMore(enable)
}