package com.wj.sampleproject.activity

import android.content.Context
import android.os.Bundle
import cn.wj.android.base.ext.startTargetActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivityStudyBinding
import com.wj.sampleproject.router.ROUTER_PATH_STUDY
import com.wj.sampleproject.viewmodel.StudyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 学习相关界面
 */
@Route(path = ROUTER_PATH_STUDY)
class StudyActivity
    : BaseActivity<StudyViewModel, AppActivityStudyBinding>() {

    override val viewModel: StudyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_study)
    }

    companion object {

        /** 使用 [context] 对象打开 StudyActivity 界面 */
        fun actionStart(context: Context) {
            context.startTargetActivity<StudyActivity>()
        }
    }
}