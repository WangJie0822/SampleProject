package com.wj.sampleproject.viewmodel

import android.view.MenuItem
import android.view.MotionEvent
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.common.ext.condition
import cn.wj.common.ext.orEmpty
import com.orhanobut.logger.Logger
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.constants.MAIN_BANNER_TRANSFORM_INTERVAL_MS
import com.wj.sampleproject.entity.BannerEntity
import com.wj.sampleproject.ext.defaultFaildBlock
import com.wj.sampleproject.ext.judge
import com.wj.sampleproject.interfaces.ArticleListPagingInterface
import com.wj.sampleproject.interfaces.impl.ArticleListPagingInterfaceImpl
import com.wj.sampleproject.repository.ArticleRepository
import com.wj.sampleproject.tools.toSnackbarModel
import kotlinx.coroutines.*

/**
 * 主界面 - 首页 ViewModel，注入[repository] 获取相关数据
 * > 文章列表实现 [ArticleListPagingInterface] 接口，委托给 [ArticleListPagingInterfaceImpl] 实现
 */
class HomepageViewModel(
        private val repository: ArticleRepository
) : BaseViewModel(),
        ArticleListPagingInterface by ArticleListPagingInterfaceImpl(repository) {

    init {
        viewModel = this
        getArticleList = { pageNum ->
            repository.getHomepageArticleList(pageNum)
        }
    }

    /** Banner 列表数据 */
    val bannerData: MutableLiveData<ArrayList<BannerEntity>> = MutableLiveData()

    /** 跳转搜索数据 */
    val jumpSearchData: MutableLiveData<Int> = MutableLiveData()

    /** Banner 轮播 job */
    private var carouselJob: Job? = null

    /** 菜单列表点击 */
    val onMenuItemClick: (MenuItem) -> Boolean = {
        if (it.itemId == R.id.menu_search) {
            jumpSearchData.value = 0
        }
        false
    }

    /** 标题折叠监听 */
    val onOffsetChanged: (Int, Int) -> Unit = { offset, _ ->
        if (offset == 0) {
            // 完全展开
            startCarousel()
        } else {
            stopCarousel()
        }
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

    /** Banner 列表条目点击 */
    val onBannerItemClick: (BannerEntity) -> Unit = { item ->
        // 跳转 WebView 打开
        jumpWebViewData.value = WebViewActivity.ActionModel(item.id.orEmpty(), item.title.orEmpty(), item.url.orEmpty())
    }

    /** 获取首页 Banner 列表 */
    fun getHomepageBannerList() {
        viewModelScope.launch {
            try {
                // 获取 Banner 数据
                repository.getHomepageBannerList()
                        .judge(onSuccess = {
                            // 请求成功
                            bannerData.value = data.orEmpty()
                        }, onFailed = defaultFaildBlock)
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "NET_ERROR")
                snackbarData.value = throwable.toSnackbarModel()
            }
        }
    }

    /** 开启 Banner 轮播 */
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

    /** 关闭 Banner 轮播 */
    fun stopCarousel() {
        if (carouselJob != null) {
            if (carouselJob?.isActive.condition) {
                carouselJob?.cancel()
            }
            carouselJob = null
        }
    }
}