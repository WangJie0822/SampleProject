package com.wj.sampleproject.entity

import androidx.databinding.ObservableBoolean
import cn.wj.android.common.ext.isNotNullAndBlank
import cn.wj.android.common.ext.isNotNullAndEmpty
import cn.wj.android.common.ext.orFalse
import com.wj.sampleproject.constants.STR_TRUE

/**
 * 首页文章列表数据实体类
 *
 * @param curPage 当前页码
 * @param offset 已显示数量
 * @param pageCount 总页数
 * @param size 当前页数据数
 * @param total 总数据量
 * @param over 是否结束
 * @param datas 文章数据W
 */
data class ArticleListEntity(
        val curPage: String? = "",
        val offset: String? = "",
        val pageCount: String? = "",
        val size: String? = "",
        val total: String? = "",
        val over: String? = "",
        val datas: ArrayList<ArticleEntity>? = arrayListOf()
)

/**
 * 文章对象数据实体类
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
 * @param originId ？
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
data class ArticleEntity(
        val apkLink: String? = "",
        val author: String? = "",
        val chapterId: String? = "",
        val chapterName: String? = "",
        val collect: String? = "",
        val courseId: String? = "",
        val desc: String? = "",
        val envelopePic: String? = "",
        val fresh: String? = "",
        val id: String? = "",
        val link: String? = "",
        val niceDate: String? = "",
        val origin: String? = "",
        val originId: String? = "",
        val prefix: String? = "",
        val projectLink: String? = "",
        val publishTime: String? = "",
        val superChapterId: String? = "",
        val superChapterName: String? = "",
        val tags: ArrayList<ArticleTagEntity>? = arrayListOf(),
        val title: String? = "",
        val type: String? = "",
        val userId: String? = "",
        val visible: String? = "",
        val zan: String? = "",
        val top: String? = ""
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
    val collected: ObservableBoolean = ObservableBoolean(false)
    
    /** 标记 - 是否显示封面 */
    val showEnvelope: Boolean
        get() = envelopePic.isNotNullAndBlank()
}

/**
 * 文章标签数据实体类
 *
 * @param name 标签名
 * @param url 标签地址
 */
data class ArticleTagEntity(
        val name: String? = "",
        val url: String? = ""
)
