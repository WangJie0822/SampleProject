package cn.wj.android.recyclerview.layoutmanager

import android.graphics.Rect
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

/**
 * 流式布局
 *
 * - 创建时间：2019/10/17
 *
 * @author 王杰
 */
class FlowLayoutManager : RecyclerView.LayoutManager() {

    /**
     * 纵向偏移量
     */
    private var mVerticalOffset = 0

    private val mItemRects = SparseArray<Rect>()

    /**
     * @return 横向的可布局的空间
     */
    private val horizontalSpace: Int
        get() = width - paddingLeft - paddingRight

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler!!)
            return
        }

        // 如果正在进行动画，则不进行布局
        if (childCount == 0 && state!!.isPreLayout) {
            return
        }

        detachAndScrapAttachedViews(recycler!!)

        // 进行布局
        layout(recycler, 0)
    }

    /**
     * @return 可以纵向滑动
     */
    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        // 如果滑动距离为0, 或是没有任何item view, 则不移动
        if (dy == 0 || childCount == 0) {
            return 0
        }

        // 实际滑动的距离，到达边界时需要进行修正
        var realOffset = dy
        if (mVerticalOffset + realOffset < 0) {
            realOffset = -mVerticalOffset
        } else if (realOffset > 0) {
            // 手指上滑，判断是否到达下边界
            val lastChildView = getChildAt(childCount - 1)
            if (getPosition(lastChildView!!) == itemCount - 1) {
                var maxBottom = getDecoratedBottom(lastChildView)

                val lastChildTop = getDecoratedTop(lastChildView)
                for (i in childCount - 2 downTo 0) {
                    val child = getChildAt(i)
                    if (getDecoratedTop(child!!) == lastChildTop) {
                        maxBottom = maxBottom.coerceAtLeast(getDecoratedBottom(getChildAt(i)!!))
                    } else {
                        break
                    }
                }

                val gap = height - paddingBottom - maxBottom
                realOffset = when {
                    gap > 0 -> -gap
                    gap == 0 -> 0
                    else -> realOffset.coerceAtMost(-gap)
                }
            }
        }

        realOffset = layout(recycler, realOffset)

        mVerticalOffset += realOffset

        offsetChildrenVertical(-realOffset)

        return realOffset
    }

    /**
     * 布局操作
     *
     * @param recycler
     * @param dy       用于判断回收、显示item, 对布局/定位本身没有影响
     * @return
     */
    private fun layout(recycler: RecyclerView.Recycler?, dy: Int): Int {

        var firstVisiblePos = 0

        // 纵向计算偏移量，考虑padding
        var topOffset = paddingTop
        // 横向计算偏移量，考虑padding
        var leftOffset = paddingLeft
        // 行高，以最高的item作为参考
        var maxLineHeight = 0

        var childCount = childCount

        // 当是滑动进入时（在onLayoutChildren方法里面，移除了所有的child view, 所以只有可能从scrollVerticalBy方法里面进入这个方法）
        if (childCount > 0) {
            // 计算滑动后，需要被回收的child view

            if (dy > 0) {
                // 手指上滑，可能需要回收顶部的view
                var i = 0
                while (i < childCount) {
                    val child = getChildAt(i)
                    if (getDecoratedBottom(child!!) - dy < topOffset) {
                        // 超出顶部的item
                        removeAndRecycleView(child, recycler!!)
                        i--
                        childCount--
                    } else {
                        firstVisiblePos = i
                        break
                    }
                    i++
                }
            } else if (dy < 0) {
                // 手指下滑，可能需要回收底部的view
                for (i in childCount - 1 downTo 0) {
                    val child = getChildAt(i)
                    if (getDecoratedTop(child!!) - dy > height - paddingBottom) {
                        // 超出底部的item
                        removeAndRecycleView(child, recycler!!)
                    } else {
                        break
                    }
                }
            }
        }

        // 进行布局
        if (dy >= 0) {
            // 手指上滑，按顺序布局item

            var minPosition = firstVisiblePos
            if (getChildCount() > 0) {
                val lastVisibleChild = getChildAt(getChildCount() - 1)
                // 修正当前偏移量
                topOffset = getDecoratedTop(lastVisibleChild!!)
                leftOffset = getDecoratedRight(lastVisibleChild)
                // 修正第一个应该进行布局的item view
                minPosition = getPosition(lastVisibleChild) + 1

                // 使用排在最后一行的所有的child view进行高度修正
                maxLineHeight = maxLineHeight.coerceAtLeast(getDecoratedMeasurementVertical(lastVisibleChild))
                for (i in getChildCount() - 2 downTo 0) {
                    val child = getChildAt(i)
                    if (getDecoratedTop(child!!) == topOffset) {
                        maxLineHeight = maxLineHeight.coerceAtLeast(getDecoratedMeasurementVertical(child))
                    } else {
                        break
                    }
                }
            }

            // 布局新的 item view
            for (i in minPosition until itemCount) {

                // 获取item view, 添加、测量、获取尺寸
                val itemView = recycler!!.getViewForPosition(i)
                addView(itemView)
                measureChildWithMargins(itemView, 0, 0)

                val sizeHorizontal = getDecoratedMeasurementHorizontal(itemView)
                val sizeVertical = getDecoratedMeasurementVertical(itemView)
                // 进行布局
                if (leftOffset + sizeHorizontal <= horizontalSpace) {
                    // 如果这行能够布局，则往后排
                    // layout
                    layoutDecoratedWithMargins(itemView, leftOffset, topOffset, leftOffset + sizeHorizontal, topOffset + sizeVertical)
                    val rect = Rect(leftOffset, topOffset + mVerticalOffset, leftOffset + sizeHorizontal, topOffset + sizeVertical + mVerticalOffset)
                    // 保存布局信息
                    mItemRects.put(i, rect)

                    // 修正横向计算偏移量
                    leftOffset += sizeHorizontal
                    maxLineHeight = maxLineHeight.coerceAtLeast(sizeVertical)
                } else {
                    // 如果当前行不够，则往下一行挪
                    // 修正计算偏移量、行高
                    topOffset += maxLineHeight
                    maxLineHeight = 0
                    leftOffset = paddingLeft

                    // layout
                    if (topOffset - dy > height - paddingBottom) {
                        // 如果超出下边界
                        // 移除并回收该item view
                        removeAndRecycleView(itemView, recycler)
                        break
                    } else {
                        // 如果没有超出下边界，则继续布局
                        layoutDecoratedWithMargins(itemView, leftOffset, topOffset, leftOffset + sizeHorizontal, topOffset + sizeVertical)
                        val rect = Rect(leftOffset, topOffset + mVerticalOffset, leftOffset + sizeHorizontal, topOffset + sizeVertical + mVerticalOffset)
                        // 保存布局信息
                        mItemRects.put(i, rect)
                        // 修正计算偏移量、行高
                        leftOffset += sizeHorizontal
                        maxLineHeight = maxLineHeight.coerceAtLeast(sizeVertical)
                    }
                }
            }
        } else {
            // 手指下滑，逆序布局新的child
            var maxPos = itemCount - 1
            if (getChildCount() > 0) {
                maxPos = getPosition(getChildAt(0)!!) - 1
            }

            for (i in maxPos downTo 0) {
                val rect = mItemRects.get(i)
                // 判断底部是否在上边界下面
                if (rect.bottom - mVerticalOffset - dy >= paddingTop) {
                    // 获取item view, 添加、设置尺寸、布局
                    val itemView = recycler!!.getViewForPosition(i)
                    addView(itemView, 0)
                    measureChildWithMargins(itemView, 0, 0)
                    layoutDecoratedWithMargins(itemView, rect.left, rect.top - mVerticalOffset, rect.right, rect.bottom - mVerticalOffset)
                }
            }
        }

        return dy
    }

    /* 对数据改变时的一些修正 */

    override fun onItemsChanged(recyclerView: RecyclerView) {
        mVerticalOffset = 0
        mItemRects.clear()
    }

    override fun onItemsAdded(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        mVerticalOffset = 0
        mItemRects.clear()
    }

    override fun onItemsMoved(recyclerView: RecyclerView, from: Int, to: Int, itemCount: Int) {
        mVerticalOffset = 0
        mItemRects.clear()
    }

    override fun onItemsRemoved(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        mVerticalOffset = 0
        mItemRects.clear()
    }

    override fun onItemsUpdated(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        mVerticalOffset = 0
        mItemRects.clear()
    }

    override fun onItemsUpdated(recyclerView: RecyclerView, positionStart: Int, itemCount: Int, payload: Any?) {
        mVerticalOffset = 0
        mItemRects.clear()
    }

    override fun onAdapterChanged(oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?) {
        mVerticalOffset = 0
        mItemRects.clear()
    }

    /**
     * 获取 child view 横向上需要占用的空间，margin计算在内
     *
     * @param view item view
     * @return child view 横向占用的空间
     */
    private fun getDecoratedMeasurementHorizontal(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return (getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin)
    }

    /**
     * 获取 child view 纵向上需要占用的空间，margin计算在内
     *
     * @param view item view
     * @return child view 纵向占用的空间
     */
    private fun getDecoratedMeasurementVertical(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin
    }
}
