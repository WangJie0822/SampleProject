package com.wj.sampleproject.repository

import com.wj.sampleproject.net.WebService
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * 搜索相关数据仓库
 */
class SearchRepository : KoinComponent {

    /** 网络请求对象 */
    private val mWebService: WebService by inject()

}