package com.wj.sampleproject.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.constants.ACTION_TITLE
import com.wj.sampleproject.constants.ACTION_WEB_URL
import com.wj.sampleproject.databinding.AppFragmentWebViewBinding
import com.wj.sampleproject.mvvm.WebViewViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * WebView 页面
 */
class WebViewFragment
    : BaseFragment<WebViewViewModel, AppFragmentWebViewBinding>() {

    companion object {
        /**
         * 创建 Fragment
         *
         * @param title 标题
         * @param url Web Url
         *
         * @return WebView Fragment
         */
        fun actionCreate(title: String, url: String): WebViewFragment {
            return WebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ACTION_TITLE, title)
                    putString(ACTION_WEB_URL, url)
                }
            }
        }
    }

    override val viewModel: WebViewViewModel by viewModel()

    override val layoutResId: Int = R.layout.app_fragment_web_view

    private val mUrl: String by lazy {
        arguments?.getString(ACTION_WEB_URL, "").orEmpty()
    }

    override fun initView() {
        // 获取标题
        viewModel.title.set(arguments?.getString(ACTION_TITLE, "").orEmpty())

        // 配置 WebView
        val webSettings = mBinding.wv.settings
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        @SuppressLint("SetJavaScriptEnabled")
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

        mBinding.wv.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        mBinding.wv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }
        }

        // 添加观察者
        viewModel.navigationData.observe(this, Observer {
            if (mBinding.wv.canGoBack()) {
                mBinding.wv.goBack()
            } else {
                mContext.finish()
            }
        })

        // 加载页面
        mBinding.wv.loadUrl(mUrl)
    }

    override fun onResume() {
        super.onResume()
        mBinding.wv.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.wv.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.wv.clearCache(true)
        mBinding.wv.removeAllViews()
        mBinding.wv.destroy()
    }

    fun onKeyDown(keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBinding.wv.canGoBack()) {
            mBinding.wv.goBack()
            return true
        }
        return false
    }
}