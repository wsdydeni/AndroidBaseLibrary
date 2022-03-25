package wsdydeni.library.android.utils.keyboard

interface KeyboardHeightListener {
    fun onKeyboardHeightChanged(keyboardHeight: Int, keyboardOpen: Boolean, isLandscape: Boolean)
}