package wsdydeni.widget.library

import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import wsdydeni.library.android.base.BaseActivity
import wsdydeni.library.android.utils.another.LogUtil
import wsdydeni.library.android.utils.density.AutoDensity
import wsdydeni.library.android.utils.display.PixelUtil
import wsdydeni.library.android.utils.immersion.getNavBarHeight
import wsdydeni.library.android.utils.immersion.getStatusBarHeight
import wsdydeni.library.android.utils.immersion.showStatusBarView
import wsdydeni.library.android.utils.keyboard.addKeyboardMonitor

class MainActivity : BaseActivity(R.layout.activity_main) {

    override var isLightSystemBar = true

    override var immersiveNavigation = true

    override var immersionStatus = true

    override fun initView() {
        addKeyboardMonitor { keyboardHeight, _, _ ->
            if(keyboardHeight > 1000) {
                return@addKeyboardMonitor
            }
            if(keyboardHeight == 0) {
                val inputLayout = findViewById<LinearLayout>(R.id.input_layout) as LinearLayout
//                val content = findViewById<View>(android.R.id.content) as ViewGroup
                inputLayout.translationY = 0f
                return@addKeyboardMonitor
            }
            val focusView: View? = window.decorView.findFocus()
            if (focusView != null && focusView is EditText) {
                val locations = IntArray(2)
                focusView.getLocationOnScreen(locations)
                val focusEtTop = locations[1]
                val focusViewHeight = focusView.measuredHeight
                val focusBottom = focusEtTop + focusViewHeight
                val generalHeight = AutoDensity.instance.getAutoDensityConfig().heightPixels
                if (keyboardHeight > generalHeight + getStatusBarHeight() - focusBottom) {
                    LogUtil.d("屏幕高度: ${AutoDensity.instance.getAutoDensityConfig().heightPixels}")
                    LogUtil.d("状态栏高度: ${getStatusBarHeight()}")
                    LogUtil.d("导航栏高度: ${getNavBarHeight()}")
                    LogUtil.d("输入框底部高度: $focusBottom")
                    LogUtil.d("输入框底部到屏幕底部的距离: ${generalHeight + getStatusBarHeight() - focusBottom}")
                    LogUtil.d("键盘高度: $keyboardHeight")
                    val difference = (generalHeight + getStatusBarHeight()) - (focusBottom + keyboardHeight)
                    LogUtil.d("布局纵向偏移量: $difference")
                    LogUtil.d("偏移量dp: ${PixelUtil.px2dp(this,difference)}")
                    val inputLayout = findViewById<LinearLayout>(R.id.input_layout) as LinearLayout
//                    val content = findViewById<View>(android.R.id.content) as ViewGroup
                    inputLayout.translationY = difference.toFloat()
                }
            }
        }
        showStatusBarView(
            findViewById(wsdydeni.library.android.R.id.fillStatusBarView),
            ContextCompat.getColor(this, wsdydeni.library.android.R.color.color_6d7174)
        )
    }
}