package com.wsdydeni.baselib.utils.keyboard

interface KeyboardHeightListener {
    fun onKeyboardHeightChanged(keyboardHeight: Int, keyboardOpen: Boolean, isLandscape: Boolean)
}