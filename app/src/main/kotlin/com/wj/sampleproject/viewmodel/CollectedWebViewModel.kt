package com.wj.sampleproject.viewmodel

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.databinding.BindingField
import cn.wj.android.base.ext.tagableScope
import cn.wj.android.common.ext.orEmpty
import cn.wj.android.common.ext.toNewList
import cn.wj.android.logger.Logger
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.entity.CollectedWebEntity
import com.wj.sampleproject.ext.snackbarMsg
import com.wj.sampleproject.model.ProgressModel
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.CollectRepository
import kotlinx.coroutines.launch

/**
 * 收藏网站 ViewModel
 *
 * 创建时间：2019/10/16
 *
 * @author 王杰
 */
class CollectedWebViewModel
/**
 * @param collectRepository 收藏相关数据仓库
 */
constructor(private val collectRepository: CollectRepository)
    : BaseViewModel() {

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
        uiCloseData.postValue(UiCloseModel())
    }

    /** 菜单列表点击 */
    val onMenuItemClick: (MenuItem) -> Boolean = {
        if (it.itemId == R.id.menu_add) {
            // 添加
            editDialogData.postValue(CollectedWebEntity())
        }
        false
    }

    /** 标记 - 是否正在刷新 */
    val refreshing: BindingField<Boolean> = BindingField(false)

    /** 刷新回调 */
    val onRefresh: () -> Unit = {
        getCollectedWebList()
    }

    /** 列表点击 */
    val onItemClick: (CollectedWebEntity) -> Unit = { item ->
        // 打开 WebView
        jumpWebViewData.postValue(WebViewActivity.ActionModel(item.name.orEmpty(), item.link.orEmpty()))
    }

    /** 列表长按点击 */
    val onItemLongClick: (View, CollectedWebEntity) -> Boolean = { v, item ->
        popupMenuData.postValue(PopupModel(v, PopupMenu.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit -> {
                    // 编辑
                    editDialogData.postValue(item)
                }
                R.id.menu_delete -> {
                    // 删除
                    deleteCollectedWeb(item)
                }
            }
            true
        }))
        true
    }

    /**
     * 获取收藏网站列表
     */
    private fun getCollectedWebList() {
        tagableScope.launch {
            try {
                val result = collectRepository.getCollectedWebList()
                if (result.success()) {
                    // 获取成功
                    websListData.postValue(result.data.orEmpty())
                } else {
                    snackbarData.postValue(result.toError())
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getCollectedWebList")
                snackbarData.postValue(throwable.snackbarMsg)
            } finally {
                refreshing.set(false)
            }
        }
    }

    /**
     * 删除收藏网站
     *
     * @param item 网站数据对象
     */
    private fun deleteCollectedWeb(item: CollectedWebEntity) {
        tagableScope.launch {
            try {
                progressData.postValue(ProgressModel())
                val result = collectRepository.deleteCollectedWeb(item.id.orEmpty())
                if (result.success()) {
                    // 删除成功，从列表移除
                    val ls = websListData.value.toNewList()
                    ls.remove(item)
                    websListData.postValue(ls)
                } else {
                    snackbarData.postValue(result.toError())
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getCollectedWebList")
                snackbarData.postValue(throwable.snackbarMsg)
            } finally {
                progressData.postValue(null)
            }
        }
    }
}

/**
 * Popup 显示数据 Model
 */
data class PopupModel
/**
 * @param view 锚点 View
 * @param callback 点击回调
 */
constructor(
        var view: View,
        var callback: PopupMenu.OnMenuItemClickListener
)