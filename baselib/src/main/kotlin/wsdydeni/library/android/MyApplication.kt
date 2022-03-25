package wsdydeni.library.android

import android.app.Application
import wsdydeni.library.android.utils.another.LogUtil

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtil.init(BuildConfig.IS_DEBUG)
    }
}