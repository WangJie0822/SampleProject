package com.wj.sampleproject.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import cn.wj.android.base.log.InternalLog
import cn.wj.android.logger.AndroidLogAdapter
import cn.wj.android.logger.Logger
import cn.wj.android.logger.PrettyFormatStrategy
import com.jeremyliao.liveeventbus.LiveEventBus
import com.wj.sampleproject.BuildConfig
import com.wj.sampleproject.di.adapterModule
import com.wj.sampleproject.di.netModule
import com.wj.sampleproject.di.repositoryModule
import com.wj.sampleproject.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * 应用全局对象
 */
class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        // Dex 分包
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        // 初始化 Koin
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(listOf(netModule, viewModelModule, repositoryModule, adapterModule))
        }

        // 初始化LiveDataBus
        LiveEventBus
                .config()
                .lifecycleObserverAlwaysActive(true)
                .autoClear(false)

        // 初始化 Logger 日志打印
        val strategy = PrettyFormatStrategy.newBuilder()
                .tag("SAMPLE")
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(strategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })

        // base库输出日志
        InternalLog.logEnable(BuildConfig.DEBUG)
    }
}