package wsdydeni.library.android.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import wsdydeni.library.android.utils.immersion.fitNavigationBar
import wsdydeni.library.android.utils.immersion.immersionStatusBar
import wsdydeni.library.android.utils.immersion.immersiveNavigationBar
import wsdydeni.library.android.utils.immersion.setLightNavigationBar


open class BaseActivity : FragmentActivity {

    constructor() : super()

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    open var immersionStatus = false

    open var immersiveNavigation = false

    open var isLightSystemBar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if(immersionStatus) immersionStatusBar(false, isLightSystemBar)
        if(immersiveNavigation) {
            immersiveNavigationBar()
            fitNavigationBar(true)
            setLightNavigationBar(isLightSystemBar)
        }
        super.onCreate(savedInstanceState)
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT.also { requestedOrientation = it }
        initView()
    }

    override fun onStart() {
        super.onStart()
        initData()
    }

    open fun initView() {}
    open fun initData() {}

}