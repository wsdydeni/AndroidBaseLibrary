package wsdydeni.widget.library

import android.app.Application
import wsdydeni.library.android.utils.density.AutoDensity
import wsdydeni.library.android.utils.density.DesignDraft

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AutoDensity.instance.init(this, DesignDraft(designSize = 360f))
    }
}