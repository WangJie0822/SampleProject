package com.wj.sampleproject.ext

import com.wj.sampleproject.constants.REQUEST_TIMEOUT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * 协程相关
 *
 * - 创建时间：2020/12/30
 *
 * @author 王杰
 */

/**
 * 在 [Dispatchers.IO] 线程执行 [block] 进行网络请求，超时时间为 [REQUEST_TIMEOUT]
 */
suspend fun <T> netRequest(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.IO) {
        withTimeout(REQUEST_TIMEOUT) {
            block.invoke(this)
        }
    }
}
