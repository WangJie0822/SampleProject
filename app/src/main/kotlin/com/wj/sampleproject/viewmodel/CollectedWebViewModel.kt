package com.wj.sampleproject.viewmodel

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.common.ext.copy
import cn.wj.android.common.ext.orEmpty
import com.orhanobut.logger.Logger
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.entity.CollectedWebEntity
import com.wj.sampleproject.ext.snackbarMsg
import com.wj.sampleproject.model.ProgressModel
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.ArticleRepository
import kotlinx.coroutines.launch

/**
 * 收藏网站 ViewModel，注入 [repository] 获取数据
 *
 * 创建时间：2019/10/16
 *
 * @author 王杰
 */
class CollectedWebViewModel(
        private val repository: ArticleRepository
) : BaseViewModel() {

    /** 控制进度条弹窗显示  */
    val progressData = MutableLiveData<ProgressModel>()

    /** 列表数据 */
    val websListData = MutableLiveData<ArrayList<CollectedWebEntity>>()

    /** PopupMenu 数据 */
    val popupMenuData = MutableLiveData<PopupModel>()

    /** 编辑弹窗数据 */
    val editDialogData = MutableLiveData<CollectedWebEntity>()

    /** 跳转 WebView */
    val jumpWebViewData = MutableLiveData<WebViewActivity.ActionModel>()

    /** 返回点击 */
    val onBackClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }

    /** 菜单列表点击 */
    val onMenuItemClick: (MenuItem) -> Boolean = {
        if (it.itemId == R.id.menu_add) {
            // 添加
            editDialogData.value = CollectedWebEntity()
        }
        false
    }

    /** 标记 - 是否正在刷新 */
    val refreshing: ObservableBoolean = ObservableBoolean(false)

    /** 刷新回调 */
    val onRefresh: () -> Unit = {
        getCollectedWebList()
    }

    /** 列表点击 */
    val onItemClick: (CollectedWebEntity) -> Unit = { item ->
        // 打开 WebView
        jumpWebViewData.value = WebViewActivity.ActionModel(item.id.orEmpty(), item.name.orEmpty(), item.link.orEmpty())
    }

    /** 列表长按点击 */
    val onItemLongClick: (View, CollectedWebEntity) -> Boolean = { v, item ->
        popupMenuData.value = (PopupModel(v) {
            when (it.itemId) {
                R.id.menu_edit -> {
                    // 编辑
                    editDialogData.value = item
                }
                R.id.menu_delete -> {
                    // 删除
                    deleteCollectedWeb(item)
                }
            }
            true
        })
        true
    }

    /** 获取收藏网站列表 */
    private fun getCollectedWebList() {
        viewModelScope.launch {
            try {
                val result = repository.getCollectedWebList()
                if (result.success()) {
                    // 获取成功
                    websListData.value = result.data.orEmpty()
                } else {
                    snackbarData.value = result.toError()
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getCollectedWebList")
                snackbarData.value = throwable.snackbarMsg
            } finally {
                refreshing.set(false)
            }
        }
    }

    /** 删除收藏网站[item] */
    private fun deleteCollectedWeb(item: CollectedWebEntity) {
        viewModelScope.launch {
            try {
                progressData.value = ProgressModel()
                val result = repository.deleteCollectedWeb(item.id.orEmpty())
                if (result.success()) {
                    // 删除成功，从列表移除
                    val ls = websListData.value.copy()
                    ls.remove(item)
                    websListData.value = ls
                } else {
                    snackbarData.value = result.toError()
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getCollectedWebList")
                snackbarData.value = throwable.snackbarMsg
            } finally {
                progressData.value = null
            }
        }
    }
}

/**
 * Popup 显示数据 Model，持有锚点 [view] 以及菜单点击回调 [callback]
 */
data class PopupModel(
        val view: View,
        val callback: PopupMenu.OnMenuItemClickListener
)