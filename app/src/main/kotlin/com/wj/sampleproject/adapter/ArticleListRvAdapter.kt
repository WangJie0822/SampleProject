package com.wj.sampleproject.adapter

import androidx.recyclerview.widget.RecyclerView
import cn.wj.android.recyclerview.adapter.base.BaseRvListDBAdapter
import cn.wj.android.recyclerview.adapter.simple.SimpleRvAdapter
import cn.wj.android.recyclerview.holder.BaseRvDBViewHolder
import cn.wj.android.recyclerview.holder.BaseRvViewHolder
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.databinding.AppRecyclerItemArticlesBinding
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.ArticleTagEntity
import com.wj.sampleproject.interfaces.ArticleListItemInterface

/**
 * 首页文章列表列表适配器类
 */
class ArticleListRvAdapter
    : BaseRvListDBAdapter<
        ArticleListRvAdapter.ViewHolder,
        AppRecyclerItemArticlesBinding,
        ArticleListItemInterface,
        ArticleEntity>() {

    override val layoutResID: Int = R.layout.app_recycler_item_articles

    override fun convert(holder: BaseRvViewHolder<ArticleEntity>, entity: ArticleEntity) {
        super.convert(holder, entity)
        (holder as? ViewHolder)?.mBinding?.let { mBinding ->
            val adapter = SimpleRvAdapter<ArticleTagEntity>(R.layout.app_recycler_item_article_tags)
            adapter.viewModel = viewModel
            adapter.mData.addAll(entity.tags.orEmpty())
            mBinding.rvArticlesTags.layoutManager = WrapContentLinearLayoutManager(RecyclerView.HORIZONTAL)
            mBinding.rvArticlesTags.adapter = adapter
        }
    }

    class ViewHolder(binding: AppRecyclerItemArticlesBinding)
        : BaseRvDBViewHolder<AppRecyclerItemArticlesBinding, ArticleEntity>(binding)
}