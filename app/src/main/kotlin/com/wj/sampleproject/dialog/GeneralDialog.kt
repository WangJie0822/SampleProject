@file:Suppress("unused")

package com.wj.sampleproject.dialog

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import cn.wj.android.base.tools.getString
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseDialog
import com.wj.sampleproject.databinding.AppDialogGeneralBinding
import com.wj.sampleproject.viewmodel.GeneralViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 通用弹窗
 *
 * * 创建时间：2019/9/28
 *
 * @author 王杰
 */
class GeneralDialog
/**
 * 私有化构造
 *
 * 请使用 [.newBuilder] 获取对象
 */
private constructor() : BaseDialog<GeneralViewModel, AppDialogGeneralBinding>() {

    /** 消极按钮点击  */
    private var onNegativeClick: OnDialogClickListener? = null

    /** 积极按钮点击  */
    private var onPositiveClick: OnDialogClickListener? = null

    /** 消极按钮点击  */
    private var onNegativeAction: OnDialogActionListener? = null

    /** 积极按钮点击  */
    private var onPositiveAction: OnDialogActionListener? = null

    /** Builder 对象  */
    var builder: Builder? = null

    // 提高可见范围，对外暴露
    public override val viewModel: GeneralViewModel by viewModel()

    override val layoutResId = R.layout.app_dialog_general

    override fun initView() {
        // 从 argument 中获取数据
        val arguments = arguments ?: return
        // 设置 UI 显示
        viewModel.showTitle.set(arguments.getBoolean(ACTION_SHOW_TITLE, false))
        viewModel.titleStr.set(arguments.getString(ACTION_TITLE_STR, ""))
        viewModel.contentStr.set(arguments.getString(ACTION_CONTENT_STR, ""))
        viewModel.contentGravity.set(arguments.getInt(ACTION_CONTENT_GRAVITY, 0))
        viewModel.showSelect.set(arguments.getBoolean(ACTION_SHOW_SELECT, false))
        viewModel.selected.set(arguments.getBoolean(ACTION_SELECTED, false))
        viewModel.selectStr.set(arguments.getString(ACTION_SELECT_STR, ""))
        viewModel.showNegativeButton.set(arguments.getBoolean(ACTION_SHOW_NEGATIVE_BUTTON, true))
        viewModel.negativeButtonStr.set(arguments.getString(ACTION_NEGATIVE_BUTTON_STR, ""))
        viewModel.showPositiveButton.set(arguments.getBoolean(ACTION_SHOW_POSITIVE_BUTTON, true))
        viewModel.positiveButtonStr.set(arguments.getString(ACTION_POSITIVE_BUTTON_STR))
    }

    override fun initObserve() {
        // 消极按钮点击
        viewModel.negativeClickData.observe(this, Observer {
            // 选中状态
            var selected = false
            val selectedObj = viewModel.selected.get()
            if (null != selectedObj) {
                selected = selectedObj
            }

            if (null != onNegativeAction) {
                // 优先处理 Action
                onNegativeAction?.invoke(selected)
                // 隐藏弹窗
                dismiss()
                return@Observer
            }

            if (null != onNegativeClick) {
                // 处理 Click
                onNegativeClick?.invoke(this, selected)
                return@Observer
            }

            // 未设置监听，默认隐藏
            dismiss()
        })

        // 积极按钮点击
        viewModel.positiveClickData.observe(this, Observer {
            // 选中状态
            var selected = false
            val selectedObj = viewModel.selected.get()
            if (null != selectedObj) {
                selected = selectedObj
            }

            if (null != onPositiveAction) {
                // 优先处理 Action
                onPositiveAction?.invoke(selected)
                // 隐藏弹窗
                dismiss()
                return@Observer
            }

            if (null != onPositiveClick) {
                // 处理 Click
                onPositiveClick?.invoke(this, selected)
                return@Observer
            }

            // 未设置监听，默认隐藏
            dismiss()
        })
    }

    /**
     * 消极按钮点击事件
     *
     * @param onNegativeClick 点击回调
     */
    private fun setOnNegativeClick(onNegativeClick: OnDialogClickListener?) {
        this.onNegativeClick = onNegativeClick
    }

    /**
     * 积极按钮点击事件
     *
     * @param onPositiveClick 点击回调
     */
    private fun setOnPositiveClick(onPositiveClick: OnDialogClickListener?) {
        this.onPositiveClick = onPositiveClick
    }

    /**
     * 消极按钮点击事件
     *
     *
     * 按钮点击后自动隐藏弹窗
     *
     * @param onNegativeAction 点击回调
     */
    private fun setOnNegativeAction(onNegativeAction: OnDialogActionListener?) {
        this.onNegativeAction = onNegativeAction
    }

    /**
     * 积极按钮点击事件
     *
     *
     * 按钮点击后自动隐藏弹窗
     *
     * @param onPositiveAction 点击回调
     */
    private fun setOnPositiveAction(onPositiveAction: OnDialogActionListener?) {
        this.onPositiveAction = onPositiveAction
    }

    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, "GeneralDialog")
    }

    fun show(fragment: Fragment) {
        show(fragment.childFragmentManager, "GeneralDialog")
    }

    /**
     * [GeneralDialog] 通用弹窗建造者对象
     */
    class Builder
    /**
     * 私有化构造
     *
     *
     * 请使用 [GeneralDialog.newBuilder] or [GeneralDialog.newBuilder]
     *
     * @param builder [Builder] 对象
     */(builder: Builder?) {

        /** 标记 - 能否取消  */
        private var cancelable = false
        /** 标记 - 是否显示标题  */
        private var showTitle = false
        /** 标题文本  */
        private var titleStr = ""
        /** 内容文本  */
        private var contentStr = ""
        /** 内容文本重心  */
        private var contentGravity = Gravity.START or Gravity.CENTER_VERTICAL
        /** 标记 - 是否显示选择器  */
        private var showSelect = false
        /** 标记 - 选择器是否选中  */
        private var selected = false
        /** 选择器文本 - 默认：不再提示  */
        private var selectStr = R.string.app_no_longer_tips.getString()
        /** 标记 - 是否显示消极按钮  */
        private var showNegativeButton = true
        /** 消极按钮文本 - 默认：取消  */
        private var negativeButtonStr = R.string.app_cancel.getString()
        /** 标记 - 是否显示积极按钮  */
        private var showPositiveButton = true
        /** 积极按钮文本 - 默认：确认  */
        private var positiveButtonStr = R.string.app_confirm.getString()
        /** 消极按钮点击  */
        private var onNegativeClick: OnDialogClickListener? = null
        /** 积极按钮点击  */
        private var onPositiveClick: OnDialogClickListener? = null
        /** 消极按钮点击  */
        private var onNegativeAction: OnDialogActionListener? = null
        /** 积极按钮点击  */
        private var onPositiveAction: OnDialogActionListener? = null

        init {
            if (null != builder) {
                cancelable = builder.cancelable
                showTitle = builder.showTitle
                titleStr = builder.titleStr
                contentStr = builder.contentStr
                contentGravity = builder.contentGravity
                showSelect = builder.showSelect
                selected = builder.selected
                selectStr = builder.selectStr
                showNegativeButton = builder.showNegativeButton
                negativeButtonStr = builder.negativeButtonStr
                showPositiveButton = builder.showPositiveButton
                positiveButtonStr = builder.positiveButtonStr
                onNegativeClick = builder.onNegativeClick
                onPositiveClick = builder.onPositiveClick
                onNegativeAction = builder.onNegativeAction
                onPositiveAction = builder.onPositiveAction
            }
        }

        /**
         * 设置能否取消
         *
         * @param cancelable 能否取消
         *
         * @return Builder 对象
         */
        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        /**
         * 设置是否显示标题
         *
         * @param showTitle 是否显示标题
         *
         * @return Builder 对象
         */
        fun showTitle(showTitle: Boolean): Builder {
            this.showTitle = showTitle
            return this
        }

        /**
         * 设置标题文本
         *
         * @param titleStr 标题文本
         *
         * @return Builder 对象
         */
        fun titleStr(titleStr: String): Builder {
            this.titleStr = titleStr
            return this
        }

        /**
         * 设置内容文本
         *
         * @param contentStr 内容文本
         *
         * @return Builder 对象
         */
        fun contentStr(contentStr: String): Builder {
            this.contentStr = contentStr
            return this
        }

        /**
         * 设置内容文本重心
         *
         * @param gravity 重心 [Gravity]
         *
         * @return Builder 对象
         */
        fun contentGravity(gravity: Int): Builder {
            this.contentGravity = gravity
            return this
        }

        /**
         * 设置是否显示选择器
         *
         * @param showSelect 是否显示选择器
         *
         * @return Builder 对象
         */
        fun showSelect(showSelect: Boolean): Builder {
            this.showSelect = showSelect
            return this
        }

        /**
         * 设置选择器默认选择状态
         *
         * @param selected 默认选择状态
         *
         * @return Builder 对象
         */
        fun defaultSelected(selected: Boolean): Builder {
            this.selected = selected
            return this
        }

        /**
         * 设置选择器提示文本
         *
         * @param selectStr 选择器提示文本
         *
         * @return Builder 对象
         */
        fun selectTipsStr(selectStr: String): Builder {
            this.selectStr = selectStr
            return this
        }

        /**
         * 设置是否显示消极按钮
         *
         * @param showNegativeButton 是否显示消极按钮
         *
         * @return Builder 对象
         */
        fun showNegativeButton(showNegativeButton: Boolean): Builder {
            this.showNegativeButton = showNegativeButton
            return this
        }

        /**
         * 设置消极按钮显示文本
         *
         * @param negativeButtonStr 消极按钮显示文本
         *
         * @return Builder 对象
         */
        fun negativeButtonStr(negativeButtonStr: String): Builder {
            this.negativeButtonStr = negativeButtonStr
            return this
        }

        /**
         * 设置是否显示积极按钮
         *
         * @param showPositiveButton 是否显示积极按钮
         *
         * @return Builder 对象
         */
        fun showPositiveButton(showPositiveButton: Boolean): Builder {
            this.showPositiveButton = showPositiveButton
            return this
        }

        /**
         * 积极按钮显示文本
         *
         * @param positiveButtonStr 积极按钮显示文本
         *
         * @return Builder 对象
         */
        fun positiveButtonStr(positiveButtonStr: String): Builder {
            this.positiveButtonStr = positiveButtonStr
            return this
        }

        /**
         * 消极按钮点击事件
         *
         * @param onNegativeClick 点击回调
         *
         * @return Builder 对象
         */
        fun setOnNegativeClick(onNegativeClick: OnDialogClickListener): Builder {
            this.onNegativeClick = onNegativeClick
            return this
        }

        /**
         * 积极按钮点击事件
         *
         * @param onPositiveClick 点击回调
         *
         * @return Builder 对象
         */
        fun setOnPositiveClick(onPositiveClick: OnDialogClickListener): Builder {
            this.onPositiveClick = onPositiveClick
            return this
        }

        /**
         * 消极按钮点击事件
         *
         *
         * 按钮点击后自动隐藏弹窗
         *
         * @param onNegativeAction 点击回调
         *
         * @return Builder 对象
         */
        fun setOnNegativeAction(onNegativeAction: OnDialogActionListener): Builder {
            this.onNegativeAction = onNegativeAction
            return this
        }

        /**
         * 积极按钮点击事件
         *
         *
         * 按钮点击后自动隐藏弹窗
         *
         * @param onPositiveAction 点击回调
         *
         * @return Builder 对象
         */
        fun setOnPositiveAction(onPositiveAction: OnDialogActionListener): Builder {
            this.onPositiveAction = onPositiveAction
            return this
        }

        /**
         * 消极按钮点击事件
         *
         * @param negativeButtonStr 消极按钮文本
         * @param onNegativeClick   点击回调
         *
         * @return Builder 对象
         */
        fun setNegativeButton(negativeButtonStr: String, onNegativeClick: OnDialogClickListener): Builder {
            this.negativeButtonStr = negativeButtonStr
            this.onNegativeClick = onNegativeClick
            return this
        }

        /**
         * 设置积极按钮
         *
         * @param positiveButtonStr 消极按钮文本
         * @param onPositiveClick   点击回调
         *
         * @return Builder 对象
         */
        fun setPositiveButton(positiveButtonStr: String, onPositiveClick: OnDialogClickListener): Builder {
            this.positiveButtonStr = positiveButtonStr
            this.onPositiveClick = onPositiveClick
            return this
        }

        /**
         * 消极按钮点击事件
         *
         *
         * 按钮点击后自动隐藏弹窗
         *
         * @param negativeButtonStr 消极按钮文本
         * @param onNegativeAction  点击回调
         *
         * @return Builder 对象
         */
        fun setNegativeAction(negativeButtonStr: String, onNegativeAction: OnDialogActionListener): Builder {
            this.negativeButtonStr = negativeButtonStr
            this.onNegativeAction = onNegativeAction
            return this
        }

        /**
         * 设置积极按钮
         *
         *
         * 按钮点击后自动隐藏弹窗
         *
         * @param positiveButtonStr 消极按钮文本
         * @param onPositiveAction  点击回调
         *
         * @return Builder 对象
         */
        fun setPositiveAction(positiveButtonStr: String, onPositiveAction: OnDialogActionListener): Builder {
            this.positiveButtonStr = positiveButtonStr
            this.onPositiveAction = onPositiveAction
            return this
        }

        /**
         * 建造 [GeneralDialog] 对象
         *
         * @return [GeneralDialog] 对象
         */
        fun build(): GeneralDialog {
            val dialog = GeneralDialog()
            dialog.isCancelable = cancelable
            val args = Bundle()
            args.putBoolean(ACTION_SHOW_TITLE, showTitle)
            args.putString(ACTION_TITLE_STR, titleStr)
            args.putString(ACTION_CONTENT_STR, contentStr)
            args.putInt(ACTION_CONTENT_GRAVITY, contentGravity)
            args.putBoolean(ACTION_SHOW_SELECT, showSelect)
            args.putBoolean(ACTION_SELECTED, selected)
            args.putString(ACTION_SELECT_STR, selectStr)
            args.putBoolean(ACTION_SHOW_NEGATIVE_BUTTON, showNegativeButton)
            args.putString(ACTION_NEGATIVE_BUTTON_STR, negativeButtonStr)
            args.putBoolean(ACTION_SHOW_POSITIVE_BUTTON, showPositiveButton)
            args.putString(ACTION_POSITIVE_BUTTON_STR, positiveButtonStr)
            dialog.arguments = args
            dialog.setOnNegativeClick(onNegativeClick)
            dialog.setOnNegativeAction(onNegativeAction)
            dialog.setOnPositiveClick(onPositiveClick)
            dialog.setOnPositiveAction(onPositiveAction)
            dialog.builder = this
            return dialog
        }
    }

    companion object {

        /** 参数 Key - 是否显示标题  */
        private const val ACTION_SHOW_TITLE = "action_show_title"

        /** 参数 Key - 标题文本  */
        private const val ACTION_TITLE_STR = "action_title_str"

        /** 参数 Key - 内容文本  */
        private const val ACTION_CONTENT_STR = "action_content_str"

        /** 参数 Key - 内容文本重心  */
        private const val ACTION_CONTENT_GRAVITY = "action_content_gravity"

        /** 参数 Key - 是否显示选择器  */
        private const val ACTION_SHOW_SELECT = "action_show_select"

        /** 参数 Key - 选择器是否选中  */
        private const val ACTION_SELECTED = "action_selected"

        /** 参数 Key - 选择器文本  */
        private const val ACTION_SELECT_STR = "ACTION_SELECT_STR"

        /** 参数 Key - 是否显示消极按钮  */
        private const val ACTION_SHOW_NEGATIVE_BUTTON = "action_show_negative_button"

        /** 参数 Key - 消极按钮文本  */
        private const val ACTION_NEGATIVE_BUTTON_STR = "action_negative_button_str"

        /** 参数 Key - 是否显示积极按钮  */
        private const val ACTION_SHOW_POSITIVE_BUTTON = "action_show_positive_button"

        /** 参数 Key - 积极按钮文本  */
        private const val ACTION_POSITIVE_BUTTON_STR = "action_positive_button_str"

        /**
         * 新建 [Builder] 建造者对象
         *
         * @return [Builder] 建造者对象
         */
        fun newBuilder(): Builder {
            return Builder(null)
        }

        /**
         * 从已有 [Builder] 建造者对象 新建 [Builder] 建造者对象
         *
         * @param builder 已有 [Builder] 建造者对象
         *
         * @return [Builder] 建造者对象
         */
        fun newBuilder(builder: Builder): Builder {
            return Builder(builder)
        }
    }
}

/**
 * Dialog 按钮点击回调接口
 */
typealias OnDialogClickListener = (GeneralDialog, Boolean) -> Unit

/**
 * Dialog 按钮点击回调接口
 *
 * 按钮点击后自动隐藏弹窗
 */
typealias OnDialogActionListener = (Boolean) -> Unit
