package com.wj.sampleproject.repository

import cn.wj.android.common.ext.orFalse
import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * 项目相关数据仓库
 */
class ProjectRepository : KoinComponent {

    /** 网络请求服务 */
    private val mWebService: WebService by inject()

    /**
     * 获取新项目分类列表
     */
    suspend fun getProjectCategory() = withContext(Dispatchers.IO) {
        mWebService.getProjectCategory()
    }

    /**
     * 获取项目列表
     *
     * @param categoryId 分类 id
     * @param pageNum 页码
     */
    suspend fun getProjectList(categoryId: String, pageNum: Int) = withContext(Dispatchers.IO) {
        // 获取项目列表
        val result = mWebService.getProjectList(pageNum, categoryId)
        // 处理收藏状态
        result.data?.datas?.forEach { it.collected.set(it.collect?.toBoolean().orFalse()) }
        result
    }
}