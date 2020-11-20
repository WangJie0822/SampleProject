package com.wj.sampleproject.activity

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import cn.wj.android.base.ext.startTargetActivity
import cn.wj.android.base.tools.jumpToBrowser
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.constants.ACTION_PARCELABLE
import com.wj.sampleproject.databinding.AppActivityWebviewBinding
import com.wj.sampleproject.fragment.WebViewFragment
import com.wj.sampleproject.viewmodel.WebViewViewModel
import kotlinx.android.parcel.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * H5 界面
 */
class WebViewActivity
    : BaseActivity<WebViewViewModel, AppActivityWebviewBinding>() {

    override val viewModel: WebViewViewModel by viewModel()

    /** WebView Fragment 对象 */
    private val webViewFragment: WebViewFragment by lazy {
        WebViewFragment.actionCreate(viewModel.webData.value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_webview)

        // 获取网页数据
        intent.getParcelableExtra<ActionModel>(ACTION_PARCELABLE)?.let { data ->
            viewModel.webData.value = data
        }

        // 加载 Fragment
        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment, webViewFragment).commit()
    }

    override fun initObserve() {
        // 浏览器打开
        viewModel.jumpBrowser.observe(this, {
            jumpToBrowser(webViewFragment.currentUrl, mContext)
            onBackPressed()
        })
    }

    /**
     * 界面跳转数据 Model
     *
     * @param id 文章 id
     * @param title 标题
     * @param url 打开链接
     */
    @Parcelize
    data class ActionModel(
            val id: String,
            val title: String,
            val url: String
    ) : Parcelable

    companion object {

        /**
         * 使用 [context] 打开 [WebViewActivity] 界面，传递参数网页数据[webData]
         */
        fun actionStart(context: Context, webData: ActionModel?) {
            context.startTargetActivity(WebViewActivity::class.java, bundleOf(
                    ACTION_PARCELABLE to webData
            ))
        }
    }
}