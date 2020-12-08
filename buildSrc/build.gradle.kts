plugins {
    `kotlin-dsl`
}

// 源文件路径设置
sourceSets {
    main {
        java.srcDir("source")
    }
}

repositories {
    jcenter()
}