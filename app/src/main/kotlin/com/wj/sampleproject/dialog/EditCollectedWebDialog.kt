package com.wj.sampleproject.dialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.wj.android.base.ext.string
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseDialog
import com.wj.sampleproject.constants.ACTION_EDIT_COLLECTED_WEB
import com.wj.sampleproject.databinding.AppDialogEditCollectedWebBinding
import com.wj.sampleproject.entity.CollectedWebEntity
import com.wj.sampleproject.viewmodel.EditCollectedWebViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 编辑收藏网站 Dialog
 *
 * - 创建时间：2019/10/17
 *
 * @author 王杰
 */
class EditCollectedWebDialog : BaseDialog<EditCollectedWebViewModel, AppDialogEditCollectedWebBinding>() {
    
    override val viewModel: EditCollectedWebViewModel by viewModel()
    
    override val layoutResId: Int = R.layout.app_dialog_edit_collected_web
    
    override fun initView() {
        // 获取数据
        val entity: CollectedWebEntity? = arguments?.getParcelable(ACTION_EDIT_COLLECTED_WEB)
        if (null == entity || entity.id.isNullOrBlank()) {
            // 新建
            viewModel.titleStr.set(R.string.app_add_collected_web.string)
        } else {
            // 编辑
            viewModel.titleStr.set(R.string.app_edit_collected_web.string)
            viewModel.id = entity.id.orEmpty()
            viewModel.webName.set(entity.name)
            viewModel.webLink.set(entity.link)
        }
    }
    
    companion object {
        /**
         * 显示弹窗
         *
         * @param activity [AppCompatActivity] 对象
         */
        fun actionShow(activity: AppCompatActivity, entity: CollectedWebEntity? = null): EditCollectedWebDialog {
            return EditCollectedWebDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ACTION_EDIT_COLLECTED_WEB, entity)
                }
                show(activity.supportFragmentManager, "EDIT_COLLECTED_WEB")
            }
        }
    }
}