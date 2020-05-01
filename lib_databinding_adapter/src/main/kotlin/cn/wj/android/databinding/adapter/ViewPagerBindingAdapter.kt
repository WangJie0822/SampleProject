@file:Suppress("unused")

package cn.wj.android.databinding.adapter

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import cn.wj.android.common.ext.condition

/**
 * ViewPager DataBinding 适配器
 */

/**
 * 设置 ViewPager 页面切换监听
 *
 * @param vp [ViewPager] 对象
 * @param scrolled onPageScrolled 回调
 * @param selected onPageSelected 回调
 * @param changed onPageScrollStateChanged 回调
 */
@BindingAdapter(
        "android:bind_vp_change_scrolled",
        "android:bind_vp_change_selected",
        "android:bind_vp_change_changed",
        "android:bind_vp_currentItemAttrChanged",
        requireAll = false
)
fun setViewPagerPageChangeListener(
        vp: ViewPager,
        scrolled: ((Int, Float, Int) -> Unit)?,
        selected: ((Int) -> Unit)?,
        changed: ((Int) -> Unit)?,
        listener: InverseBindingListener?
) {
    vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            changed?.invoke(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            scrolled?.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            selected?.invoke(position)
            listener?.onChange()
        }
    })
}

/**
 * 设置 ViewPager 预加载页数
 *
 * @param vp [ViewPager] 对象
 * @param offscreenPageLimit 预加载页数
 */
@BindingAdapter("android:bind_vp_offscreenPageLimit")
fun setViewPagerOffscreenPageLimit(vp: ViewPager, offscreenPageLimit: Int?) {
    if (null == offscreenPageLimit) {
        return
    }
    vp.offscreenPageLimit = offscreenPageLimit
}

/**
 * 设置 ViewPager 位置
 *
 * @param vp [ViewPager] 对象
 * @param currentItem 位置
 * @param smoothScroll 是否平滑滚动
 */
@BindingAdapter("android:bind_vp_currentItem", "android:bind_vp_smoothScroll", requireAll = false)
fun setViewPagerCurrentItem(vp: ViewPager, currentItem: Int?, smoothScroll: Boolean?) {
    if (null == currentItem) {
        return
    }
    vp.setCurrentItem(currentItem, smoothScroll.condition)
}

@InverseBindingAdapter(attribute = "android:bind_vp_currentItem")
fun getViewPagerCurrentItem(vp: ViewPager): Int {
    return vp.currentItem
}

/**
 * 设置 ViewPager 适配器
 *
 * @param vp [ViewPager] 对象
 * @param adapter 适配器
 */
@BindingAdapter("android:bind_vp_adapter")
fun setViewPagerAdapter(vp: ViewPager, adapter: PagerAdapter?) {
    vp.adapter = adapter
}