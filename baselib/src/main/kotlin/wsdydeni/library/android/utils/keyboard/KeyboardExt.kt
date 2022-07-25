package wsdydeni.library.android.utils.keyboard

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import wsdydeni.library.android.utils.another.LogUtil
import wsdydeni.library.android.utils.density.AutoDensity
import wsdydeni.library.android.utils.display.PixelUtil
import wsdydeni.library.android.utils.immersion.getNavBarHeight
import wsdydeni.library.android.utils.immersion.getStatusBarHeight


fun FragmentActivity.addKeyboardMonitor() {
    KeyboardHeightProvider(this,object : KeyboardHeightListener {
        override fun onKeyboardHeightChanged(keyboardHeight: Int, keyboardOpen: Boolean, isHorizontal: Boolean) {
            if(keyboardHeight > 1000) {
                return
            }
            if(keyboardHeight == 0) {
                val content = findViewById<View>(android.R.id.content) as ViewGroup
                content.translationY = 0f
                return
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
                    LogUtil.d("偏移量dp: ${PixelUtil.px2dp(this@addKeyboardMonitor,difference)}")
                    val content = findViewById<View>(android.R.id.content) as ViewGroup
                    content.translationY = difference.toFloat()
                }
            }
        }
    })
}

/**
 * 键盘监听
 *
 * @param onKeyboardHeightChanged 监听键盘变化回调
 *      keyboardHeight 键盘高度
 *      keyboardOpen 键盘是否打开
 *      isLandscape 是否为横屏
 */
fun FragmentActivity.addKeyboardMonitor(onKeyboardHeightChanged: (keyboardHeight: Int,keyboardOpen: Boolean, isHorizontal: Boolean) -> Unit) {
    KeyboardHeightProvider(this,object : KeyboardHeightListener {
        override fun onKeyboardHeightChanged(keyboardHeight: Int,keyboardOpen: Boolean, isHorizontal: Boolean) {
            onKeyboardHeightChanged(keyboardHeight, keyboardOpen, isHorizontal)
        }
    })
}
