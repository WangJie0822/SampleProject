package com.wj.sampleproject.koin

import cn.wj.android.logger.Logger
import com.wj.android.okhttp.InterceptorLogger
import com.wj.android.okhttp.ParamsInterceptor
import com.wj.sampleproject.mvvm.MainViewModel
import com.wj.sampleproject.net.UrlDefinition
import com.wj.sampleproject.net.WebService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
                        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                            override fun log(message: String) {
                                Logger.t("NET").i(message)
                            }
                        })
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
}