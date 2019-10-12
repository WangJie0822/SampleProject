package cn.wj.android.recyclerview.adapter

import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

/**
 * 无动画回调
 */
class AdapterListUpdateNoAnimCallback
/**
 * Creates an AdapterListUpdateCallback that will dispatch update events to the given adapter.
 *
 * @param mAdapter The Adapter to send updates to.
 */
(private val mAdapter: RecyclerView.Adapter<*>) : ListUpdateCallback {

    /** {@inheritDoc}  */
    override fun onInserted(position: Int, count: Int) {
        mAdapter.notifyDataSetChanged()
    }

    /** {@inheritDoc}  */
    override fun onRemoved(position: Int, count: Int) {
        mAdapter.notifyDataSetChanged()
    }

    /** {@inheritDoc}  */
    override fun onMoved(fromPosition: Int, toPosition: Int) {
        mAdapter.notifyDataSetChanged()
    }

    /** {@inheritDoc}  */
    override fun onChanged(position: Int, count: Int, payload: Any?) {
        mAdapter.notifyDataSetChanged()
    }
}
