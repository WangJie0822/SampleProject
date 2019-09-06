package com.wj.sampleproject.koin

import cn.wj.android.logger.Logger
import com.wj.android.okhttp.InterceptorLogger
import com.wj.android.okhttp.LoggerInterceptor
import com.wj.android.okhttp.ParamsInterceptor
import com.wj.sampleproject.BuildConfig
import com.wj.sampleproject.adapter.HomepageArticleListRvAdapter
import com.wj.sampleproject.mvvm.HomepageViewModel
import com.wj.sampleproject.mvvm.MainViewModel
import com.wj.sampleproject.mvvm.SplashViewModel
import com.wj.sampleproject.net.UrlDefinition
import com.wj.sampleproject.net.WebService
import com.wj.sampleproject.repository.HomepageRepository
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 网络请求 Module
 */
val netModule: Module = module {

    single {
        //缓存路径
        val logger = object : InterceptorLogger {
            override fun log(msg: String) {
                Logger.t("NET").i(msg)
            }
        }
        OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(
                        LoggerInterceptor(object : InterceptorLogger {
                            override fun log(msg: String) {
                                Logger.t("NET").i(msg)
                            }
                        }, if (BuildConfig.DEBUG) LoggerInterceptor.Level.BODY else LoggerInterceptor.Level.NONE)
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
    single {
        HomepageRepository()
    }
}

/**
 * 适配器 Module
 */
val adapterModule: Module = module {
    factory { HomepageArticleListRvAdapter() }
}

/**
 * ViewModel Module
 */
val viewModelModule: Module = module {
    viewModel { SplashViewModel() }
    viewModel { MainViewModel() }
    viewModel { HomepageViewModel(get()) }
}