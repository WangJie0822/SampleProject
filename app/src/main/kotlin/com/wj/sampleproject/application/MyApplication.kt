package com.wj.sampleproject.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import cn.wj.android.base.utils.AppManager
import com.wj.sampleproject.koin.adapterModule
import com.wj.sampleproject.koin.netModule
import com.wj.sampleproject.koin.repositoryModule
import com.wj.sampleproject.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * 应用全局对象
 */
class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        // 注册应用管理
        AppManager.register(this)

        // 初始化 Koin
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(arrayListOf(netModule, viewModelModule, repositoryModule, adapterModule))
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        // Dex 分包
        MultiDex.install(this)
    }
}