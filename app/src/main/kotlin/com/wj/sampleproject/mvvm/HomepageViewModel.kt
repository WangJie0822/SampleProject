package com.wj.sampleproject.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.base.databinding.BindingField
import cn.wj.android.base.ext.orFalse
import cn.wj.android.base.tools.toast
import cn.wj.android.logger.Logger
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.entity.homepage.ArticleEntity
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

    /** 页码 */
    private var pageNum: Int = NET_PAGE_START

    /** 刷新状态 */
    val refreshing: BindingField<Boolean> = BindingField(false)

    /** 刷新列表 */
    val onRefresh: () -> Unit = {
        loadMoreEnable.set(true)
        pageNum = NET_PAGE_START
        getHomepageArticleList()
    }

    /** 加载更多状态 */
    val loadMore: BindingField<Boolean> = BindingField(false)

    /** 加载更多 */
    val onLoadMore: () -> Unit = {
        pageNum++
        getHomepageArticleList()
    }

    /** 是否允许加载更多 */
    val loadMoreEnable: BindingField<Boolean> = BindingField(true)

    /** 搜索点击 */
    val onSearchClick: () -> Unit = {
        // TODO
    }

    /** 文章列表条目点击 */
    val onAritcleItemClick: (ArticleEntity) -> Unit = { item ->
        // TODO
    }

    /** 列表数据 */
    val listData = MutableLiveData<RefreshList<ArticleEntity>>()

    override fun onCreate(source: LifecycleOwner) {
        super.onCreate(source)

        // 刷新数据
        onRefresh.invoke()
    }

    /**
     * 获取文章列表
     */
    private fun getHomepageArticleList() {
        viewModelScope.launch {
            // 标记 - 是否到末尾
            var over = false
            try {
                // 获取网络数据
                val result = repository.getHompageArticleList(pageNum)
                if (result.success()) {
                    // 网络请求成功
                    over = result.data?.over?.toBoolean().orFalse()
                    listData.postValue(RefreshList(result.data?.datas, refreshing.get()))
                }
            } catch (e: Exception) {
                // TODO
                toast(e.toString())
                Logger.e(e)
            } finally {
                refreshing.set(false)
                loadMore.set(false)
                loadMoreEnable.set(!over)
            }
        }
    }
}