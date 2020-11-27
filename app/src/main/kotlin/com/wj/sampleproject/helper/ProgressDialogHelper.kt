package com.wj.sampleproject.helper

import androidx.fragment.app.FragmentActivity
import cn.wj.android.base.ext.isMainProcess
import cn.wj.android.base.thread.runOnMainThread
import cn.wj.android.base.utils.AppManager
import com.orhanobut.logger.Logger
import com.wj.sampleproject.dialog.ProgressDialog

/**
 * 加载进度条帮助类
 *
 * - 创建时间：2019/10/14
 *
 * @author 王杰
 */
object ProgressDialogHelper {

    /** 弹窗对象  */
    private var mDialog: ProgressDialog? = null

    /** 使用 [activity] 显示弹窗，传递参数 能否取消[cancelable] & 提示文本[hint] */
    private fun showDialog(activity: FragmentActivity, cancelable: Boolean, hint: String) {
        // 显示前先隐藏
        dismissDialog()
        // 创建并显示 Dialog
        mDialog = ProgressDialog.actionShow(activity, cancelable, hint)
    }

    /** 隐藏弹窗 */
    private fun dismissDialog() {
        if (null == mDialog) {
            return
        }
        try {
            // 隐藏弹窗
            mDialog!!.dismiss()
            // 移除引用
            mDialog = null
        } catch (e: Exception) {
            Logger.t("ProgressDialogHelper").e(e, "dismissDialog")
        }
    }

    /**
     * 使用 [activity] 显示弹窗，传递参数 能否取消 [cancelable] & 提示文本 [hint]
     * > [cancelable] 默认 `true` & [hint] 默认 `""`
     */
    fun show(activity: FragmentActivity, cancelable: Boolean = true, hint: String = "") {
        if (AppManager.getContext().isMainProcess) {
            // 主线程
            showDialog(activity, cancelable, hint)
        } else {
            // 子线程
            runOnMainThread({
                showDialog(activity, cancelable, hint)
            })
        }
    }

    /** 隐藏弹窗 */
    fun dismiss() {
        if (AppManager.getContext().isMainProcess) {
            // 主线程
            dismissDialog()
        } else {
            // 子线程
            runOnMainThread({
                dismissDialog()
            })
        }
    }
}