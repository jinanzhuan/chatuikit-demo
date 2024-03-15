pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.aliyun.com/repository/public/")}
        maven { url = uri("https://developer.huawei.com/repo/")}
        maven { url = uri("https://developer.hihonor.com/repo")}
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.aliyun.com/repository/public/")}
        maven { url = uri("https://developer.huawei.com/repo/")}
        maven { url = uri("https://developer.hihonor.com/repo")}
    }
}

rootProject.name = "chatuikit-android-demo"
include(":app")
include(":ease-im-kit")
project(":ease-im-kit").projectDir = File("../ChatUIKit/ease-im-kit")
 