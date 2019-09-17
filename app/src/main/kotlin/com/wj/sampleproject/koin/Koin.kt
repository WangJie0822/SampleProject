package com.wj.sampleproject.koin

import cn.wj.android.logger.Logger
import com.wj.android.okhttp.InterceptorLogger
import com.wj.android.okhttp.LoggerInterceptor
import com.wj.sampleproject.BuildConfig
import com.wj.sampleproject.adapter.BannerVpAdapter
import com.wj.sampleproject.adapter.HomepageArticleListRvAdapter
import com.wj.sampleproject.adapter.NavigationRvAdapter
import com.wj.sampleproject.adapter.SystemCategoryRvAdapter
import com.wj.sampleproject.mvvm.*
import com.wj.sampleproject.net.UrlDefinition
import com.wj.sampleproject.net.WebService
import com.wj.sampleproject.repository.HomepageRepository
import com.wj.sampleproject.repository.SystemRepository
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
                        LoggerInterceptor(logger, if (BuildConfig.DEBUG) LoggerInterceptor.Level.BODY else LoggerInterceptor.Level.NONE)
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
    single {
        SystemRepository()
    }
}

/**
 * 适配器 Module
 */
val adapterModule: Module = module {
    factory { HomepageArticleListRvAdapter() }
    factory { BannerVpAdapter() }
    factory { SystemCategoryRvAdapter() }
    factory { NavigationRvAdapter() }
}

/**
 * ViewModel Module
 */
val viewModelModule: Module = module {
    viewModel { SplashViewModel() }
    viewModel { MainViewModel() }
    viewModel { HomepageViewModel(get()) }
    viewModel { SystemViewModel() }
    viewModel { SystemCategoryViewModel(get()) }
    viewModel { NavigationViewModel(get()) }
    viewModel { BjnewsViewModel() }
    viewModel { ProjectViewModel() }
    viewModel { MyViewModel() }
    viewModel { SearchViewModel() }
    viewModel { WebViewViewModel() }
}