package com.wsdydeni.baselib.utils.immersion

import android.app.Dialog
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager


// https://juejin.cn/post/7075578574362640421#heading-18

fun Dialog.setLightStatusBar(isLightingColor: Boolean) {
    val window = this.window ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (isLightingColor) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}

fun Dialog.setLightNavigationBar(isLightingColor: Boolean) {
    val window = this.window ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isLightingColor) {
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR else 0
    }
}

fun Dialog.immersiveStatusBar() {
    val window = this.window ?: return
    (window.decorView as ViewGroup).setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
        override fun onChildViewAdded(parent: View?, child: View?) {
            if (child?.id == android.R.id.statusBarBackground) {
                child.scaleX = 0f
            }
        }

        override fun onChildViewRemoved(parent: View?, child: View?) {}
    })
}

fun Dialog.immersiveNavigationBar() {
    val window = this.window ?: return
    (window.decorView as ViewGroup).setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
        override fun onChildViewAdded(parent: View?, child: View?) {
            if (child?.id == android.R.id.navigationBarBackground) {
                child.scaleX = 0f
            } else if (child?.id == android.R.id.statusBarBackground) {
                child.scaleX = 0f
            }
        }

        override fun onChildViewRemoved(parent: View?, child: View?) {}
    })
}

fun Dialog.immersion() {
    window?.let {
        it.setGravity(Gravity.CENTER)
        it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        it.setWindowAnimations(com.wsdydeni.baselib.R.style.PopWindowAnimStyle)
    }
    immersiveStatusBar()
    immersiveNavigationBar()
    setLightStatusBar(true)
    setLightNavigationBar(true)
}
