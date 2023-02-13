package wsdydeni.widget.library.base

sealed class ViewModelEffect

object DialogShowEffect : ViewModelEffect()

class DialogDismissEffect(val isCancel: Boolean = false) : ViewModelEffect()

class ToastShowEffect(val text: String) : ViewModelEffect()