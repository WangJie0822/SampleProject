package com.wj.sampleproject.adapter

import cn.wj.android.recyclerview.adapter.base.BaseRvDBAdapter
import cn.wj.android.recyclerview.adapter.simple.SimpleRvAdapter
import cn.wj.android.recyclerview.holder.BaseRvDBViewHolder
import cn.wj.android.recyclerview.holder.BaseRvViewHolder
import cn.wj.android.recyclerview.layoutmanager.FlowLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.databinding.AppRecyclerItemSystemCategoryBinding
import com.wj.sampleproject.entity.SystemCategoryEntity
import com.wj.sampleproject.viewmodel.SystemCategoryViewModel

/**
 * 体系目录列表适配器类
 */
class SystemCategoryRvAdapter
    : BaseRvDBAdapter<
        SystemCategoryRvAdapter.ViewHolder,
        AppRecyclerItemSystemCategoryBinding,
        SystemCategoryViewModel,
        SystemCategoryEntity>() {

    override val layoutResID: Int = R.layout.app_recycler_item_system_category

    override fun convert(holder: BaseRvViewHolder<SystemCategoryEntity>, entity: SystemCategoryEntity) {
        super.convert(holder, entity)
        (holder as? ViewHolder)?.mBinding?.let { binding ->
            val adapter = SimpleRvAdapter<SystemCategoryEntity>(R.layout.app_recycler_item_system_category_child)
            adapter.viewModel = viewModel
            adapter.loadMore(entity.children)
            binding.rvSystemCategoryChild.layoutManager = FlowLayoutManager()
            binding.rvSystemCategoryChild.adapter = adapter
        }
    }

    class ViewHolder(binding: AppRecyclerItemSystemCategoryBinding)
        : BaseRvDBViewHolder<AppRecyclerItemSystemCategoryBinding, SystemCategoryEntity>(binding)
}