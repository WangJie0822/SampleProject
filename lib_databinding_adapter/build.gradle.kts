plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {

    // 编译 SDK 版本
    compileSdkVersion(Configuration.AppConfigs.compile_sdk_version)

    // 资源前缀
    resourcePrefix("databinding")

    defaultConfig {
        // 最低支持版本
        minSdkVersion(Configuration.AppConfigs.min_sdk_version)
        // 目标 SDK 版本
        targetSdkVersion(Configuration.AppConfigs.target_sdk_version)
    }

    // 源文件路径设置
    sourceSets {
        named("main") {
            java.srcDirs("src/main/java", "src/main/kotlin")
            jni.srcDirs("libs", "jniLibs")
        }
    }

    buildFeatures {
        // DataBinding 开启
        dataBinding = true
    }

    // Java 版本配置
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Kotlin
    implementation(Configuration.Dependencies.kotlin_stdlib)

    // v4
    implementation(Configuration.Dependencies.androidx_legacy)
    // v7
    implementation(Configuration.Dependencies.androidx_appcompat)
    // material
    implementation(Configuration.Dependencies.androidx_material)

    // Base 库
//    api("com.github.WangJie0822.SampleProject:lib_base:1.0.1")
    api(project(":lib_base"))
}
