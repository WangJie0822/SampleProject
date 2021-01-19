package com.wj.sampleproject.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import cn.wj.android.base.ext.startTargetActivity
import cn.wj.android.recyclerview.adapter.simple.SimpleRvListAdapter
import cn.wj.android.recyclerview.layoutmanager.FlowLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.jeremyliao.liveeventbus.LiveEventBus
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.constants.EVENT_COLLECTION_REFRESH_COLLECTED_WEB
import com.wj.sampleproject.databinding.AppActivityCollectedWebBinding
import com.wj.sampleproject.dialog.EditCollectedWebDialog
import com.wj.sampleproject.entity.CollectedWebEntity
import com.wj.sampleproject.helper.ProgressDialogHelper
import com.wj.sampleproject.router.ROUTER_PATH_COLLECTED_WEB
import com.wj.sampleproject.viewmodel.CollectedWebViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 收藏网站界面
 * > 用户收藏的三方网站列表，可以新增、修改、删除
 *
 * - 创建时间：2019/10/16
 *
 * @author 王杰
 */
@Route(path = ROUTER_PATH_COLLECTED_WEB)
class CollectedWebActivity
    : BaseActivity<CollectedWebViewModel, AppActivityCollectedWebBinding>() {

    override val viewModel: CollectedWebViewModel by viewModel()

    /** 列表适配器对象 */
    private val mAdapter = SimpleRvListAdapter<CollectedWebEntity>(R.layout.app_recycler_item_collected_web)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_collected_web)

        // 配置 RecyclerView
        mBinding.rvWebs.let { rv ->
            rv.layoutManager = FlowLayoutManager()
            rv.adapter = mAdapter.also {
                it.viewModel = viewModel
                it.setEmptyView(R.layout.app_layout_placeholder)
            }
        }

        // 自动加载数据
        viewModel.refreshing.set(true)
    }

    override fun initObserve() {
        // 进度弹窗
        viewModel.progressData.observe(this, { progress ->
            if (null == progress) {
                ProgressDialogHelper.dismiss()
            } else {
                ProgressDialogHelper.show(mContext, progress.cancelable, progress.hint)
            }
        })
        // 收藏列表
        viewModel.websListData.observe(this, {
            mAdapter.submitList(it)
        })
        // Popup
        viewModel.popupMenuData.observe(this, Observer {
            if (null == it) {
                return@Observer
            }
            PopupMenu(mContext, it.view).apply {
                inflate(R.menu.app_menu_collected_web_item)
                setOnMenuItemClickListener(it.callback)
            }.show()
        })
        // 编辑弹窗
        viewModel.editDialogData.observe(this, {
            EditCollectedWebDialog.actionShow(mContext, it)
        })
        // 跳转 WebView
        viewModel.jumpWebViewData.observe(this, {
            WebViewActivity.actionStart(mContext, it)
        })
        // LiveEvent
        LiveEventBus.get(EVENT_COLLECTION_REFRESH_COLLECTED_WEB)
                .observe(this, {
                    viewModel.refreshing.set(true)
                })
    }

    companion object {

        /** 使用 [context] 打开 [CollectedWebActivity] 界面 */
        fun actionStart(context: Context) {
            context.startTargetActivity<CollectedWebActivity>()
        }
    }
}