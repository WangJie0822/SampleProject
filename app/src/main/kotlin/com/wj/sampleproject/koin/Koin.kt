package com.wj.sampleproject.koin

import cn.wj.android.logger.Logger
import com.wj.android.okhttp.InterceptorLogger
import com.wj.android.okhttp.LogInterceptor
import com.wj.android.okhttp.ParamsInterceptor
import com.wj.sampleproject.BuildConfig
import com.wj.sampleproject.mvvm.Main2ViewModel
import com.wj.sampleproject.mvvm.MainViewModel
import com.wj.sampleproject.net.UrlDefinition
import com.wj.sampleproject.net.WebService
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 网络请求 Module
 */
val netModule: Module = module {

    single<OkHttpClient> {
        //缓存路径
        val logger = object : InterceptorLogger {
            override fun log(msg: String) {
                Logger.t("NET").i(msg)
            }
        }
        OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(
                        LogInterceptor.newBuilder()
                                .level(if (BuildConfig.DEBUG) LogInterceptor.Level.BODY else LogInterceptor.Level.NONE)
                                .logger(logger)
                                .build()
                )
                .addInterceptor(
                        ParamsInterceptor.newBuilder()
                                .addStaticParam("hha", "haa")
                                .addDynamicParam("hehe") { "hehe" }
                                .addStaticHeader("toto", "toto")
                                .addDynamicHeader("dd") { "dd" }
                                .logger(logger)
                                .build()
                )
                .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
                .baseUrl(UrlDefinition.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(get<OkHttpClient>())
                .build()
    }

    single<WebService> {
        get<Retrofit>().create(WebService::class.java)
    }
}

/**
 * 数据仓库 Module
 */
val repositoryModule: Module = module {
}

/**
 * 适配器 Module
 */
val adapterModule: Module = module {
}

/**
 * ViewModel Module
 */
val viewModelModule: Module = module {
    viewModel { MainViewModel() }
    viewModel { Main2ViewModel() }
}