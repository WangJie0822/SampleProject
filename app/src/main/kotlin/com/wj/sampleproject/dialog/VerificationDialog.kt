package com.wj.sampleproject.dialog

import androidx.appcompat.app.AppCompatActivity
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseDialog
import com.wj.sampleproject.databinding.AppDialogVerificationBinding
import com.wj.sampleproject.helper.ProgressDialogHelper
import com.wj.sampleproject.viewmodel.VerificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 安全验证弹窗
 *
 * - 创建时间：2021/1/11
 *
 * @author 王杰
 */
class VerificationDialog
    : BaseDialog<VerificationViewModel, AppDialogVerificationBinding>() {

    override val viewModel: VerificationViewModel by viewModel()

    override val layoutResId: Int = R.layout.app_dialog_verification

    /** 验证成功回调 */
    private var callback: ((String) -> Unit)? = null

    override fun initView() {
        setOnDialogDismissListener {
            callback?.invoke(viewModel.verificationInfo)
        }
    }

    override fun initObserve() {
        // 进度弹窗
        viewModel.progressData.observe(this@VerificationDialog, { progress ->
            if (null == progress) {
                ProgressDialogHelper.dismiss()
            } else {
                ProgressDialogHelper.show(mContext, progress.cancelable, progress.hint)
            }
        })
    }

    companion object {

        /**
         * 使用 [activity] 创建、显示并返回 [VerificationDialog] 弹窗，回调 [callback]，返回用户账号信息 [String]，取消返回 `""`
         */
        fun actionShow(activity: AppCompatActivity, callback: (String) -> Unit): VerificationDialog {
            return VerificationDialog().apply {
                this.callback = callback
                show(activity.supportFragmentManager, "VerificationDialog")
            }
        }
    }
}