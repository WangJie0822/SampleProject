package com.wj.sampleproject

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cn.wj.android.base.ext.recycler.adapter.SimpleRvAdapter
import cn.wj.android.base.tools.parseColor
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivityMainBinding
import com.wj.sampleproject.mvvm.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity
    : BaseActivity<MainViewModel, AppActivityMainBinding>() {

    override val mViewModel: MainViewModel by viewModel()

    private val mAdapter = SimpleRvAdapter<String>(R.layout.app_recycler_item_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_main)

        mAdapter.bindViewModel(mViewModel)

        mBinding.rv.layoutManager = GridLayoutManager(mContext, 3)
        mBinding.rv.adapter = mAdapter

        mAdapter.showFooterWhenEmpty = true
        mAdapter.showHeaderWhenEmpty = true

        mAdapter.setEmptyView(TextView(mContext).apply {
            text = "EMPTY"
            setTextColor("#FFFFFF".parseColor())
            setBackgroundColor("#000000".parseColor())
        })

        mAdapter.setOnEmptyClickListener {
            Toast.makeText(mContext, "EMPTY", Toast.LENGTH_SHORT).show()
        }

        mAdapter.addHeaderView(TextView(mContext).apply {
            text = "HEADER"
            setTextColor("#FFFFFF".parseColor())
            setBackgroundColor("#000000".parseColor())
        })

        mAdapter.addFooterView(TextView(mContext).apply {
            text = "FOOTER"
            setTextColor("#FFFFFF".parseColor())
            setBackgroundColor("#000000".parseColor())
        })

        mViewModel.listData.observe(this, Observer {
            if (it.refresh) {
                mAdapter.clear()
            }
            mAdapter.mData.addAll(it.list)
            mAdapter.notifyDataSetChanged()
        })

        mViewModel.clickItem.observe(this, Observer {
            Toast.makeText(mContext, it, Toast.LENGTH_SHORT).show()
        })
    }
}