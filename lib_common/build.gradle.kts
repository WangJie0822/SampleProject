plugins {
    id("java-library")
    kotlin("jvm")
    id("com.github.dcendents.android-maven")
}

group = "com.github.WangJie0822"

dependencies {
    // kotlin
    implementation(Configuration.Dependencies.kotlin_stdlib)

    // 测试
    testImplementation(Configuration.Dependencies.test_junit)
}
