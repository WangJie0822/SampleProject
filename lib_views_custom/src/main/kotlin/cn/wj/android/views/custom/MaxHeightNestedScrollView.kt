package cn.wj.android.views.custom

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView

/**
 * 可设置最大高度 ScrollView
 *
 * @author 王杰
 */
class MaxHeightNestedScrollView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    /** 最大高度 */
    private var mMaxHeight = -1f

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightNestedScrollView, defStyleAttr, 0)
        mMaxHeight = a.getDimension(R.styleable.MaxHeightNestedScrollView_mhnsv_maxHeight, -1f)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newHeightMeasureSpec = heightMeasureSpec
        if (mMaxHeight != -1f) {
            // 设置最大高度
            newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight.toInt(), MeasureSpec.AT_MOST)
        }
        // 重新计算控件高、宽
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    /**
     * 设置控件最大高度
     */
    @Suppress("unused")
    fun setMaxHeight(height: Float) {
        mMaxHeight = height
        requestLayout()
    }
}