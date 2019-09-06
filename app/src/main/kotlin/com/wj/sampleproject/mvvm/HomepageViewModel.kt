package com.wj.sampleproject.mvvm

import android.view.MotionEvent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.base.databinding.BindingField
import cn.wj.android.base.ext.condition
import cn.wj.android.base.ext.isNotNullAndEmpty
import cn.wj.android.base.ext.orEmpty
import cn.wj.android.base.ext.orFalse
import cn.wj.android.base.tools.toast
import cn.wj.android.logger.Logger
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.BannerEntity
import com.wj.sampleproject.net.RefreshList
import com.wj.sampleproject.repository.HomepageRepository
import kotlinx.coroutines.*

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

    /** 轮播 */
    private var carouselJob: Job? = null

    /** Banner 数量 */
    var bannerCount = 0

    /** 标记 - 是否显示 Banner */
    val showBanner: BindingField<Boolean> = BindingField(false)

    val onBannerTouch: (MotionEvent) -> Boolean = { event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // 按下、抬起，取消轮播
                stopCarouse()
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                // 抬起、取消，开启轮播
                startCarouse()
            }
        }
        false
    }

    /** Banner 下标 */
    val bannerCurrent: BindingField<Int> = BindingField()

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
    val onAritcleItemClick: (ArticleEntity) -> Unit = { _ ->
        // TODO
    }

    /** Banner 列表数据 */
    val bannerListData = MutableLiveData<ArrayList<BannerEntity>>()
    /** 文章列表数据 */
    val articleListData = MutableLiveData<RefreshList<ArticleEntity>>()

    override fun onCreate(source: LifecycleOwner) {
        super.onCreate(source)

        // 获取 Banner 数据
        getHomepageBannerList()
        // 刷新数据
        refreshing.set(true)
        onRefresh.invoke()
    }

    override fun onStart(source: LifecycleOwner) {
        super.onStart(source)
        startCarouse()
    }

    override fun onStop(source: LifecycleOwner) {
        super.onStop(source)
        stopCarouse()
    }

    /**
     * 获取首页 Banner 列表
     */
    private fun getHomepageBannerList() {
        viewModelScope.launch {
            try {
                // 获取 Banner 列表
                val result = repository.getHomepageBannerList()
                if (result.success()) {
                    // 获取成功
                    showBanner.set(result.data.isNotNullAndEmpty())
                    if (showBanner.get().condition) {
                        // 刷新 Banner 列表
                        bannerListData.postValue(result.data.orEmpty())
                        // 开启轮播
                        startCarouse()
                    }
                }
            } catch (e: Exception) {
                // TODO
                toast(e.toString())
                Logger.e(e)
            }
        }
    }

    /**
     * 开启轮播
     */
    private fun startCarouse() {
        stopCarouse()
        carouselJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(3000L)
                if (bannerCount > 0) {
                    val target = ((bannerCurrent.get() ?: 0) + 1) % bannerCount
                    bannerCurrent.set(target)
                }
            }
        }
    }

    /**
     * 关闭轮播
     */
    private fun stopCarouse() {
        if (carouselJob != null) {
            if (carouselJob?.isActive.condition) {
                carouselJob?.cancel()
            }
            carouselJob = null
        }
    }

    /**
     * 获取文章列表
     */
    private fun getHomepageArticleList() {
        viewModelScope.launch {
            // 标记 - 是否到末尾
            var over = false
            try {
                // 获取文章列表
                val result = repository.getHompageArticleList(pageNum)
                if (result.success()) {
                    // 网络请求成功
                    over = result.data?.over?.toBoolean().orFalse()
                    articleListData.postValue(RefreshList(result.data?.datas, refreshing.get()))
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