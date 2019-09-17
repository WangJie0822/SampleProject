package com.wj.sampleproject.adapter

import cn.wj.android.recyclerview.adapter.BaseRvDBAdapter
import cn.wj.android.recyclerview.adapter.BaseRvDBViewHolder
import cn.wj.android.recyclerview.adapter.BaseRvViewHolder
import cn.wj.android.recyclerview.adapter.SimpleRvAdapter
import cn.wj.android.recyclerview.layoutmanager.FlowLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.databinding.AppRecyclerItemNavigationBinding
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.NavigationListEntity
import com.wj.sampleproject.mvvm.NavigationViewModel

/**
 * 导航列表适配器类
 */
class NavigationRvAdapter
    : BaseRvDBAdapter<
        NavigationRvAdapter.ViewHolder,
        AppRecyclerItemNavigationBinding,
        NavigationViewModel,
        NavigationListEntity>() {

    override val layoutResID: Int = R.layout.app_recycler_item_navigation

    override fun convert(holder: BaseRvViewHolder<NavigationListEntity>, entity: NavigationListEntity) {
        super.convert(holder, entity)
        (holder as? ViewHolder)?.mBinding?.let { binding ->
            val adapter = SimpleRvAdapter<ArticleEntity>(R.layout.app_recycler_item_navigation_child)
            adapter.mViewModel = mViewModel
            adapter.loadData(entity.articles)
            binding.rvNavigationChild.layoutManager = FlowLayoutManager()
            binding.rvNavigationChild.adapter = adapter
        }
    }

    class ViewHolder(binding: AppRecyclerItemNavigationBinding)
        : BaseRvDBViewHolder<AppRecyclerItemNavigationBinding, NavigationListEntity>(binding)
}