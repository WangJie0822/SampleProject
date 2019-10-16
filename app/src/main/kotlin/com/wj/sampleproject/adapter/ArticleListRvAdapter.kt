package com.wj.sampleproject.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cn.wj.android.recyclerview.adapter.BaseRvDBViewHolder
import cn.wj.android.recyclerview.adapter.BaseRvListDBAdapter
import cn.wj.android.recyclerview.adapter.BaseRvViewHolder
import cn.wj.android.recyclerview.adapter.SimpleRvAdapter
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.databinding.AppRecyclerItemArticlesBinding
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.ArticleTagEntity

/**
 * 首页文章列表列表适配器类
 */
class ArticleListRvAdapter
    : BaseRvListDBAdapter<
        ArticleListRvAdapter.ViewHolder,
        AppRecyclerItemArticlesBinding,
        ArticleListViewModel,
        ArticleEntity>(
        object : DiffUtil.ItemCallback<ArticleEntity>() {
            override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                return oldItem == newItem
            }
        }, true) {

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

interface ArticleListViewModel {

    /** 文章列表条目点击 */
    val onArticleItemClick: (ArticleEntity) -> Unit

    /** 文章收藏点击 */
    val onArticleCollectClick: (ArticleEntity) -> Unit
}