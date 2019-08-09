package com.wj.sampleproject.mvvm

import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.constants.COMMON_PAGE_SIZE
import cn.wj.android.base.constants.COMMON_PAGE_START
import com.wj.sampleproject.base.ListEntity
import com.wj.sampleproject.base.mvvm.BaseViewModel
import kotlin.random.Random

/**
 *  ViewModel
 */
class MainViewModel : BaseViewModel() {

    val listData = MutableLiveData<ListEntity<String>>()

    val clickItem = MutableLiveData<String>()

    var pageNum = COMMON_PAGE_START

    /** 刷新 */
    val onRefreshClick: () -> Unit = {
        pageNum = COMMON_PAGE_START
        getData()
    }

    /** 清空 */
    val onClearClick: () -> Unit = {
        pageNum = COMMON_PAGE_START
        listData.postValue(ListEntity(arrayListOf(), true))
    }

    /** 加载更多 */
    val onLoadMoreClick: () -> Unit = {
        pageNum++
        getData()
    }

    /** 条目点击事件 */
    val onItemClick: (String) -> Unit = {
        clickItem.postValue(it)
    }

    override fun onCreate() {
        super.onCreate()

        onRefreshClick.invoke()
    }

    private fun getData() {
        val ls = arrayListOf<String>()
        for (i in 0 until COMMON_PAGE_SIZE) {
            ls.add("item: $i --->${Random.nextInt(200)}")
        }
        listData.postValue(ListEntity(ls, pageNum == COMMON_PAGE_START))
    }

}
