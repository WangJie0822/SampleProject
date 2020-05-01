package com.wj.sampleproject.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.Observer
import cn.wj.android.base.tools.jumpToBrowser
import cn.wj.android.base.utils.AppManager
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.constants.ACTION_TITLE
import com.wj.sampleproject.constants.ACTION_WEB_URL
import com.wj.sampleproject.databinding.AppActivityWebviewBinding
import com.wj.sampleproject.fragment.WebViewFragment
import com.wj.sampleproject.viewmodel.WebViewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * H5 界面
 */
class WebViewActivity
    : BaseActivity<WebViewViewModel, AppActivityWebviewBinding>() {
    
    override val viewModel: WebViewViewModel by viewModel()
    
    /** WebView Fragment 对象 */
    private val webViewFragment: WebViewFragment by lazy {
        WebViewFragment.actionCreate(intent.getStringExtra(ACTION_TITLE).orEmpty(), intent.getStringExtra(ACTION_WEB_URL).orEmpty())
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_webview)
        
        // 获取标题
        viewModel.title.set(intent.getStringExtra(ACTION_TITLE).orEmpty())
        
        // 加载 Fragment
        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment, webViewFragment).commit()
    }
    
    override fun initObserve() {
        // 返回点击
        viewModel.navigationData.observe(this, Observer {
            if (!webViewFragment.onKeyDown()) {
                finish()
            }
        })
        // 浏览器打开
        viewModel.jumpBorwser.observe(this, Observer {
            jumpToBrowser(webViewFragment.currentUrl, mContext)
        })
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return webViewFragment.onKeyDown(keyCode) || super.onKeyDown(keyCode, event)
    }
    
    /**
     * 界面跳转数据 Model
     */
    data class ActionModel
    /**
     * @param title 标题
     * @param url 打开链接
     */
    constructor(
            var title: String,
            var url: String
    )
    
    companion object {
        /**
         * 界面入口
         *
         * @param context Context 对象
         * @param title 标题
         * @param url Web Url
         */
        fun actionStart(context: Context = AppManager.getContext(), title: String, url: String) {
            context.startActivity(Intent(context, WebViewActivity::class.java).apply {
                putExtra(ACTION_TITLE, title)
                putExtra(ACTION_WEB_URL, url)
                if (context !is Activity) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            })
        }
    }
}