// 新增 buildscript
buildscript {
    dependencies {
        // Note: If you use HuaWei or Honor push, you need to add the following dependencies
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("com.huawei.agconnect:agcp:1.9.1.301")
        classpath("com.hihonor.mcs:asplugin:2.0.1.300")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.1" apply false
}