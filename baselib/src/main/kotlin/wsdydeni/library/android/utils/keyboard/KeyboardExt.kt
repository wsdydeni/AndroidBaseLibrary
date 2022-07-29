package wsdydeni.library.android.utils.keyboard

import androidx.fragment.app.FragmentActivity

fun FragmentActivity.addKeyboardMonitor(onKeyboardHeightChanged: (keyboardHeight: Int, keyboardOpen: Boolean, isHorizontal: Boolean) -> Unit) {
    KeyboardHeightProvider(this,object : KeyboardHeightListener {
        override fun onKeyboardHeightChanged(keyboardHeight: Int,keyboardOpen: Boolean, isHorizontal: Boolean) {
            onKeyboardHeightChanged(keyboardHeight, keyboardOpen, isHorizontal)
        }
    })
}
