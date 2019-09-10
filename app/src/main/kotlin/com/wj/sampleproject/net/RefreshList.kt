package com.wj.sampleproject.net

/**
 * 可刷新列表数据封装实体类
 */
data class RefreshList<T>
/**
 * 构造方法
 *
 * @param list 数据列表
 * @param refresh 是否刷新
 */
constructor(
        var list: ArrayList<T>? = arrayListOf(),
        var refresh: Boolean? = false
)