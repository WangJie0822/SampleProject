package com.wj.sampleproject.helper

import androidx.lifecycle.MutableLiveData
import com.tencent.mmkv.MMKV
import com.wj.sampleproject.constants.SP_KEY_USER_INFO
import com.wj.sampleproject.entity.UserInfoEntity
import com.wj.sampleproject.ext.toJsonString
import com.wj.sampleproject.ext.toTypeEntity

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
        value = MMKV.defaultMMKV().decodeString(SP_KEY_USER_INFO, "").toTypeEntity<UserInfoEntity>()
        firstLoad = false
    }

    override fun setValue(value: UserInfoEntity?) {
        super.setValue(value)
        MMKV.defaultMMKV().encode(SP_KEY_USER_INFO, value.toJsonString())
    }
}