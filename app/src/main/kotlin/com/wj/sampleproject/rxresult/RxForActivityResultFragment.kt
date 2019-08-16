package com.wj.sampleproject.rxresult

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * 空 Fragment 处理 startActivityForResult
 */
@Suppress("unused")
class RxForActivityResultFragment : Fragment() {

    /** 界面跳转集合 */
    private val map = hashMapOf<Int, PublishSubject<RxForActivityResultInfo>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
     * 跳转 Activity 并返回 Observable
     *
     * @param requestCode 请求码
     * @param intent [Intent] 对象
     *
     * @return [Observable] 对象，用于处理返回数据
     */
    fun startForResult(requestCode: Int, intent: Intent): Observable<RxForActivityResultInfo> {
        val subject = PublishSubject.create<RxForActivityResultInfo>()
        map[requestCode] = subject
        return subject.doOnSubscribe {
            startActivityForResult(intent, requestCode)
        }
    }

    /**
     * 跳转 Activity 并返回 Observable
     *
     * @param requestCode 请求码
     * @param start 跳转 Activity 方法块
     *
     * @return [Observable] 对象，用于处理返回数据
     */
    fun startForResult(requestCode: Int, start: (Fragment) -> Unit): Observable<RxForActivityResultInfo> {
        val subject = PublishSubject.create<RxForActivityResultInfo>()
        map[requestCode] = subject
        return subject.doOnSubscribe {
            start.invoke(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (map.containsKey(requestCode)) {
            // 跳转返回，发送事件
            val subject = map[requestCode] ?: return
            subject.onNext(RxForActivityResultInfo(requestCode, resultCode, data))
            subject.onComplete()
        }
    }
}