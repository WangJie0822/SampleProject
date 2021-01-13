package com.wj.sampleproject.helper

import androidx.lifecycle.MutableLiveData
import com.wj.sampleproject.constants.DATA_CACHE_KEY_COOKIES
import com.wj.sampleproject.constants.DATA_CACHE_KEY_USER_INFO
import com.wj.sampleproject.entity.UserInfoEntity
import com.wj.sampleproject.tools.decodeString
import com.wj.sampleproject.tools.encode
import com.wj.sampleproject.tools.toJsonString
import com.wj.sampleproject.tools.toTypeEntity

/**
 * 用户信息
 *
 * - 创建时间：2019/10/14
 *
 * @author 王杰
 */
object UserInfoData : MutableLiveData<UserInfoEntity>() {

    private var firstLoad = true

    override fun onActive() {
        if (!firstLoad) {
            return
        }
        value = DATA_CACHE_KEY_USER_INFO.decodeString("").toTypeEntity<UserInfoEntity>()
        firstLoad = false
    }

    override fun setValue(value: UserInfoEntity?) {
        super.setValue(value)
        DATA_CACHE_KEY_USER_INFO.encode(value.toJsonString())
        if (null == value) {
            // 退出登录，清空 cookie
            DATA_CACHE_KEY_COOKIES.encode("")
        }
    }
}