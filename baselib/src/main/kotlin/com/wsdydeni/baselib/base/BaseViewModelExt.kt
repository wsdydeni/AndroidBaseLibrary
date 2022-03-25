package com.wsdydeni.baselib.base

/**
 * 必须执行且只能执行一次的事件
 */
sealed class ViewModelEffect

data class ToastDebugEffect(val text: String) : ViewModelEffect()

data class ToastEffect(val text: String) : ViewModelEffect()

object DialogShowEffect : ViewModelEffect()

object DialogDismissEffect : ViewModelEffect()