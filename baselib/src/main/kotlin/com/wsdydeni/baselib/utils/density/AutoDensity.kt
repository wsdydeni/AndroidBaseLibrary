package com.wsdydeni.baselib.utils.density

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log

/**
 * @Date: 2020/4/17
 * @Author: hugo
 * @Description: 今日头条的屏幕适配方案
 * [一种极低成本的Android屏幕适配方式](https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA)
 * [今日头条屏幕适配方案终极版正式发布](https://www.jianshu.com/p/4aa23d69d481)
 * [AndroidAutoSize](https://github.com/JessYanCoding/AndroidAutoSize)
 *
 *
 * 需要考虑以下需求：
 * 1.为特定Activity指定设计尺寸 : 已支持
 * 2.支持Activity选择不使用适配 ：已支持
 * 3.支持第三方SDK引入的Activity适配(选择适配或者保持原来) ：已支持
 * 4.支持选择smallest-width或者height作为设计基准 ：已支持
 * 5.支持选择字体是否跟随适配 ：已支持
 * 6.Fragment适配(不是很重要，但是可以参考一下是怎么做的) ： 未支持
 *
 * 关于phone设计稿适配pad的问题：
 * 1.按照设计稿适配，这样做风险比较大，切换到竖屏的时候可能用不了（内容较长的页面需要滚动）；
 * 2.不适配，根据sw-600dp来判断，如果大于这个值则不要初始化 AutoDensity；
 * 3.按照一定的策略来适配，如sw-600dp：500F, sw-800dp: 600F, 也就是按比例缩放的原理；
 *
 */
@Suppress("unused")
class AutoDensity private constructor() : ActivityLifecycleCallbacks, ComponentCallbacks {

    companion object {
        val TAG: String = AutoDensity::class.java.simpleName
        val instance = AutoDensity()
    }

    private lateinit var application: Application
    private lateinit var autoDensityConfig: AutoDensityConfig
    private lateinit var customAdaptManager: CustomAdaptManager
    private var inited: Boolean = false

    fun getAutoDensityConfig(): AutoDensityConfig {
        if (!inited) {
            throw NullPointerException("before you call getCustomAdaptManager, call init first")
        }
        return autoDensityConfig
    }

    fun getCustomAdaptManager(): CustomAdaptManager {
        if (!inited) {
            throw NullPointerException("before you call getCustomAdaptManager, call init first")
        }
        return customAdaptManager
    }

    /**
     * 默认设计尺寸为: 375dp，并且以smallest-width作为设计基准
     *
     * @param application application
     */
    fun init(application: Application) {
        this.init(application, DesignDraft(true, 375f, true))
    }

    /**
     * 正常使用请调用这个方法
     *
     * @param application    application
     * @param appDesignDraft design info
     */
    fun init(application: Application, appDesignDraft: DesignDraft): AutoDensity {
        this.application = application
        this.application.registerActivityLifecycleCallbacks(this)
        this.application.registerComponentCallbacks(this)
        this.autoDensityConfig = AutoDensityConfig(application, appDesignDraft)
        this.customAdaptManager = CustomAdaptManager()
        this.inited = true
        return this
    }

    /**
     * 想象中的一种适配pad的方案(毕竟为pad单独设计一套UI和布局代价有点大)，就是一种策略，通过改变设计稿的
     * 大小，控制在不同屏幕的缩放比例在一定的范围不要显得过大。
     */
    fun adaptPad(): AutoDensity {
        val smallestWidth: Float
        val designDraft = autoDensityConfig.appDesignDraft
        smallestWidth = if (designDraft.isBaseOnSmallestWidth) {
            autoDensityConfig.width.coerceAtMost(autoDensityConfig.height)
        } else {
            autoDensityConfig.width.coerceAtLeast(autoDensityConfig.height)
        }
        val adaptWidth: Float = when {
            smallestWidth > 1000f -> {
                800f
            }
            smallestWidth > 800f -> {
                600f
            }
            smallestWidth > 600 -> {
                500f
            }
            smallestWidth > 500 -> {
                450f
            }
            else -> {
                designDraft.designSize
            }
        }
        autoDensityConfig.appDesignDraft = DesignDraft(
            designDraft.isBaseOnSmallestWidth,
            adaptWidth, designDraft.isEnableScaledFont
        )
        return this
    }

    private fun apply(activity: Activity, designDraft: DesignDraft) {
        val width = autoDensityConfig.widthPixels.coerceAtMost(autoDensityConfig.heightPixels)
        val height = autoDensityConfig.widthPixels.coerceAtLeast(autoDensityConfig.heightPixels)
        if (width < 1) {
            Log.d(TAG, "can not read widthPixels and heightPixels")
            return
        }
        val targetDensity: Float = if (designDraft.isBaseOnSmallestWidth) {
            width / designDraft.designSize
        } else {
            height / designDraft.designSize
        }
        val targetDensityDpi = (DisplayMetrics.DENSITY_MEDIUM * targetDensity).toInt()
        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
        if (designDraft.isEnableScaledFont) {
            val targetScaledDensity = targetDensity * (autoDensityConfig.scaledDensity
                    / autoDensityConfig.density)
            activityDisplayMetrics.scaledDensity = targetScaledDensity
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale < 0) {
            return
        }
        autoDensityConfig.scaledDensity = application.resources.displayMetrics.scaledDensity
    }

    override fun onLowMemory() {}

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (!autoDensityConfig.isEnableAutoDensity) {
            // 1.全局禁用AutoDensity适配，直接返回即可
            return
        }
        if (activity is CancelAdapt) {
            // 2.如果activity实现了CancelAdapt接口，那么不需要适配
            return
        }
        if (customAdaptManager.isCancelAdapt(activity.javaClass)) {
            // 3.如果是第三方SDK的activity，并且明确配置了不适配，则不适配
            return
        }
        val designDraft = if (activity is CustomAdapt) {
            // 1. 实现CustomAdapt具有最高优先级
            (activity as CustomAdapt).designSize()
        } else if (autoDensityConfig.isEnableCustomAdapt &&
            customAdaptManager.getCustomAdaptDesignInfo(activity.javaClass) != null
        ) {
            // 2.第三方SDK的Activity，如果有独立的设计，那么使用这个设计参数
            customAdaptManager.getCustomAdaptDesignInfo(activity.javaClass)!!
        } else {
            // 3. 剩下的就是使用全局统一设计稿的了
            autoDensityConfig.appDesignDraft
        }
        try {
            apply(activity, designDraft)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

}