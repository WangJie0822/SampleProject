package com.wj.sampleproject.activity

import android.os.Bundle
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivityMainBinding
import com.wj.sampleproject.mvvm.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity
    : BaseActivity<MainViewModel, AppActivityMainBinding>() {

    override val mViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_main)

    }
}