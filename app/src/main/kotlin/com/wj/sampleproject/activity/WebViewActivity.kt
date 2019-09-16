package com.wj.sampleproject.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.constants.ACTION_TITLE
import com.wj.sampleproject.constants.ACTION_WEB_URL
import com.wj.sampleproject.databinding.AppActivityWebviewBinding
import com.wj.sampleproject.mvvm.WebViewViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * H5 界面
 */
class WebViewActivity
    : BaseActivity<WebViewViewModel, AppActivityWebviewBinding>() {

    companion object {
        /**
         * 界面入口
         *
         * @param context Context 对象
         * @param title 标题
         * @param url Web Url
         */
        fun actionStart(context: Context, title: String, url: String) {
            context.startActivity(Intent(context, WebViewActivity::class.java).apply {
                putExtra(ACTION_TITLE, title)
                putExtra(ACTION_WEB_URL, url)
                if (context !is Activity) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            })
        }
    }

    override val mViewModel: WebViewViewModel by viewModel()

    /** WebView url */
    private val mUrl: String by lazy {
        intent.getStringExtra(ACTION_WEB_URL).orEmpty()
    }

    override fun initBefore() {
        // 标题
        mViewModel.title.set(intent.getStringExtra(ACTION_TITLE).orEmpty())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_webview)

        // 设置 Toolbar
        setSupportActionBar(mBinding.toolbar)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 返回按钮
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBinding.wv.canGoBack()) {
            mBinding.wv.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}