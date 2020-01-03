@file:Suppress("unused")

package cn.wj.android.swipeback.helper

import android.app.Activity
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import cn.wj.android.swipeback.constants.SwipeBackConfig
import cn.wj.android.swipeback.view.SlideBackView
import kotlin.math.absoluteValue

/**
 * 侧滑返回帮助类
 *
 * @param mActivity [Activity] 对象
 *
 * - 创建时间：2020/1/2
 *
 * @author 王杰
 */
class SwipeBackHelper(private val mActivity: Activity) {

    /** 侧滑返回 View */
    private val slideBackView: SlideBackView = SlideBackView(mActivity)

    init {
        // 将侧滑返回 View 添加到 Activity
        (mActivity.window.decorView as FrameLayout).addView(
                slideBackView,
                FrameLayout.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.MATCH_PARENT
                )
        )
    }

    /** 屏幕宽度 */
    private val screenWidth = mActivity.resources.displayMetrics.widthPixels

    /** 标记 - 是否是从左侧侧滑 */
    private var slideFromStartSide = false
    /** 标记 - 是否是从右侧侧滑 */
    private var slideFromEndSide = false

    /** 按下 x 轴位置 */
    private var mTouchStartX = 0f

    /** 标记 - 是否允许侧滑返回 */
    var swipeBackEnable = true

    /**
     * 拦截[Activity]事件分发并处理侧滑返回
     *
     * @param ev 事件对象
     *
     * @return 是否拦截
     */
    fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (!swipeBackEnable) {
            return false
        }
        return when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // 按下
                mTouchStartX = ev.rawX
                if (ev.rawX < screenWidth * SwipeBackConfig.sensorPercent) {
                    // 边缘侧滑
                    show(true, ev.rawY)
                    slideFromStartSide = true
                } else if (ev.rawX > screenWidth * (1 - SwipeBackConfig.sensorPercent)) {
                    show(false, ev.rawY)
                    slideFromEndSide = true
                }
                slideFromStartSide || slideFromEndSide
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 抬起，取消
                if (slideFromStartSide || slideFromEndSide) {
                    hide()
                    slideFromStartSide = false
                    slideFromEndSide = false
                }
                false
            }
            MotionEvent.ACTION_MOVE -> {
                // 移动
                if (slideFromStartSide || slideFromEndSide) {
                    val x = ev.rawX
                    val moveSize = (x - mTouchStartX).absoluteValue * SwipeBackConfig.slippageFactor
                    val progress = if (moveSize >= SwipeBackConfig.moveMax) 1f else (moveSize / SwipeBackConfig.moveMax)
                    setProgress(progress)
                    true
                } else {
                    false
                }
            }
            else -> {
                // 其他
                false
            }
        }
    }

    /**
     * 显示侧滑 View
     *
     * @param start 是否从左侧侧滑
     * @param y 手指按下 y 轴位置
     */
    private fun show(start: Boolean, y: Float) {
        slideBackView.visibility = View.VISIBLE
        slideBackView.rotation = if (start) 0f else 180f
        slideBackView.translationY = y - (slideBackView.height) / 2
        slideBackView.layoutParams = (slideBackView.layoutParams as FrameLayout.LayoutParams).apply {
            gravity = if (start) Gravity.START else Gravity.END
        }
    }

    /**
     * 隐藏侧滑 View
     * - 滑动距离超过一半时调用[Activity.onBackPressed]
     */
    private fun hide() {
        if (slideBackView.mProgress >= 0.5f) {
            // 滑动距离超过一半
            mActivity.onBackPressed()
            if (mActivity.isFinishing) {
                slideBackView.visibility = View.GONE
            }
        }
        slideBackView.release()
    }

    /**
     * 设置滑动进度
     *
     * @param progress 进度， 0~1
     */
    private fun setProgress(progress: Float) {
        slideBackView.setProgress(progress)
    }
}

/**
 * 拦截[Activity]事件分发
 *
 * @param ev 事件对象
 * @param activityDispatch 处理正常分发代码块
 *
 * @return 是否拦截事件
 */
fun SwipeBackHelper?.dispatchTouchEvent(ev: MotionEvent, activityDispatch: () -> Boolean): Boolean {
    return if (null != this && dispatchTouchEvent(ev)) {
        true
    } else {
        activityDispatch.invoke()
    }
}