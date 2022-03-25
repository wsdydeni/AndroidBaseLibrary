package wsdydeni.library.android.utils.density

import android.app.Activity

/**
 * @Date: 2020/4/20
 * @Author: hugo
 * @Description: 第三方SDK的Activity和全局设计不一致的Activity，可以在这里添加配置单独适配
 * （应用内的Activity如果需要独立配置，可以实现CustomAdapt接口，而不是添加到这里，后面可以扩展一下）
 */
class CustomAdaptManager {
    private val cancelAdaptMap: MutableMap<String, String?> =
        HashMap()
    private val customAdaptMap: MutableMap<String, DesignDraft> =
        HashMap()

    fun addCancelAdapt(clazz: Class<out Activity?>) {
        cancelAdaptMap[clazz.name] = clazz.name
    }

    fun addCustomAdapt(
        clazz: Class<out Activity?>,
        designDraft: DesignDraft
    ) {
        customAdaptMap[clazz.name] = designDraft
    }

    fun isCancelAdapt(clazz: Class<out Activity?>): Boolean {
        return cancelAdaptMap[clazz.name] != null
    }

    fun getCustomAdaptDesignInfo(clazz: Class<out Activity?>): DesignDraft? {
        return customAdaptMap[clazz.name]
    }
}