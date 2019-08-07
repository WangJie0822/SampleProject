package cn.wj.android.base.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import cn.wj.android.base.R

/**
 * 自定义 ViewPager，可配置能否滑动
 *
 * @author 王杰
 */
class CustomViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : ViewPager(context, attrs) {

    /** 是否允许滑动 */
    private var mScrollable = true

    /** 是否修正高度 */
    private var mFixHeight = false

    init {
        val types = context.obtainStyledAttributes(attrs, R.styleable.CustomViewPager)
        mScrollable = types.getBoolean(R.styleable.CustomViewPager_scrollable, true)
        mFixHeight = types.getBoolean(R.styleable.CustomViewPager_fixHeight, false)
        types.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return mScrollable && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return mScrollable && super.onInterceptTouchEvent(ev)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mFixHeight) {
            val newHeightMeasureSpec: Int
            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
                val h = child.measuredHeight
                if (h > height) height = h
            }

            newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}