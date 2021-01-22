package com.wj.sampleproject.di

import cn.wj.common.ext.orEmpty
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.orhanobut.logger.Logger
import com.wj.android.okhttp.InterceptorLogger
import com.wj.android.okhttp.LoggerInterceptor
import com.wj.sampleproject.BuildConfig
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.adapter.NavigationRvAdapter
import com.wj.sampleproject.adapter.SystemCategoryRvAdapter
import com.wj.sampleproject.constants.DATA_CACHE_KEY_COOKIES
import com.wj.sampleproject.entity.CookieEntity
import com.wj.sampleproject.net.UrlDefinition
import com.wj.sampleproject.net.WebService
import com.wj.sampleproject.repository.ArticleRepository
import com.wj.sampleproject.repository.UserRepository
import com.wj.sampleproject.tools.*
import com.wj.sampleproject.viewmodel.*
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

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
                            DATA_CACHE_KEY_COOKIES.decodeString("").toTypeEntity<CookieEntity>()
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
                            DATA_CACHE_KEY_COOKIES.encode(cookieEntity.toJsonString())
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
                .addConverterFactory(jsonDefault.asConverterFactory("application/json; charset=UTF-8".toMediaType()))
                .client(get())
                .build()
    }

    single<WebService> {
        get<Retrofit>().create(WebService::class.java)
    }
}

/** 数据仓库 Module */
val repositoryModule: Module = module {
    factory { UserRepository(get()) }
    factory { ArticleRepository(get()) }
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
    viewModel { HomepageViewModel(get()) }
    viewModel { SystemViewModel() }
    viewModel { SystemCategoryViewModel(get()) }
    viewModel { NavigationViewModel(get()) }
    viewModel { BjnewsViewModel(get()) }
    viewModel { BjnewsArticlesViewModel(get()) }
    viewModel { ProjectViewModel(get()) }
    viewModel { ProjectArticlesViewModel(get()) }
    viewModel { MyViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { WebViewViewModel() }
    viewModel { LoginViewModel(get()) }
    viewModel { GeneralViewModel() }
    viewModel { CollectionViewModel(get()) }
    viewModel { CollectedWebViewModel(get()) }
    viewModel { EditCollectedWebViewModel(get()) }
    viewModel { SystemArticlesViewModel(get()) }
    viewModel { ProgressViewModel() }
    viewModel { StudyViewModel() }
    viewModel { SettingsViewModel(get()) }
    viewModel { VerificationViewModel(get()) }
    viewModel { BiometricViewModel() }
    viewModel { QuestionAnswerViewModel(get()) }
    viewModel { CoinViewModel(get()) }
}