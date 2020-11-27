package com.wj.sampleproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.common.ext.orEmpty
import com.orhanobut.logger.Logger
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.NavigationListEntity
import com.wj.sampleproject.ext.snackbarMsg
import com.wj.sampleproject.repository.SystemRepository
import kotlinx.coroutines.launch

/**
 * 导航 ViewModel
 *
 * @param repository 体系相关数据仓库
 */
class NavigationViewModel(
        private val repository: SystemRepository
) : BaseViewModel() {

    /** 列表数据 */
    val listData = MutableLiveData<ArrayList<NavigationListEntity>>()

    /** 跳转 WebView */
    val jumpWebViewData = MutableLiveData<WebViewActivity.ActionModel>()

    /** 导航条目点击 */
    val onNavigationItemClick: (ArticleEntity) -> Unit = { item ->
        // 跳转 WebView
        jumpWebViewData.value = WebViewActivity.ActionModel(item.id.orEmpty(), item.title.orEmpty(), item.link.orEmpty())
    }

    /** 获取导航列表 */
    fun getNavigationList() {
        viewModelScope.launch {
            try {
                val result = repository.getNavigationList()
                if (result.success()) {
                    // 获取成功
                    listData.value = result.data.orEmpty()
                } else {
                    snackbarData.value = result.toError()
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "NET_ERROR")
                snackbarData.value = throwable.snackbarMsg
            }
        }
    }
}