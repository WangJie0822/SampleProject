package com.wj.sampleproject.base.recyclerview.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * 加载更多 View 接口
 *
 * @author 王杰
 */
interface BaseRvLoadMoreView {

    companion object {
        /** View 状态 - 默认 */
        const val VIEW_STATUS_DEFAULT = 0x0101561
        /** View 状态 - 加载中 */
        const val VIEW_STATUS_LOADING = 0x0101562
        /** View 状态 - 加载失败 */
        const val VIEW_STATUS_FAILED = 0x0101563
        /** View 状态 - 加载完成 */
        const val VIEW_STATUS_END = 0x0101564
    }

    /** View 状态 */
    var viewStatus: Int
    /** 加载更多回调 */
    var listener: OnLoadMoreListener?
    /** 加载失败点击监听 */
    var failedListener: OnLoadMoreFailedClickListener?
    /** 没有更多点击监听 */
    var endListener: OnLoadMoreEndClickListener?

    /**
     * 加载布局
     *
     * @return 返回布局 id
     */
    fun layoutResID(): Int

    /**
     * 配置显示 View
     *
     * @param holder ViewHolder 对象
     */
    fun convert(holder: RecyclerView.ViewHolder)

    /**
     * 加载更多
     */
    fun onLoadMore()

    /**
     * 加载完成
     */
    fun onComplete()

    /**
     * 加载失败
     */
    fun onFailed()

    /**
     * 加载结束，没有更多
     */
    fun onEnd()
}