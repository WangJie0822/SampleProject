package com.wj.sampleproject.di

import cn.wj.android.common.ext.orEmpty
import cn.wj.android.logger.Logger
import com.tencent.mmkv.MMKV
import com.wj.android.okhttp.InterceptorLogger
import com.wj.android.okhttp.LoggerInterceptor
import com.wj.sampleproject.BuildConfig
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.adapter.NavigationRvAdapter
import com.wj.sampleproject.adapter.SystemCategoryRvAdapter
import com.wj.sampleproject.constants.SP_KEY_COOKIES
import com.wj.sampleproject.entity.CookieEntity
import com.wj.sampleproject.ext.toJsonString
import com.wj.sampleproject.ext.toTypeEntity
import com.wj.sampleproject.net.UrlDefinition
import com.wj.sampleproject.net.WebService
import com.wj.sampleproject.repository.*
import com.wj.sampleproject.viewmodel.*
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** 网络请求 Module */
val netModule: Module = module {

    single {
        //缓存路径
        val logger = object : InterceptorLogger {
            override fun invoke(msg: String) {
                Logger.t("NET").i(msg)
            }
        }
        OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .cookieJar(object : CookieJar {
                    override fun loadForRequest(url: HttpUrl): List<Cookie> {
                        val cookieEntity = try {
                            MMKV.defaultMMKV().decodeString(SP_KEY_COOKIES, "").toTypeEntity<CookieEntity>()
                        } catch (e: Exception) {
                            Logger.t("NET").e(e, "loadCookie")
                            null
                        }
                        return cookieEntity?.cookies.orEmpty()
                    }

                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                        if (cookies.size > 1) {
                            val ls = arrayListOf<Cookie>()
                            ls.addAll(cookies)
                            val cookieEntity = CookieEntity(ls)
                            MMKV.defaultMMKV().encode(SP_KEY_COOKIES, cookieEntity.toJsonString())
                        }
                    }
                })
                .addNetworkInterceptor(
                        LoggerInterceptor(logger, if (BuildConfig.DEBUG) LoggerInterceptor.LEVEL_BODY else LoggerInterceptor.LEVEL_NONE)
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

/** 数据仓库 Module */
val repositoryModule: Module = module {
    factory { HomepageRepository(get()) }
    factory { SystemRepository(get()) }
    factory { BjnewsRepository(get()) }
    factory { ProjectRepository(get()) }
    factory { MyRepository(get()) }
    factory { CollectRepository(get()) }
    factory { SearchRepository(get()) }
}

/** 适配器 Module */
val adapterModule: Module = module {
    factory { ArticleListRvAdapter() }
    factory { SystemCategoryRvAdapter() }
    factory { NavigationRvAdapter() }
}

/** ViewModel Module */
val viewModelModule: Module = module {
    viewModel { BlankViewModel() }
    viewModel { MainViewModel() }
    viewModel { HomepageViewModel(get(), get()) }
    viewModel { SystemViewModel() }
    viewModel { SystemCategoryViewModel(get()) }
    viewModel { NavigationViewModel(get()) }
    viewModel { BjnewsViewModel(get()) }
    viewModel { BjnewsArticlesViewModel(get(), get()) }
    viewModel { ProjectViewModel(get()) }
    viewModel { ProjectArticlesViewModel(get(), get()) }
    viewModel { MyViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { WebViewViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { GeneralViewModel() }
    viewModel { CollectionViewModel(get()) }
    viewModel { CollectedWebViewModel(get()) }
    viewModel { EditCollectedWebViewModel(get()) }
    viewModel { SystemArticlesViewModel(get(), get()) }
    viewModel { ProgressViewModel() }
}