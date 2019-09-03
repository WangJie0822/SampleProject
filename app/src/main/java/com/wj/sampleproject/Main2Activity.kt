package com.wj.sampleproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import cn.wj.android.logger.Logger
import cn.wj.android.rx.subscribeWithOwner
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivityMain2Binding
import com.wj.sampleproject.mvvm.Main2ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel

class Main2Activity
    : BaseActivity<Main2ViewModel, AppActivityMain2Binding>() {

    companion object {
        /**
         * 界面入口
         *
         * @param context Context 对象
         */
        fun actionStart(context: Context) {
            context.startActivity(Intent(context, Main2Activity::class.java))
        }
    }

    override val mViewModel: Main2ViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_main2)

        mViewModel.clickLiveData.observe(this, Observer {
            Observable.create<String> {
                try {
                    for (i in 0 until 100) {
                        it.onNext("++$i")
                        Thread.sleep(500L)
                    }
                } catch (e: Exception) {
                    it.onError(e)
                }
                it.onComplete()
            }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWithOwner(this, {
                        Logger.e("print ----> $it")
                    }, {
                        Logger.e(it)
                    })

        })
    }
}
