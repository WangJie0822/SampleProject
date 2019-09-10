package com.wj.sampleproject.adapter

import androidx.recyclerview.widget.RecyclerView
import cn.wj.android.base.ext.orEmpty
import cn.wj.android.recyclerview.adapter.BaseRvDBAdapter
import cn.wj.android.recyclerview.adapter.BaseRvDBViewHolder
import cn.wj.android.recyclerview.adapter.BaseRvViewHolder
import cn.wj.android.recyclerview.adapter.SimpleRvAdapter
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.databinding.AppRecyclerItemHomepageArticlesBinding
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.ArticleTagEntity
import com.wj.sampleproject.mvvm.HomepageViewModel

/**
 * 首页文章列表列表适配器类
 */
class HomepageArticleListRvAdapter
    : BaseRvDBAdapter<
        HomepageArticleListRvAdapter.ViewHolder,
        AppRecyclerItemHomepageArticlesBinding,
        HomepageViewModel,
        ArticleEntity>() {

    override val layoutResID: Int = R.layout.app_recycler_item_homepage_articles

    override fun convert(holder: BaseRvViewHolder<ArticleEntity>, entity: ArticleEntity) {
        super.convert(holder, entity)
        (holder as? ViewHolder)?.mBinding?.let { mBinding ->
            val adapter = SimpleRvAdapter<ArticleTagEntity>(R.layout.app_recycler_item_homepage_article_tags)
            adapter.mViewModel = mViewModel
            adapter.mData.addAll(entity.tags.orEmpty())
            mBinding.rvArticlesTags.layoutManager = WrapContentLinearLayoutManager(RecyclerView.HORIZONTAL)
            mBinding.rvArticlesTags.adapter = adapter
        }
    }

    class ViewHolder(binding: AppRecyclerItemHomepageArticlesBinding)
        : BaseRvDBViewHolder<AppRecyclerItemHomepageArticlesBinding, ArticleEntity>(binding)
}