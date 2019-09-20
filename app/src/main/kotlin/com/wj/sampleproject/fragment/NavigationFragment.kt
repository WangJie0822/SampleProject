package com.wj.sampleproject.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.adapter.NavigationRvAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentNavigationBinding
import com.wj.sampleproject.mvvm.NavigationViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 导航页面
 */
class NavigationFragment
    : BaseFragment<NavigationViewModel, AppFragmentNavigationBinding>() {

    companion object {
        /**
         * 创建 Fragment
         *
         * @return 导航 Fragment
         */
        fun actionCreate(): NavigationFragment {
            return NavigationFragment()
        }
    }

    override val mViewModel: NavigationViewModel by viewModel()

    override val layoutResID: Int = R.layout.app_fragment_navigation

    /** 列表适配器对象 */
    private val mAdapter: NavigationRvAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 获取导航数据
        mViewModel.getNavigationList()
    }

    override fun initView() {
        // 配置 RecyclerView
        mAdapter.mViewModel = mViewModel
        mBinding.rvNavigation.layoutManager = WrapContentLinearLayoutManager()
        mBinding.rvNavigation.adapter = mAdapter

        // 注册观察者
        mViewModel.listData.observe(this, Observer {
            mAdapter.loadData(it)
        })
    }
}