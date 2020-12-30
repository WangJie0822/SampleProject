package com.wj.sampleproject.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wj.sampleproject.databinding.SmartRefreshState
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.ArticleListEntity
import com.wj.sampleproject.net.NetResult

/**
 * 分页文章列表逻辑接口
 * > 包含 item 点击跳转逻辑、收藏、取消收藏逻辑，分页加载
 *
 * - 创建时间：2020/12/29
 *
 * @author 王杰
 */
interface ArticleListPagingInterface : ArticleListInterface {

    /** 页码 */
    val pageNumber: MutableLiveData<Int>

    /** 文章列表数据 */
    val articleListData: LiveData<ArrayList<ArticleEntity>>

    /** 刷新状态 */
    val refreshing: MutableLiveData<SmartRefreshState>

    /** 加载更多状态 */
    val loadMore: MutableLiveData<SmartRefreshState>

    /** 刷新回调 */
    val onRefresh: () -> Unit

    /** 加载更多回调 */
    val onLoadMore: () -> Unit

    /** 根据页码 [Int] 获取文章列表数据 */
    var getArticleList: suspend (Int) -> NetResult<ArticleListEntity>


}
