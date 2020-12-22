package com.wj.sampleproject.application

import android.app.Activity
import android.app.ActivityOptions
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import cn.wj.android.base.ext.getDefaultOptions
import cn.wj.android.base.log.InternalLog
import com.jeremyliao.liveeventbus.LiveEventBus
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.wj.sampleproject.BuildConfig
import com.wj.sampleproject.di.adapterModule
import com.wj.sampleproject.di.netModule
import com.wj.sampleproject.di.repositoryModule
import com.wj.sampleproject.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import skin.support.SkinCompatManager
import skin.support.app.SkinAppCompatViewInflater
import skin.support.app.SkinCardViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater
import skin.support.design.app.SkinMaterialViewInflater

/**
 * 应用全局类
 */
@Suppress("unused")
class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        // Dex 分包
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        // 初始化换肤框架
        SkinCompatManager.withoutActivity(this)
                .addInflater(SkinAppCompatViewInflater())
                .addInflater(SkinMaterialViewInflater())
                .addInflater(SkinConstraintViewInflater())
                .addInflater(SkinCardViewInflater())
                .loadSkin()

        // 初始化 Koin
        startKoin {
            androidLogger(Level.DEBUG)
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

        // 配置界面跳转默认配置参数
        getDefaultOptions = { context ->
            if (context is Activity) {
                ActivityOptions.makeSceneTransitionAnimation(context).toBundle()
            } else {
                null
            }
        }
    }
}