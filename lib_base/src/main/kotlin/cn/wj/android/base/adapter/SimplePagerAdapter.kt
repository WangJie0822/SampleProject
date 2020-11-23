package cn.wj.android.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.PagerAdapter
import cn.wj.android.base.BR

/**
 * 简易的 ViewPager 适配器
 * > 使用 [layoutResId] 布局，[E] 类型的数据
 *
 * - 创建时间：2020/11/20
 *
 * @author 王杰
 */
class SimplePagerAdapter<E>(private val layoutResId: Int)
    : PagerAdapter() {

    /** 数据列表 */
    val mData = arrayListOf<E>()

    /** ViewModel 对象 */
    var viewModel: Any? = null

    override fun getCount(): Int {
        return mData.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(container.context),
                layoutResId, container, false
        )
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.item, mData[position])

        container.addView(binding.root)

        return binding.root
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    /** 刷新列表至 [ls] 数据 */
    fun refresh(ls: Collection<E>?) {
        mData.clear()
        mData.addAll(ls ?: listOf())
        notifyDataSetChanged()
    }
}