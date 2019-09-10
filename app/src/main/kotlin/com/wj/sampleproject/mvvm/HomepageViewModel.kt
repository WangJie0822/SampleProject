package com.wj.sampleproject.mvvm

import android.view.MenuItem
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.base.databinding.BindingField
import cn.wj.android.base.utils.AppManager
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.SearchActivity
import com.wj.sampleproject.base.SnackbarEntity
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.ext.showMsg
import com.wj.sampleproject.net.RefreshList
import com.wj.sampleproject.repository.HomepageRepository
import kotlinx.coroutines.launch

/**
 * 主界面 - 首页 ViewModel
 */
class HomepageViewModel
/**
 * 构造方法
 *
 * @param repository 主页数据仓库
 */
constructor(private val repository: HomepageRepository)
    : BaseViewModel() {

    override fun onCreate(source: LifecycleOwner) {
        super.onCreate(source)

        // 刷新文章列表
        refreshing.set(true)
    }

    /** 菜单列表点击 */
    val onMenuItemClick: (MenuItem) -> Boolean = {
        if (it.itemId == R.id.menu_search) {
            SearchActivity.actionStart(AppManager.getContext())
        }
        false
    }

    /** 标记 - 是否正在刷新 */
    val refreshing: BindingField<Boolean> = BindingField(false)

    /** 刷新回调 */
    val onRefresh: () -> Unit = {
        pageNum = NET_PAGE_START
        getHomepageArticleList()
    }

    /** 标记 - 是否正在加载更多 */
    val loadMore: BindingField<Boolean> = BindingField(false)

    /** 加载更多回调 */
    val onLoadMore: () -> Unit = {
        pageNum++
        getHomepageArticleList()
    }

    /** 页码 */
    private var pageNum = NET_PAGE_START

    /** 文章列表数据 */
    val articleListData = MutableLiveData<RefreshList<ArticleEntity>>()

    /**
     * 获取首页文章列表
     */
    private fun getHomepageArticleList() {
        viewModelScope.launch {
            try {
                // 获取文章列表数据
                val result = repository.getHompageArticleList(pageNum)
                if (result.success()) {
                    // 请求成功
                    articleListData.postValue(RefreshList(result.data?.datas, refreshing.get()))
                } else {
                    snackbarData.postValue(SnackbarEntity(result.errorMsg))
                }
            } catch (throwable: Throwable) {
                snackbarData.postValue(SnackbarEntity(throwable.showMsg))
            } finally {
                refreshing.set(false)
                loadMore.set(false)
            }
        }
    }

}