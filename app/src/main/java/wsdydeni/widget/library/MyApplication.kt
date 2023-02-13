package wsdydeni.widget.library

import android.app.Application
import com.hjq.toast.ToastUtils
import wsdydeni.library.android.utils.another.LogUtil
import wsdydeni.library.android.utils.density.AutoDensity
import wsdydeni.library.android.utils.density.DesignDraft

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtil.init(true)
        ToastUtils.init(this)
        AutoDensity.instance.init(this, DesignDraft(designSize = 360f))
    }
}