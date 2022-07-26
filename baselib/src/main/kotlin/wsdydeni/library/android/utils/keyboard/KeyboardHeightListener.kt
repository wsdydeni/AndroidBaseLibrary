package wsdydeni.library.android.utils.keyboard

interface KeyboardHeightListener {
    /**
     * 键盘监听
     *
     * @param keyboardHeight 键盘高度
     * @param keyboardOpen 键盘是否打开
     * @param isHorizontal 是否为横屏
     */
    fun onKeyboardHeightChanged(keyboardHeight: Int, keyboardOpen: Boolean, isHorizontal: Boolean)
}