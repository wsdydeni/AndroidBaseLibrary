package com.wsdydeni.baselib.utils.density

/**
 * @Date: 2020/4/20
 * @Author: hugo
 * @Description: 如果某个Activity的设计稿尺寸和全局的不一样，可以实现这个接口
 */
interface CustomAdapt {
    /**
     * 设计稿
     * @return DesignDraft
     */
    fun designSize(): DesignDraft
}