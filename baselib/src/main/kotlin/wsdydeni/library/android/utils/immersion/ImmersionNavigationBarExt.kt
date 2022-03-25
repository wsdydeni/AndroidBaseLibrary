package wsdydeni.library.android.utils.immersion

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsdydeni.baselib.R


fun Activity.setLightNavigationBar(isLightingColor: Boolean) {
    val window = this.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isLightingColor) {
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR else 0
    }
}

fun Activity.getNavBarHeight(): Int {
    return navigationBarHeightLiveData.value ?: 0
}

fun Activity.setNavigationBarColor(color: Int) {
    val navigationBarView = window.decorView.findViewById<View?>(R.id.navigation_bar_view)
    if (color == 0 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
        navigationBarView?.setBackgroundColor(STATUS_BAR_MASK_COLOR)
    } else {
        navigationBarView?.setBackgroundColor(color)
    }
}

fun Activity.immersiveNavigationBar(callback: (() -> Unit)? = null) {
    val view = (window.decorView as ViewGroup).getChildAt(0)
    view.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
        val lp = view.layoutParams as FrameLayout.LayoutParams
        if (lp.bottomMargin > 0) {
            lp.bottomMargin = 0
            v.layoutParams = lp
        }
        if (view.paddingBottom > 0) {
            view.setPadding(0, view.paddingTop, 0, 0)
            val content = findViewById<View>(android.R.id.content)
            content.requestLayout()
        }
    }

    val content = findViewById<View>(android.R.id.content)
    content.setPadding(0, content.paddingTop, 0, -1)

    val heightLiveData = MutableLiveData<Int>()
    heightLiveData.value = 0
    window.decorView.setTag(R.id.navigation_height_live_data, heightLiveData)
    callback?.invoke()

    window.decorView.findViewById(R.id.navigation_bar_view) ?: View(window.context).apply {
        id = R.id.navigation_bar_view
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, heightLiveData.value ?: 0)
        params.gravity = Gravity.BOTTOM
        layoutParams = params
        (window.decorView as ViewGroup).addView(this)

        if (this@immersiveNavigationBar is FragmentActivity) {
            heightLiveData.observe(this@immersiveNavigationBar) {
                val lp = layoutParams
                lp.height = heightLiveData.value ?: 0
                layoutParams = lp
            }
        }

        (window.decorView as ViewGroup).setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                if (child?.id == android.R.id.navigationBarBackground) {
                    child.scaleX = 0f
                    bringToFront()

                    child.addOnLayoutChangeListener { _, _, top, _, bottom, _, _, _, _ ->
                        heightLiveData.value = bottom - top
                    }
                } else if (child?.id == android.R.id.statusBarBackground) {
                    child.scaleX = 0f
                }
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {
            }
        })
    }
    setNavigationBarColor(Color.TRANSPARENT)
}

fun Activity.fitNavigationBar(fit: Boolean) {
    val content = findViewById<View>(android.R.id.content)
    if (fit) {
        content.setPadding(0, content.paddingTop, 0, navigationBarHeightLiveData.value ?: 0)
    } else {
        content.setPadding(0, content.paddingTop, 0, -1)
    }
    if (this is FragmentActivity) {
        navigationBarHeightLiveData.observe(this) {
            if (content.paddingBottom != -1) {
                content.setPadding(0, content.paddingTop, 0, it)
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
val Activity.navigationBarHeightLiveData: LiveData<Int>
    get() {
        var liveData = window.decorView.getTag(R.id.navigation_height_live_data) as? LiveData<Int>
        if (liveData == null) {
            liveData = MutableLiveData()
            window.decorView.setTag(R.id.navigation_height_live_data, liveData)
        }
        return liveData
    }

private const val STATUS_BAR_MASK_COLOR = 0x7F000000



