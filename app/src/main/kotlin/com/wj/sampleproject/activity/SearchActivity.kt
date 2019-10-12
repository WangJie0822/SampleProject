package com.wj.sampleproject.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivitySearchBinding
import com.wj.sampleproject.mvvm.SearchViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 搜索界面
 */
class SearchActivity
    : BaseActivity<SearchViewModel, AppActivitySearchBinding>() {

    companion object {
        /**
         * 界面入口
         *
         * @param context Context 对象
         */
        fun actionStart(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java).apply {
                if (context !is Activity) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            })
        }
    }

    override val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_search)
    }
}