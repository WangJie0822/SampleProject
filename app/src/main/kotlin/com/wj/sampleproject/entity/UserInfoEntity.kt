package com.wj.sampleproject.entity

/**
 * 用户信息数据实体类
 *
 * * 创建时间：2019/10/14
 *
 * @author 王杰
 */
data class UserInfoEntity
/**
 * 主构造函数
 *
 * @param admin 是否是管理员
 * @param chapterTops ？
 * @param collectIds ？
 * @param email 邮箱
 * @param icon 用户头像地址
 * @param id 用户 id
 * @param nickname 用户昵称
 * @param password 用户密码
 * @param publicName 公共名称
 * @param token 用户 token
 * @param type ？
 * @param username 用户名
 */
constructor(
        var admin: String? = "",
        var chapterTops: ArrayList<Any>? = arrayListOf(),
        var collectIds: ArrayList<Any>? = arrayListOf(),
        var email: String? = "",
        var icon: String? = "",
        var id: String? = "",
        var nickname: String? = "",
        var password: String? = "",
        var publicName: String? = "",
        var token: String? = "",
        var type: String? = "",
        var username: String? = ""
)

