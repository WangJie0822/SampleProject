package com.wj.sampleproject.viewmodel

import android.view.inputmethod.EditorInfo
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.common.ext.isNotNullAndBlank
import cn.wj.common.ext.orEmpty
import com.orhanobut.logger.Logger
import com.wj.sampleproject.R
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.databinding.SmartRefreshState
import com.wj.sampleproject.entity.HotSearchEntity
import com.wj.sampleproject.ext.defaultFaildBlock
import com.wj.sampleproject.ext.judge
import com.wj.sampleproject.ext.toSnackbarModel
import com.wj.sampleproject.interfaces.ArticleListPagingInterface
import com.wj.sampleproject.interfaces.impl.ArticleListPagingInterfaceImpl
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.ArticleRepository
import kotlinx.coroutines.launch

/**
 * 搜索 ViewModel，注入 [repository] 获取数据
 */
class SearchViewModel(
        private val repository: ArticleRepository
) : BaseViewModel(),
        ArticleListPagingInterface by ArticleListPagingInterfaceImpl(repository) {

    init {
        viewModel = this
        getArticleList = { pageNum ->
            repository.search(pageNum, keywords.get().orEmpty())
        }
    }

    /** 热搜数据 */
    val hotSearchData = MutableLiveData<ArrayList<HotSearchEntity>>()

    /** 搜索关键字 */
    val keywords: ObservableField<String> = ObservableField("")

    /** 标记 - 是否显示搜索热词 */
    val showHotSearch: ObservableBoolean = object : ObservableBoolean(keywords) {
        override fun get(): Boolean {
            return keywords.get().isNullOrBlank()
        }
    }

    /** 软键盘搜索 */
    val onSearchAction: (Int) -> Boolean = { actionId ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // 搜索
            if (keywords.get().isNullOrBlank()) {
                snackbarData.value = SnackbarModel(R.string.app_please_enter_keywords)
                true
            } else {
                refreshing.value = SmartRefreshState(true)
                false
            }
        } else {
            false
        }
    }

    /** 返回点击 */
    val onBackClick = {
        uiCloseData.value = UiCloseModel()
    }

    /** 热门搜索条目点击 */
    val onHotSearchItemClick: (HotSearchEntity) -> Unit = { item ->
        keywords.set(item.name)
        if (keywords.get().isNotNullAndBlank()) {
            onRefresh.invoke()
        }
    }

    /** 获取热搜数据 */
    fun getHotSearch() {
        viewModelScope.launch {
            try {
                // 获取热搜数据
                repository.getHotSearch()
                        .judge(onSuccess = {
                            // 获取成功
                            hotSearchData.value = data.orEmpty()
                        }, onFailed = defaultFaildBlock)
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getHotSearch")
                // 获取失败，提示、回滚收藏状态
                snackbarData.value = throwable.toSnackbarModel()
            }
        }
    }
}