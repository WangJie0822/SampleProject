package com.wj.android.forresult

/**
 * 权限申请返回数据
 * > 单个校验使用
 *
 * @param granted 是否全部授权
 * @param permission 申请权限数量
 *
 * - 创建时间：2020/4/28
 *
 * @author 王杰
 */
data class PermissionsResultEachInfo(
        var granted: Boolean,
        var permission: String
)