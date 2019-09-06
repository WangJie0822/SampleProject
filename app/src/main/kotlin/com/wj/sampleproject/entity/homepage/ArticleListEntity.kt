package com.wj.sampleproject.entity.homepage

import cn.wj.android.base.ext.isNotNullAndEmpty
import cn.wj.android.base.ext.orFalse
import com.wj.sampleproject.constants.STR_TRUE

/**
 * 首页文章列表数据实体类
 */
data class ArticleListEntity
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
}

/**
 * 文章标签数据实体类
 */
data class ArticleTagEntity
constructor(
        var name: String? = "",
        var url: String? = ""
)
