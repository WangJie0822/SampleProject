plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {

    // 编译 SDK 版本
    compileSdkVersion(Configuration.AppConfigs.compile_sdk_version)
    // 编译工具版本
    buildToolsVersion(Configuration.AppConfigs.build_tools_version)

    // 资源前缀
    resourcePrefix("app")

    defaultConfig {
        // 应用 id
        applicationId = Configuration.AppConfigs.application_id

        // 最低支持版本
        minSdkVersion(Configuration.AppConfigs.min_sdk_version)
        // 目标 SDK 版本
        targetSdkVersion(Configuration.AppConfigs.target_sdk_version)

        // 应用版本号
        versionCode = Configuration.AppConfigs.version_code
        // 应用版本名
        versionName = Configuration.AppConfigs.version_name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 开启 Dex 分包
        multiDexEnabled = true
    }

    signingConfigs {
        // 签名配置
        getByName("debug") {
            keyAlias = Configuration.SigningConfigs.key_alias
            keyPassword = Configuration.SigningConfigs.key_password
            storeFile = file(Configuration.SigningConfigs.store_file)
            storePassword = Configuration.SigningConfigs.store_password
            isV1SigningEnabled = true
            isV2SigningEnabled = true
        }
        create("release") {
            keyAlias = Configuration.SigningConfigs.key_alias
            keyPassword = Configuration.SigningConfigs.key_password
            storeFile = file(Configuration.SigningConfigs.store_file)
            storePassword = Configuration.SigningConfigs.store_password
            isV1SigningEnabled = true
            isV2SigningEnabled = true
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isZipAlignEnabled = false
            isShrinkResources = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
            signingConfig = signingConfigs.findByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            isZipAlignEnabled = true
            isShrinkResources = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
            signingConfig = signingConfigs.findByName("release")
        }
    }

    // 维度
    flavorDimensions("version")

    productFlavors {
        // 正式线上版本
        create("online") {
            setDimension("version")
            // 版本名后缀
            versionNameSuffix = "_online"
            // 是否使用线上环境
            buildConfigField("boolean", "IS_ONLINE_ENV", "true")
        }

        // 测试版本
        create("offline") {
            setDimension("version")
            // 应用包名后缀
            applicationIdSuffix = ".offline"
            // 版本名后缀
            versionNameSuffix = "_offline"
            // 是否使用线上环境
            buildConfigField("boolean", "IS_ONLINE_ENV", "false")
        }

        // 开发版本
        create("dev") {
            setDimension("version")
            // 应用包名后缀
            applicationIdSuffix = ".dev"
            // 版本名后缀
            versionNameSuffix = "_dev"
            // 是否使用线上环境
            buildConfigField("boolean", "IS_ONLINE_ENV", "false")
        }
    }

    // 源文件路径设置
    sourceSets {
        named("main") {
            java.srcDirs("src/main/java", "src/main/kotlin")
            jni.srcDirs("libs", "jniLibs")
        }
    }

    // dex 配置
    dexOptions {
        jumboMode = true
        dexInProcess = true
        preDexLibraries = true
        javaMaxHeapSize = "4g"
        maxProcessCount = 6
        keepRuntimeAnnotatedClasses = false
    }

    buildFeatures {
        // DataBinding 开启
        dataBinding = true
    }

    lintOptions {
        // 出现错误不终止编译
        isAbortOnError = false
    }

    // 使用 httpclient
    useLibrary("org.apache.http.legacy")

    // 配置 APK 输出路径
    applicationVariants.all {
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName = "sample_${flavorName}_${buildType.name}_v${versionName}.apk"
            }
        }
    }

    // Java 版本配置
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // kotlin Jvm 版本
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    // Kotlin
    implementation(Configuration.Dependencies.kotlin_stdlib)
    // 协程
    implementation(Configuration.Dependencies.kotlin_coroutines)

    // Dex 分包
    implementation(Configuration.Dependencies.androidx_multidex)

    // v4
    implementation(Configuration.Dependencies.androidx_legacy)
    // v7
    implementation(Configuration.Dependencies.androidx_appcompat)
    // design
    implementation(Configuration.Dependencies.androidx_material)
    // RecyclerView
    implementation(Configuration.Dependencies.androidx_recyclerview)
    // 约束性布局
    implementation(Configuration.Dependencies.androidx_constraint)

    // activity
    implementation(Configuration.Dependencies.androidx_activity_ktx)
    // fragment
    implementation(Configuration.Dependencies.androidx_fragment_ktx)

    // ktx
    implementation(Configuration.Dependencies.androidx_core_ktx)

    // LifeCycle 拓展
    implementation(Configuration.Dependencies.androidx_lifecycle_ktx)
    implementation(Configuration.Dependencies.androidx_lifecycle_extensions)
    // ViewModel 拓展
    implementation(Configuration.Dependencies.androidx_lifecycle_viewmodel_ktx)
    // LiveData 拓展
    implementation(Configuration.Dependencies.androidx_lifecycle_livedata_ktx)

    // viewpager2
    implementation(Configuration.Dependencies.androidx_viewpager2)

    // Logger
    implementation(Configuration.Dependencies.logger)

    // Koin
    implementation(Configuration.Dependencies.koin_scope)
    implementation(Configuration.Dependencies.koin_viewmodel)
    implementation(Configuration.Dependencies.koin_ext)

    // LiveEventBus
    implementation(Configuration.Dependencies.live_event_bus)

    // OkHttp
    implementation(Configuration.Dependencies.okhttp)

    // Retrofit
    implementation(Configuration.Dependencies.retrofit)
    implementation(Configuration.Dependencies.retrofit_gson)

    // Glide
    implementation(Configuration.Dependencies.coil)

    // MMKV 数据存储
    implementation(Configuration.Dependencies.tencent_mmkv)

    // SmartRefreshLayout
    implementation(Configuration.Dependencies.smart_refresh)
    implementation(Configuration.Dependencies.smart_refresh_header_classics)
    implementation(Configuration.Dependencies.smart_refresh_footer_classics)

    // 状态栏工具
    implementation(Configuration.Dependencies.immersion_bar)
    implementation(Configuration.Dependencies.immersion_bar_ktx)

    // 换肤
    implementation(Configuration.Dependencies.skin_support)
    implementation(Configuration.Dependencies.skin_support_appcompat)
    implementation(Configuration.Dependencies.skin_support_material)
    implementation(Configuration.Dependencies.skin_support_cardview)
    implementation(Configuration.Dependencies.skin_support_constraint)

    // Tablayout
    implementation(Configuration.Dependencies.tablayout)

    // 依赖 base 库
    implementation(project(":lib_ui"))
    implementation(project(":lib_databinding_adapter"))
    implementation(project(":lib_okhttp_interceptor"))
    implementation(project(":lib_recyclerview"))
    implementation(project(":lib_views_custom"))
    implementation(project(":lib_swipe_back"))

    // 测试
    testImplementation(Configuration.Dependencies.test_junit)
    androidTestImplementation(Configuration.Dependencies.androidx_test_runner)
    androidTestImplementation(Configuration.Dependencies.androidx_test_espresso_core)
    androidTestImplementation(Configuration.Dependencies.androidx_test_ext_junit)
}
