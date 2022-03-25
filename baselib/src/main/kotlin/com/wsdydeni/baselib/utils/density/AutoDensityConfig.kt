package com.wsdydeni.baselib.utils.density

import android.app.Application

/**
 * @Date: 2020/4/20
 * @Author: hugo
 * @Description: 全局的配置
 */
class AutoDensityConfig(application: Application, designDraft: DesignDraft) {
    var isEnableAutoDensity = true // 是否启用适配，如果否，则不适配，可以应急
    var isEnableCustomAdapt = true // 是否允许Activity自定义设计稿大小，如果否则使用全局的配置
    var isEnableScaledFont = true // 字体是否跟着适配
    var appDesignDraft: DesignDraft = designDraft  // 全局的设计参数
    var scaledDensity: Float // 控制字体

    // density、widthPixels、heightPixels是设备原始值，width、height的单位是dp
    @JvmField
    val density: Float
    @JvmField
    val widthPixels: Int
    @JvmField
    val heightPixels: Int
    @JvmField
    val width: Float
    @JvmField
    val height: Float

    init {
        val displayMetrics = application.resources.displayMetrics
        density = displayMetrics.density
        scaledDensity = displayMetrics.scaledDensity
        widthPixels = displayMetrics.widthPixels
        heightPixels = displayMetrics.heightPixels
        width = widthPixels / density
        height = heightPixels / density
    }
}