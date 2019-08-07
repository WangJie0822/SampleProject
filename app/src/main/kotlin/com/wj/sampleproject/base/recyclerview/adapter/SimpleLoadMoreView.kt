package com.wj.sampleproject.base.recyclerview.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wj.sampleproject.R

/**
 * 加载更多 View 默认实现
 *
 * @author 王杰
 */
class SimpleLoadMoreView : BaseRvLoadMoreView {

    private var default: View? = null
    private var loading: View? = null
    private var failed: View? = null
    private var end: View? = null

    override var listener: OnLoadMoreListener? = null
    override var failedListener: OnLoadMoreFailedClickListener? = null
    override var endListener: OnLoadMoreEndClickListener? = null

    override var viewStatus: Int = BaseRvLoadMoreView.VIEW_STATUS_DEFAULT
        set(value) {
            field = value
            when (field) {
                BaseRvLoadMoreView.VIEW_STATUS_DEFAULT -> onComplete()
                BaseRvLoadMoreView.VIEW_STATUS_LOADING -> {
                    onLoadMore()
                    listener?.onLoadMore()
                }
                BaseRvLoadMoreView.VIEW_STATUS_FAILED -> onFailed()
                BaseRvLoadMoreView.VIEW_STATUS_END -> onEnd()
            }
        }

    override fun layoutResID() = R.layout.app_layout_recycler_base_load_more

    override fun convert(holder: RecyclerView.ViewHolder) {
        default = holder.itemView.findViewById(R.id.fl_default)
        loading = holder.itemView.findViewById(R.id.ll_loading)
        failed = holder.itemView.findViewById(R.id.fl_failed)
        end = holder.itemView.findViewById(R.id.fl_end)

        when (viewStatus) {
            BaseRvLoadMoreView.VIEW_STATUS_DEFAULT -> onComplete()
            BaseRvLoadMoreView.VIEW_STATUS_LOADING -> onLoadMore()
            BaseRvLoadMoreView.VIEW_STATUS_FAILED -> onFailed()
            BaseRvLoadMoreView.VIEW_STATUS_END -> onEnd()
        }
    }

    override fun onLoadMore() {
        default?.visibility = View.GONE
        loading?.visibility = View.VISIBLE
        failed?.visibility = View.GONE
        end?.visibility = View.GONE
    }

    override fun onComplete() {
        default?.visibility = View.VISIBLE
        loading?.visibility = View.GONE
        failed?.visibility = View.GONE
        end?.visibility = View.GONE
    }

    override fun onFailed() {
        default?.visibility = View.GONE
        loading?.visibility = View.GONE
        failed?.visibility = View.VISIBLE
        end?.visibility = View.GONE
    }

    override fun onEnd() {
        default?.visibility = View.GONE
        loading?.visibility = View.GONE
        failed?.visibility = View.GONE
        end?.visibility = View.VISIBLE
    }
}