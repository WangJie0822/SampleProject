package com.wj.sampleproject.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.ext.string
import com.wj.sampleproject.R
import com.wj.sampleproject.base.viewmodel.BaseViewModel

/**
 * 进度弹窗 ViewModel
 *
 * - 创建时间：2020/11/17
 *
 * @author 王杰
 */
class ProgressViewModel : BaseViewModel() {

    /** 空白点击数据 */
    val blankClickData = MutableLiveData<Int>()

    /** 提示文本 */
    val hintStr: ObservableField<String> = ObservableField(R.string.app_in_request.string)

    /** 空白点击事件 */
    val onBlankClick: () -> Unit = {
        blankClickData.value = 0
    }
}