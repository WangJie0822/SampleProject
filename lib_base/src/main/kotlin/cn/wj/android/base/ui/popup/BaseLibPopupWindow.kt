package cn.wj.android.base.ui.popup

import android.graphics.Rect
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import cn.wj.android.base.tools.isNavBarVisible
import cn.wj.android.base.tools.isSupportNavBar
import cn.wj.android.base.tools.navBarHeight

/**
 * PopupWindow 基类
 * - 添加了 DataBinding 支持
 * - 封装了一些兼容性处理
 *
 * @param mContext [AppCompatActivity] 对象
 *
 * @author 王杰
 */
abstract class BaseLibPopupWindow<DB : ViewDataBinding>(open val mContext: AppCompatActivity)
    : PopupWindow() {

    protected lateinit var mBinding: DB

    override fun showAsDropDown(anchor: View?) {
        if (anchor == null) {
            return
        }
        // 处理高版本显示问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val rect = Rect()
            anchor.getGlobalVisibleRect(rect)
            val h = anchor.resources.displayMetrics.heightPixels - rect.bottom
            height = h
        }
        super.showAsDropDown(anchor)
    }

    /**
     * 设置显示在 View 的上方(以 View 的左边距为开始位置)
     *
     * @param v 要显示在上方的 View
     */
    fun showAsUp(v: View) {
        //获取需要在其上方显示的控件的位置信息
        val location = IntArray(2)
        v.getLocationOnScreen(location)
        //获取自身的长宽高
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupHeight = contentView.measuredHeight
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, 0, location[1] - popupHeight)
    }

    /**
     * 在屏幕下方显示
     *
     * @param activity [AppCompatActivity] 对象
     */
    open fun showAtBottom(activity: AppCompatActivity = mContext) {
        var height = 0
        if (isSupportNavBar) {
            // 支持虚拟按键
            if (activity.isNavBarVisible()) {
                // 显示导航栏
                height = navBarHeight
            }
        }
        showAtLocation(activity.window.decorView, Gravity.BOTTOM, 0, height)
    }
}