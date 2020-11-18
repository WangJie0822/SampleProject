@file:Suppress("unused")

package com.wj.sampleproject.dialog

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseDialog
import com.wj.sampleproject.databinding.AppDialogProgressBinding
import com.wj.sampleproject.viewmodel.ProgressViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 进度条弹窗
 *
 * * 创建时间：2020/11/17
 *
 * @author 王杰
 */
class ProgressDialog
    : BaseDialog<ProgressViewModel, AppDialogProgressBinding>() {

    // 提高可见范围，对外暴露
    public override val viewModel: ProgressViewModel by viewModel()

    override val layoutResId = R.layout.app_dialog_progress

    override fun initView() {
        // 从 argument 中获取数据
        val arguments = arguments ?: return
        isCancelable = arguments.getBoolean(ACTION_EVENT_CANCELABLE, true)
        val hint = arguments.getString(ACTION_EVENT_HINT, "").orEmpty()
        if (hint.isNotBlank()) {
            viewModel.hintStr.set(hint)
        }
    }

    override fun initObserve() {
        viewModel.blankClickData.observe(this, {
            if (isCancelable) {
                dismiss()
            }
        })
    }


    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, "ProgressDialog")
    }

    fun show(fragment: Fragment) {
        show(fragment.childFragmentManager, "ProgressDialog")
    }

    companion object {
        /** 能否取消 */
        private const val ACTION_EVENT_CANCELABLE = "action_event_cancelable"

        /** 提示文本 */
        private const val ACTION_EVENT_HINT = "action_event_hint"

        /**
         * 创建 Dialog
         *
         * @param cancelable 能否取消
         * @param hint 提示文本
         *
         * @return 进度条弹窗
         */
        fun actionCreate(cancelable: Boolean = true, hint: String = ""): ProgressDialog {
            return ProgressDialog().apply {
                arguments = bundleOf(
                        ACTION_EVENT_CANCELABLE to cancelable,
                        ACTION_EVENT_HINT to hint
                )
            }
        }
    }
}