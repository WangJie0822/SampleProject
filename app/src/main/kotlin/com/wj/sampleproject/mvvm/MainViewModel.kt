package com.wj.sampleproject.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.wj.sampleproject.base.ListEntity
import com.wj.sampleproject.base.mvvm.BaseViewModel
import kotlin.random.Random

/**
 *  ViewModel
 */
class MainViewModel : BaseViewModel() {

    val listData = MutableLiveData<ListEntity<String>>()

    val clickItem = MutableLiveData<String>()

    var pageNum = cn.wj.android.databinding.adapter.COMMON_PAGE_START

    /** 刷新 */
    val onRefreshClick: () -> Unit = {
        pageNum = cn.wj.android.databinding.adapter.COMMON_PAGE_START
        getData()
    }

    /** 清空 */
    val onClearClick: () -> Unit = {
        pageNum = cn.wj.android.databinding.adapter.COMMON_PAGE_START
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

    override fun onCreate(source: LifecycleOwner) {
        super.onCreate(source)

        onRefreshClick.invoke()
    }

    private fun getData() {
        val ls = arrayListOf<String>()
        for (i in 0 until cn.wj.android.databinding.adapter.COMMON_PAGE_SIZE) {
            ls.add("item: $i --->${Random.nextInt(200)}")
        }
        listData.postValue(ListEntity(ls, pageNum == cn.wj.android.databinding.adapter.COMMON_PAGE_START))
    }

}
