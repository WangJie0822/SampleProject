package com.wj.sampleproject.mvvm

import androidx.lifecycle.viewModelScope
import com.wj.sampleproject.base.mvvm.BaseViewModel

/**
 * 搜索 ViewModel
 */
class SearchViewModel
    : BaseViewModel() {

    override fun onCleared() {
        super.onCleared()

        viewModelScope
    }
}