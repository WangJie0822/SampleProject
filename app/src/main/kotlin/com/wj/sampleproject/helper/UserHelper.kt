package com.wj.sampleproject.helper

import com.tencent.mmkv.MMKV
import com.wj.sampleproject.constants.SP_KEY_USER_INFO
import com.wj.sampleproject.entity.UserInfoEntity
import com.wj.sampleproject.ext.toJsonString
import com.wj.sampleproject.ext.toTypeEntity

/**
 * 用户相关帮助类
 *
 * - 创建时间：2019/10/14
 *
 * @author 王杰
 */
object UserHelper {
    
    /** 用户数据对象 */
    var userInfo: UserInfoEntity? = null
        get() {
            if (null == field) {
                field = MMKV.defaultMMKV().decodeString(SP_KEY_USER_INFO, "").toTypeEntity<UserInfoEntity>()
            }
            return field
        }
        set(value) {
            field = value
            MMKV.defaultMMKV().encode(SP_KEY_USER_INFO, value.toJsonString())
        }
}