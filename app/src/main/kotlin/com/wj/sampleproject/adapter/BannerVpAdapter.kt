package com.wj.sampleproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.wj.sampleproject.databinding.AppViewpagerItemBannerBinding
import com.wj.sampleproject.entity.BannerEntity
import com.wj.sampleproject.viewmodel.HomepageViewModel

/**
 * 首页 Banner 适配器类
 */
class BannerVpAdapter : PagerAdapter() {

    var mViewModel: HomepageViewModel? = null

    private val mData = arrayListOf<BannerEntity>()

    override fun getCount(): Int {
        return mData.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = AppViewpagerItemBannerBinding.inflate(
                LayoutInflater.from(container.context),
                container, false
        )
        binding.viewModel = mViewModel
        binding.item = mData[position]

        container.addView(binding.root)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    fun refresh(ls: ArrayList<BannerEntity>) {
        mData.clear()
        mData.addAll(ls)
        notifyDataSetChanged()
    }
}