package com.wj.sampleproject.entity

import kotlinx.serialization.Serializable

/**
 * 积分信息
 *
 * @param coinCount 积分数
 * @param level 等级
 * @param rank 排名
 * @param userId 用户 id
 * @param username 用户名
 *
 * - 创建时间：2021/1/15
 *
 * @author 王杰
 */
@Serializable
data class CoinEntity(
        val coinCount: String? = "",
        val level: String? = "",
        val rank: String? = "",
        val userId: String? = "",
        val username: String? = ""
)