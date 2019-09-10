package com.wj.sampleproject.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import cn.wj.android.base.ext.condition
import cn.wj.android.base.ext.orEmpty
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.SearchActivity
import com.wj.sampleproject.adapter.BannerVpAdapter
import com.wj.sampleproject.adapter.HomepageArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentHomepageBinding
import com.wj.sampleproject.mvvm.HomepageViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 主界面 - 首页
 */
class HomepageFragment
    : BaseFragment<HomepageViewModel, AppFragmentHomepageBinding>() {

    companion object {
        /**
         * 创建 Fragment
         *
         * @return 首页 Fragment
         */
        fun actionCreate(): HomepageFragment {
            return HomepageFragment()
        }
    }

    override val mViewModel: HomepageViewModel by viewModel()

    override val layoutResID: Int = R.layout.app_fragment_homepage

    /** Banner 列表适配器对象 */
    private val mBannerAdapter: BannerVpAdapter by inject()
    /** 文章列表适配器对象 */
    private val mArticleAdapter: HomepageArticleListRvAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun initView() {
        // 配置标题栏
        mContext.setSupportActionBar(mBinding.toolbar)

        // 配置 ViewPager
        mBannerAdapter.mViewModel = mViewModel
        mBinding.vpBanner.adapter = mBannerAdapter

        // 配置 RecyclerView
        mArticleAdapter.mViewModel = mViewModel
        mArticleAdapter.setEmptyView(R.layout.app_layout_no_data)
        mBinding.rvArticles.layoutManager = WrapContentLinearLayoutManager()
        mBinding.rvArticles.adapter = mArticleAdapter

        // 注册数据监听
        mViewModel.bannerListData.observe(this, Observer {
            mViewModel.bannerCount = it.size
            mBannerAdapter.mData.clear()
            mBannerAdapter.mData.addAll(it)
            mBannerAdapter.notifyDataSetChanged()
        })
        mViewModel.articleListData.observe(this, Observer {
            mArticleAdapter.loadData(it.list.orEmpty(), it.refresh.condition)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menu_search) {
            // 跳转搜索界面
            SearchActivity.actionStart(mContext)
            true
        } else {
            false
        }
    }
}