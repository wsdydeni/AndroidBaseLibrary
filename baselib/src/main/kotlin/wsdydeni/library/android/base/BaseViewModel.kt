package wsdydeni.library.android.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow


/**
 * how to use flow
 *
 * [Channel.receiveAsFlow] “外冷内热” 不会丢失且一对一订阅，只执行一次
 *
 * [MutableSharedFlow.asSharedFlow] 具有时效性且支持一对多订阅
 *
 * [MutableStateFlow.asStateFlow] 初始值不能为空 长期状态
 *
 * how to collect flow
 *
 * 对[Lifecycle.repeatOnLifecycle]封装的简化方法,不能够在协程作用域内调用
 *
 * 默认行为在ON_START事件开始订阅/重新订阅，在ON_STOP事件取消订阅
 *
 * [Fragment.launchAndRepeatWithViewLifecycle]
 *
 * [FragmentActivity.launchAndRepeatWithViewLifecycle]
 */
open class BaseViewModel : ViewModel() {

    private val _effect = Channel<ViewModelEffect>()
    val effect = _effect.receiveAsFlow()

    private suspend fun setEffect(builder: () -> ViewModelEffect) {
        val newEffect = builder()
        _effect.send(newEffect)
    }

    suspend fun showToast(text : String) {
        setEffect { ToastEffect(text) }
    }

    suspend fun showToastDebug(text: String) {
        setEffect { ToastDebugEffect(text) }
    }

    suspend fun showDialog() {
        setEffect { DialogShowEffect }
    }

    suspend fun dismissDialog() {
        setEffect { DialogDismissEffect }
    }

}

