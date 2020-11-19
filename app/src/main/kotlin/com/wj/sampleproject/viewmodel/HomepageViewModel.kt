package com.wj.sampleproject.viewmodel

import android.view.MenuItem
import android.view.MotionEvent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.common.ext.condition
import cn.wj.android.common.ext.orEmpty
import cn.wj.android.common.ext.toNewList
import cn.wj.android.logger.Logger
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListViewModel
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.constants.MAIN_BANNER_TRANSFORM_INTERVAL_MS
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.BannerEntity
import com.wj.sampleproject.ext.showMsg
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.repository.CollectRepository
import com.wj.sampleproject.repository.HomepageRepository
import kotlinx.coroutines.*

/**
 * 主界面 - 首页 ViewModel
 *
 * @param homepageRepository 主页数据仓库
 * @param collectRepository 收藏相关数据仓库
 */
class HomepageViewModel(
        private val homepageRepository: HomepageRepository,
        private val collectRepository: CollectRepository
) : BaseViewModel(),
        ArticleListViewModel {

    /** 页码 */
    private var pageNum = NET_PAGE_START

    /** Banner 列表数据 */
    val bannerData = MutableLiveData<ArrayList<BannerEntity>>()

    /** 文章列表数据 */
    val articleListData = MutableLiveData<ArrayList<ArticleEntity>>()

    /** 跳转 WebView 数据 */
    val jumpWebViewData = MutableLiveData<WebViewActivity.ActionModel>()

    /** 跳转搜索数据 */
    val jumpSearchData = MutableLiveData<Int>()

    /** Banner 轮播 job */
    private var carouselJob: Job? = null

    /** 菜单列表点击 */
    val onMenuItemClick: (MenuItem) -> Boolean = {
        if (it.itemId == R.id.menu_search) {
            jumpSearchData.value = 0
        }
        false
    }

    /** Banner 下标 */
    val bannerCurrent: ObservableInt = ObservableInt()

    /** Banner 预加载页数 */
    val bannerLimit: ObservableInt = ObservableInt()

    /** Banner 数量 */
    var bannerCount: Int = 0
        set(value) {
            field = value
            // 更新 Banner 预加载页数
            bannerLimit.set(field * 2)
            // 开启轮询
            startCarousel()
        }

    /** Banner 触摸事件 */
    val onBannerTouch: (MotionEvent) -> Boolean = { event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // 按下、移动，取消轮播
                stopCarousel()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 抬起、取消，开启轮播
                startCarousel()
            }
        }
        false
    }

    /** 文章列表条目点击 */
    val onBannerItemClick: (BannerEntity) -> Unit = { item ->
        // 跳转 WebView 打开
        jumpWebViewData.value = WebViewActivity.ActionModel(item.id.orEmpty(), item.title.orEmpty(), item.url.orEmpty())
    }

    /** 标记 - 是否正在刷新 */
    val refreshing: ObservableBoolean = ObservableBoolean(false)

    /** 刷新回调 */
    val onRefresh: () -> Unit = {
        pageNum = NET_PAGE_START
        noMore.set(false)
        getHomepageArticleList()
    }

    /** 标记 - 是否正在加载更多 */
    val loadMore: ObservableBoolean = ObservableBoolean(false)

    /** 加载更多回调 */
    val onLoadMore: () -> Unit = {
        pageNum++
        getHomepageArticleList()
    }

    /** 标记 - 是否没有更多 */
    val noMore: ObservableBoolean = ObservableBoolean(false)

    /** 文章列表条目点击 */
    override val onArticleItemClick: (ArticleEntity) -> Unit = { item ->
        // 跳转 WebView 打开
        jumpWebViewData.value = WebViewActivity.ActionModel(item.id.orEmpty(), item.title.orEmpty(), item.link.orEmpty())
    }

    /** 文章收藏点击 */
    override val onArticleCollectClick: (ArticleEntity) -> Unit = { item ->
        if (item.collected.get().condition) {
            // 已收藏，取消收藏
            item.collected.set(false)
            unCollect(item)
        } else {
            // 未收藏，收藏
            item.collected.set(true)
            collect(item)
        }
    }

    /**
     * 获取首页 Banner 列表
     */
    fun getHomepageBannerList() {
        viewModelScope.launch {
            try {
                // 获取 Banner 数据
                val result = homepageRepository.getHomepageBannerList()
                if (result.success()) {
                    // 请求成功
                    bannerData.value = result.data.orEmpty()
                } else {
                    snackbarData.value = SnackbarModel(result.errorMsg)
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "NET_ERROR")
                snackbarData.value = SnackbarModel(throwable.showMsg)
            }
        }
    }

    /**
     * 开启 Banner 轮播
     */
    fun startCarousel() {
        // 关闭轮播
        stopCarousel()
        if (bannerCount < 2) {
            // Banner 小于2，不轮播
            return
        }
        // 新建协程
        carouselJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                // 延时
                delay(MAIN_BANNER_TRANSFORM_INTERVAL_MS)
                // 切换
                val current = bannerCurrent.get()
                bannerCurrent.set((current + 1) % bannerCount)
            }
        }
    }

    /**
     * 关闭 Banner 轮播
     */
    fun stopCarousel() {
        if (carouselJob != null) {
            if (carouselJob?.isActive.condition) {
                carouselJob?.cancel()
            }
            carouselJob = null
        }
    }

    /**
     * 获取首页文章列表
     */
    private fun getHomepageArticleList() {
        viewModelScope.launch {
            try {
                // 获取文章列表数据
                val result = homepageRepository.getHomepageArticleList(pageNum)
                if (result.success()) {
                    // 请求成功
                    articleListData.value = articleListData.value.toNewList(result.data?.datas, refreshing.get())
                    noMore.set(result.data?.over?.toBoolean().condition)
                } else {
                    snackbarData.value = SnackbarModel(result.errorMsg)
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getHomepageArticleList")
                snackbarData.value = SnackbarModel(throwable.showMsg)
            } finally {
                refreshing.set(false)
                loadMore.set(false)
            }
        }
    }

    /**
     * 收藏
     *
     * @param item 文章对象
     */
    private fun collect(item: ArticleEntity) {
        viewModelScope.launch {
            try {
                // 收藏
                val result = collectRepository.collectArticleInside(item.id.orEmpty())
                if (!result.success()) {
                    // 收藏失败，提示、回滚收藏状态
                    snackbarData.value = SnackbarModel(result.errorMsg)
                    item.collected.set(false)
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "collect")
                // 收藏失败，提示、回滚收藏状态
                snackbarData.value = SnackbarModel(throwable.showMsg)
                item.collected.set(false)
            }
        }
    }

    /**
     * 取消收藏
     *
     * @param item 文章对象
     */
    private fun unCollect(item: ArticleEntity) {
        viewModelScope.launch {
            try {
                // 取消收藏
                val result = collectRepository.unCollectArticleList(item.id.orEmpty())
                if (!result.success()) {
                    // 取消收藏失败，提示、回滚收藏状态
                    snackbarData.value = SnackbarModel(result.errorMsg)
                    item.collected.set(true)
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "unCollect")
                // 取消收藏失败，提示、回滚收藏状态
                snackbarData.value = SnackbarModel(throwable.showMsg)
                item.collected.set(true)
            }
        }
    }
}