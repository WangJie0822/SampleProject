package com.wj.sampleproject.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivityWebviewBinding
import com.wj.sampleproject.mvvm.WebViewViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * H5 界面
 */
class WebViewActivity
    : BaseActivity<WebViewViewModel, AppActivityWebviewBinding>() {

    override val mViewModel: WebViewViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_webview)

        // 设置 Toolbar
        setSupportActionBar(mBinding.toolbar)
        // 显示标题文本
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 配置 WebView
        val webSettings = mBinding.wv.settings
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        @SuppressLint("SetJavaScriptEnabled")
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

        mBinding.wv.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
//        mBinding.wv.addJavascriptInterface(WebView2JsInterface(), "android")
//        mBinding.wv.webChromeClient = object : WebChromeClient() {
//            override fun onProgressChanged(view: WebView, newProgress: Int) {
//                if (newProgress == 100) {
//                    mBinding.pb.visibility = View.INVISIBLE
//                } else {
//                    if (View.INVISIBLE == mBinding.pb.visibility) {
//                        mBinding.pb.visibility = View.VISIBLE
//                    }
//                    mBinding.pb.progress = newProgress
//                }
//                super.onProgressChanged(view, newProgress)
//            }
//        }

        mBinding.wv.webViewClient = object : WebViewClient() {
            @Suppress("OverridingDeprecatedMember")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

//        mBinding.wv.loadUrl(mWebUrl)

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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBinding.wv.canGoBack()) {
            mBinding.wv.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}