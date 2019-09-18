package com.wj.sampleproject.entity

import cn.wj.android.base.ext.isNotNullAndBlank
import cn.wj.android.base.ext.isNotNullAndEmpty
import cn.wj.android.base.ext.orFalse
import com.wj.sampleproject.constants.STR_TRUE

/**
 * 首页文章列表数据实体类
 */
data class ArticleListEntity
/**
 * 构造方法
 *
 * @param curPage 当前页码
 * @param offset 已显示数量
 * @param pageCount 总页数
 * @param size 当前页数据数
 * @param total 总数据量
 * @param over 是否结束
 * @param datas 文章数据
 */
constructor(
        var curPage: String? = "",
        var offset: String? = "",
        var pageCount: String? = "",
        var size: String? = "",
        var total: String? = "",
        var over: String? = "",
        var datas: ArrayList<ArticleEntity>? = arrayListOf()
)

/**
 * 文章对象数据实体类
 */
data class ArticleEntity
/**
 * 构造方法
 *
 * @param apkLink APK 下载链接
 * @param author 作者
 * @param chapterId 二级分类 id
 * @param chapterName 二级分类名称
 * @param collect 是否收藏
 * @param courseId ？
 * @param desc 描述
 * @param envelopePic 文章里的图片地址
 * @param fresh 是否是新文章
 * @param id 文章 id
 * @param link 文章跳转链接
 * @param niceDate 显示时间
 * @param origin ？
 * @param prefix ？
 * @param projectLink ？
 * @param publishTime 发布时间
 * @param superChapterId 一级分类 id
 * @param superChapterName 一级分类名称
 * @param tags 标签列表
 * @param title 标题
 * @param type ？
 * @param userId ？
 * @param visible ？
 * @param zan 赞 数量
 * @param top 自设属性 是否置顶
 */
constructor(
        var apkLink: String? = "",
        var author: String? = "",
        var chapterId: String? = "",
        var chapterName: String? = "",
        var collect: String? = "",
        var courseId: String? = "",
        var desc: String? = "",
        var envelopePic: String? = "",
        var fresh: String? = "",
        var id: String? = "",
        var link: String? = "",
        var niceDate: String? = "",
        var origin: String? = "",
        var prefix: String? = "",
        var projectLink: String? = "",
        var publishTime: String? = "",
        var superChapterId: String? = "",
        var superChapterName: String? = "",
        var tags: ArrayList<ArticleTagEntity>? = arrayListOf(),
        var title: String? = "",
        var type: String? = "",
        var userId: String? = "",
        var visible: String? = "",
        var zan: String? = "",
        var top: String? = ""
) {
    /** 标记 - 是否置顶 */
    val isTop: Boolean
        get() = top == STR_TRUE

    /** 标记 - 是否最新 */
    val isNew: Boolean
        get() = fresh?.toBoolean().orFalse()

    /** 标记 - 是否显示标签 */
    val showTags: Boolean
        get() = tags.isNotNullAndEmpty()

    /** 标记 - 是否收藏 */
    val collected: Boolean
        get() = collect?.toBoolean().orFalse()

    /** 标记 - 是否显示封面 */
    val showEnvelope: Boolean
        get() = envelopePic.isNotNullAndBlank()
}

/**
 * 文章标签数据实体类
 */
data class ArticleTagEntity
/**
 * 构造方法
 *
 * @param name 标签名
 * @param url 标签地址
 */
constructor(
        var name: String? = "",
        var url: String? = ""
)
