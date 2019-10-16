package cn.wj.android.recyclerview.adapter

import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

/**
 * 有动画回调
 * - 修复添加头部后数据错位问题
 *
 * - 创建时间：2019/10/16
 *
 * @author 王杰
 */
class AdapterListUpdateCallback
/**
 * Creates an AdapterListUpdateCallback that will dispatch update events to the given adapter.
 *
 * @param mAdapter The Adapter to send updates to.
 */
(private val mAdapter: RecyclerView.Adapter<*>) : ListUpdateCallback {

    override fun onInserted(position: Int, count: Int) {
        val realPosition = if (mAdapter is BaseRvListAdapter<*, *>) {
            mAdapter.fixPosition(position)
        } else {
            position
        }
        mAdapter.notifyItemRangeInserted(realPosition, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        val realPosition = if (mAdapter is BaseRvListAdapter<*, *>) {
            mAdapter.fixPosition(position)
        } else {
            position
        }
        mAdapter.notifyItemRangeRemoved(realPosition, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        val realFromPosition = if (mAdapter is BaseRvListAdapter<*, *>) {
            mAdapter.fixPosition(fromPosition)
        } else {
            fromPosition
        }
        val realToPosition = if (mAdapter is BaseRvListAdapter<*, *>) {
            mAdapter.fixPosition(toPosition)
        } else {
            toPosition
        }
        mAdapter.notifyItemMoved(realFromPosition, realToPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        val realPosition = if (mAdapter is BaseRvListAdapter<*, *>) {
            mAdapter.fixPosition(position)
        } else {
            position
        }
        mAdapter.notifyItemRangeChanged(realPosition, count, payload)
    }
}