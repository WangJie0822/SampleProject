package com.wj.sampleproject.helper

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import cn.wj.android.base.utils.AppManager
import cn.wj.android.common.ext.isMainProcess
import cn.wj.android.common.ext.runOnMainThread
import cn.wj.android.common.thread.MainThreadManager
import cn.wj.android.logger.Logger
import com.wj.sampleproject.R
import com.wj.sampleproject.databinding.AppDialogProgressBinding

/**
 * 加载进度条帮助类
 * <p/>
 * 创建时间：2019/10/14
 *
 * @author 王杰
 */
object ProgressDialogHelper {

    /** 弹窗对象  */
    private var mDialog: Dialog? = null

    /**
     * 显示弹窗
     *
     * @param activity   Activity 对象
     * @param cancelable 能否取消
     */
    private fun showDialog(activity: Activity, cancelable: Boolean) {
        // 显示前先隐藏
        dismissDialog()

        // 加载布局
        val binding = AppDialogProgressBinding.inflate(
                LayoutInflater.from(activity),
                null, false
        )

        // 初始化 Dialog
        mDialog = Dialog(activity, R.style.app_progress_dialog)
        // 设置能否取消
        mDialog!!.setCancelable(cancelable)
        // 设置点击弹窗外不能取消
        mDialog!!.setCanceledOnTouchOutside(false)
        // 设置弹窗布局
        mDialog!!.setContentView(binding.getRoot())
        // 显示
        mDialog!!.show()
    }

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
     * 显示弹窗
     *
     * @param activity   Activity 对象
     * @param cancelable 能否取消
     */
    fun show(activity: Activity, cancelable: Boolean = true) {
        if (AppManager.getContext().isMainProcess) {
            // 主线程
            showDialog(activity, cancelable)
        } else {
            // 子线程
            MainThreadManager.postToMainThread(Runnable {
                showDialog(activity, cancelable)
            })
        }
    }

    /**
     * 隐藏弹窗
     */
    fun dismiss() {
        if (AppManager.getContext().isMainProcess) {
            // 主线程
            dismissDialog()
        } else {
            // 子线程
            runOnMainThread(Runnable {
                dismissDialog()
            })
        }
    }
}