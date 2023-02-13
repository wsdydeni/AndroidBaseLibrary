# AndroidBaseLibrary

[![](https://jitpack.io/v/wsdydeni/AndroidBaseLibrary.svg)](https://jitpack.io/#wsdydeni/AndroidBaseLibrary)
[![GitHub stars](https://img.shields.io/github/stars/wsdydeni/AndroidBaseLibrary)](https://github.com/wsdydeni/AndroidBaseLibrary/stargazers)
[![GitHub issues](https://img.shields.io/github/issues/wsdydeni/AndroidBaseLibrary)](https://github.com/wsdydeni/AndroidBaseLibrary/issues)

`MVI` 架构的 `Android` 基础库

[MVI 架构 - 用Kotlin Flow解决Android开发中的痛点问题](https://juejin.cn/post/7031726493906829319)

[MVI 架构 - 不做跟风党，LiveData，StateFlow，SharedFlow 使用场景对比](https://juejin.cn/post/7007602776502960165)

## 沉浸式

### 状态栏

[封装使用](https://github.com/wsdydeni/AndroidBaseLibrary/blob/master/baselib/src/main/kotlin/wsdydeni/library/android/utils/immersion/ImmersionStatusBarExt.kt)

### 导航栏

[Android 系统 Bar 沉浸式完美兼容方案](https://juejin.cn/post/7075578574362640421)

## 键盘高度适配

[PopupWindow 方案](https://github.com/wsdydeni/AndroidBaseLibrary/blob/master/baselib/src/main/kotlin/wsdydeni/library/android/utils/keyboard/KeyboardHeightProvider.kt)

[扩展函数封装](https://github.com/wsdydeni/AndroidBaseLibrary/blob/master/baselib/src/main/kotlin/wsdydeni/library/android/utils/keyboard/KeyboardExt.kt)

## 屏幕适配

[AutoDensity](https://github.com/Hbottle/AutoDensity)

今日头条Android屏幕适配方案&Smallest屏幕适配方案最佳实践

## 网络请求

[AndroidBaseLibraryNetwork](https://github.com/wsdydeni/AndroidBaseLibraryNetwork/tree/master/baselib-network/src/main/java/wsdydeni/library/android_network/network)

# How to Use

To get a Git project into your build:

## Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
## Step 2. Add the dependency

```gradle
dependencies {
    implementation 'com.github.wsdydeni:AndroidBaseLibrary:1.2.0'
}
```
