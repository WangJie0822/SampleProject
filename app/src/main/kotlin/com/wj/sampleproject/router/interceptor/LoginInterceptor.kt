package com.wj.sampleproject.router.interceptor

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter
import com.wj.sampleproject.helper.UserInfoData
import com.wj.sampleproject.router.*

/**
 * 登录状态拦截器
 *
 * - 创建时间：2021/1/19
 *
 * @author 王杰
 */
@Interceptor(priority = 8, name = "登录状态拦截器")
class LoginInterceptor : IInterceptor {

    override fun init(context: Context) {
    }

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        if (needIntercept(postcard.path)) {
            // 需要登录验证
            if (null == UserInfoData.value) {
                // 未登录，转发到登录页
                ARouter.getInstance().build(ROUTER_PATH_LOGIN).navigation()
                callback.onInterrupt(Exception("must login first!"))
                return
            }
        }
        // 处理完成，交还控制
        callback.onContinue(postcard)
    }

    /** 根据 [path] 判断是否需要拦截 */
    private fun needIntercept(path: String): Boolean {
        return path in arrayOf(
                ROUTER_PATH_SETTING, // 设置
                ROUTER_PATH_COIN, // 积分
                ROUTER_PATH_COLLECTED_WEB, // 收藏网站
                ROUTER_PATH_COLLECTION // 我的收藏
        )
    }
}