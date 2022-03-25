package com.wsdydeni.baselib

import android.app.Application
import com.wsdydeni.baselib.utils.another.LogUtil

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtil.init(BuildConfig.IS_DEBUG)
    }
}