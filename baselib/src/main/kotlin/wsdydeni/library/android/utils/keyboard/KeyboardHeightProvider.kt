package wsdydeni.library.android.utils.keyboard

import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import wsdydeni.library.android.utils.another.LogUtil
import wsdydeni.library.android.utils.display.PixelUtil


/**
 * @see <a href="https://www.jianshu.com/p/bfdffc88bf0a">adjustNothing时监听键盘状态及高度</a>
 */
class KeyboardHeightProvider(activity: FragmentActivity,listener: KeyboardHeightListener) : PopupWindow(activity) {

    private var recordHeight = 0

    init {
        val popupView = LinearLayout(activity)
        popupView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            val rect = Rect()
            contentView.getWindowVisibleDisplayFrame(rect)
            var keyboardHeight: Int = metrics.heightPixels - (rect.bottom - rect.top)
            if (keyboardHeight < PixelUtil.dip2px(activity,200f)) {
                keyboardHeight = 0
            }
            if(keyboardHeight < 1000 && keyboardHeight > PixelUtil.dip2px(activity,200f)) {
                recordHeight = keyboardHeight
            }
            if(keyboardHeight > 1000) {
                if(recordHeight != 0) {
                    keyboardHeight = recordHeight
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    activity.window.decorView.setOnApplyWindowInsetsListener { _, insets ->
                        insets.getInsets(WindowInsets.Type.navigationBars()).bottom.run {
                            keyboardHeight = insets.getInsets(WindowInsets.Type.ime()).bottom.minus(this)
                            recordHeight = keyboardHeight
                            LogUtil.d("keyboardHeight 修正: $keyboardHeight")
                            insets
                        }
                    }
                }
            }
            val isLandscape = metrics.widthPixels > metrics.heightPixels
            val keyboardOpen = keyboardHeight > 0
            activity.window.decorView.setOnApplyWindowInsetsListener(null)
            listener.onKeyboardHeightChanged(keyboardHeight, keyboardOpen, isLandscape)
        }
        popupView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        contentView = popupView
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        inputMethodMode = INPUT_METHOD_NEEDED
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(ColorDrawable(0))
        val content = activity.findViewById<View>(android.R.id.content) as ViewGroup
        content.post { showAtLocation(content, Gravity.NO_GRAVITY, 0, 0) }
        activity.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if(event == Lifecycle.Event.ON_DESTROY) {
                    contentView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
                    dismiss()
                }
            }
        })
    }
}