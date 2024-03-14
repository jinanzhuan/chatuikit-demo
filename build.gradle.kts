// 新增 buildscript
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.huawei.agconnect:agcp:1.6.0.300")
        classpath("com.hihonor.mcs:asplugin:2.0.0")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}