package com.wj.sampleproject.mvvm

import android.view.MenuItem
import android.view.MotionEvent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.base.databinding.BindingField
import cn.wj.android.base.ext.condition
import cn.wj.android.base.ext.orEmpty
import cn.wj.android.base.utils.AppManager
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.SearchActivity
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.base.SnackbarEntity
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.constants.MAIN_BANNER_TRANSFORM_INTERVAL_MS
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.BannerEntity
import com.wj.sampleproject.ext.showMsg
import com.wj.sampleproject.net.RefreshList
import com.wj.sampleproject.repository.HomepageRepository
import kotlinx.coroutines.*

/**
 * 主界面 - 首页 ViewModel
 */
class HomepageViewModel
/**
 * @param repository 主页数据仓库
 */
constructor(private val repository: HomepageRepository)
    : BaseViewModel() {

    override fun firstLoad() {
        // 获取 Banner 数据
        getHomepageBannerList()
        // 刷新文章列表
        refreshing.set(true)
    }

    override fun onStart(source: LifecycleOwner) {
        super.onStart(source)
        // 开启轮播
        startCarousel()
    }

    override fun onStop(source: LifecycleOwner) {
        super.onStop(source)
        // 关闭轮播
        stopCarousel()
    }

    /** 菜单列表点击 */
    val onMenuItemClick: (MenuItem) -> Boolean = {
        if (it.itemId == R.id.menu_search) {
            SearchActivity.actionStart(AppManager.getContext())
        }
        false
    }

    /** Banner 下标 */
    val bannerCurrent: BindingField<Int> = BindingField()

    /** Banner 预加载页数 */
    val bannerLimit: BindingField<Int> = BindingField()

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

    /** 标记 - 是否正在刷新 */
    val refreshing: BindingField<Boolean> = BindingField(true)

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

    /** Banner 列表数据 */
    val bannerData = MutableLiveData<ArrayList<BannerEntity>>()
    /** 文章列表数据 */
    val articleListData = MutableLiveData<RefreshList<ArticleEntity>>()

    /** Banner 轮播 job */
    private var carouselJob: Job? = null

    /** 文章列表条目点击 */
    val onArticleItemClick: (ArticleEntity) -> Unit = { item ->
        // 跳转 WebView 打开
        WebViewActivity.actionStart(AppManager.getContext(), item.title.orEmpty(), item.link.orEmpty())
    }

    /**
     * 获取首页 Banner 列表
     */
    private fun getHomepageBannerList() {
        viewModelScope.launch {
            try {
                // 获取 Banner 数据
                val result = repository.getHomepageBannerList()
                if (result.success()) {
                    // 请求成功
                    bannerData.postValue(result.data.orEmpty())
                } else {
                    snackbarData.postValue(SnackbarEntity(result.errorMsg))
                }
            } catch (throwable: Throwable) {
                snackbarData.postValue(SnackbarEntity(throwable.showMsg))
            }
        }
    }

    /**
     * 开启 Banner 轮播
     */
    private fun startCarousel() {
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
                val current = bannerCurrent.get() ?: 0
                bannerCurrent.set((current + 1) % bannerCount)
            }
        }
    }

    /**
     * 关闭 Banner 轮播
     */
    private fun stopCarousel() {
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