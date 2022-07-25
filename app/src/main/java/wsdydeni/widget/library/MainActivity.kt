package wsdydeni.widget.library

import android.os.Bundle
import wsdydeni.library.android.base.BaseActivity
import wsdydeni.library.android.utils.immersion.showStatusBarView

class MainActivity : BaseActivity() {

    override var openImmersion: Boolean = true

    override var openKeyboardMonitor: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showStatusBarView(
            findViewById(wsdydeni.library.android.R.id.fillStatusBarView),
//            ContextCompat.getColor(this, wsdydeni.library.android.R.color.color_6d7174)
        )
    }
}