package com.wj.android.forresult

/**
 * 权限申请返回数据
 *
 * @param allGranted 是否全部授权
 * @param requestCount 申请权限数量
 * @param deniedList 未授权权限列表
 *
 * - 创建时间：2020/4/28
 *
 * @author 王杰
 */
data class PermissionsResultInfo(
        var allGranted: Boolean,
        var requestCount: Int,
        var deniedList: ArrayList<String>
)