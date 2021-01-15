package com.wj.sampleproject.entity

import kotlinx.serialization.Serializable

/**
 * 积分记录
 *
 * @param coinCount 积分变动值
 * @param date 时间，[Long] 类型
 * @param reason 积分变动原因
 * @param desc 积分变动描述
 *
 * - 创建时间：2021/1/15
 *
 * @author 王杰
 */
@Serializable
data class CoinRecordEntity(
        val coinCount: String? = "",
        val date: String? = "",
        val reason: String? = "",
        val desc: String? = ""
)
