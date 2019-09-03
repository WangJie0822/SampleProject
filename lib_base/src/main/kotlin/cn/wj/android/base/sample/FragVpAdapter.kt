package cn.wj.android.base.sample

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import cn.wj.android.base.adapter.FragVpAdapter

/**
 * [cn.wj.android.base.adapter.FragVpAdapter] 示例
 *
 * @author 王杰
 * @date 2019/8/5
 */
class FragVpAdapter {

    /**
     * 示例方法
     */
    fun sample(activity: AppCompatActivity, fragmentList: ArrayList<Fragment>) {
        // 新建 Builder
        // 设置 FragmentManager
        // 设置 Fragment 集合
        // 设置 行为
        // 获取 Fragment 对应标题
        // 构建 FragVpAdapter 对象
        FragVpAdapter.newBuilder()
                .manager(activity.supportFragmentManager)
                .frags(fragmentList)
                .behavior(FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
                .pageTitle { fragment, _ -> fragment.tag }
                .build()
    }
}