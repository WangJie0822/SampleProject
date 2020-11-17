package com.wj.sampleproject.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.SystemArticlesActivity
import com.wj.sampleproject.adapter.SystemCategoryRvAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentSystemCategoryBinding
import com.wj.sampleproject.viewmodel.SystemCategoryViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 体系目录列表界面
 */
class SystemCategoryFragment
    : BaseFragment<SystemCategoryViewModel, AppFragmentSystemCategoryBinding>() {

    override val viewModel: SystemCategoryViewModel by viewModel()

    override val layoutResId: Int = R.layout.app_fragment_system_category

    /** 列表适配器对象 */
    private val mAdapter: SystemCategoryRvAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 获取分类数据
        viewModel.getSystemCategoryList()
    }

    override fun initView() {
        // 配置 RecyclerView
        mBinding.rvSystemCategory.let { rv ->
            rv.layoutManager = WrapContentLinearLayoutManager()
            rv.adapter = mAdapter.also { it.viewModel = viewModel }
        }
    }

    override fun initObserve() {
        // 目录列表
        viewModel.listData.observe(this, Observer {
            mAdapter.loadMore(it.orEmpty())
        })
        // 跳转体系文章列表
        viewModel.jumpSystemData.observe(this, Observer {
            if (null == it) {
                return@Observer
            }
            SystemArticlesActivity.actionStart(mContext, it.name.orEmpty(), it.id.orEmpty())
        })
    }

    companion object {
        /**
         * 创建 Fragment
         *
         * @return 体系目录列表 Fragment
         */
        fun actionCreate(): SystemCategoryFragment {
            return SystemCategoryFragment()
        }
    }
}