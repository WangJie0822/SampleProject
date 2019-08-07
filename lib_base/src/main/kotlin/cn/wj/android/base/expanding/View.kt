@file:Suppress("unused")

package cn.wj.android.base.expanding

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup

/**
 * 当前 View 是否被覆盖
 */
fun View.isViewCovered(): Boolean {

    /**
     * 获取 View 在父布局中的位置
     */
    fun indexOfViewInParent(view: View, parent: ViewGroup): Int {
        var index = 0
        while (index < parent.childCount) {
            if (parent.getChildAt(index) === view)
                break
            index++
        }
        return index
    }

    var currentView = this

    val currentViewRect = Rect()
    val partVisible = currentView.getGlobalVisibleRect(currentViewRect)
    val totalHeightVisible = currentViewRect.bottom - currentViewRect.top >= this.measuredHeight
    val totalWidthVisible = currentViewRect.right - currentViewRect.left >= this.measuredWidth
    val totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible
    if (!totalViewVisible) {
        //if any part of the view is clipped by any of its parents,return true
        return true
    }

    while (currentView.parent is ViewGroup) {
        val currentParent = currentView.parent as ViewGroup
        if (currentParent.visibility != View.VISIBLE)
        //if the parent of view is not visible,return true
            return true

        val start = indexOfViewInParent(currentView, currentParent)
        for (i in start + 1 until currentParent.childCount) {
            val viewRect = Rect()
            this.getGlobalVisibleRect(viewRect)
            val otherView = currentParent.getChildAt(i)
            val otherViewRect = Rect()
            otherView.getGlobalVisibleRect(otherViewRect)
            if (Rect.intersects(viewRect, otherViewRect))
            //if view intersects its older brother(covered),return true
                return true
        }
        currentView = currentParent
    }
    return false
}

