package wsdydeni.library.android.utils.immersion

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


fun Activity.setSystemStatusBar(isShowSystemStatusBar: Boolean,isLightStatusBar: Boolean) {
    var flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    if(!isShowSystemStatusBar) {
        flag = flag or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isLightStatusBar) {
        flag = if(isLightStatusBar) flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else flag and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
    window.decorView.systemUiVisibility = flag
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(isShowSystemStatusBar)
    }
}

fun Activity.setStatusBarColor(@ColorInt statusColor: Int) {
    var color = statusColor
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        color = Color.GRAY
    }
    window.statusBarColor = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) color else -2
}

fun Activity.immersionStatusBar(isShowSystemStatusBar: Boolean,isLightStatusBar: Boolean) {
    setSystemStatusBar(isShowSystemStatusBar,isLightStatusBar)
    setStatusBarColor(Color.TRANSPARENT)
}

fun Activity.showStatusBarView(view: View,@ColorInt backgroundColor: Int? = null) {
    val lp = view.layoutParams
    lp.width = LinearLayout.LayoutParams.MATCH_PARENT
    lp.height = getStatusBarHeight()
    view.layoutParams = lp
    view.visibility = View.VISIBLE
    if(backgroundColor != null) {
        view.setBackgroundColor(backgroundColor)
    }
}

fun Activity.getStatusBarHeight() : Int {
    val windowInsetsCompat = ViewCompat.getRootWindowInsets(window.decorView)
    val insets = windowInsetsCompat?.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.statusBars())
    if(insets != null) {
        return insets.top
    }
    val resources: Resources = resources
    val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}