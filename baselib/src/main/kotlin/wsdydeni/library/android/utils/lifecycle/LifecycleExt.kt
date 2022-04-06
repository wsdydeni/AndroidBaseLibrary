package wsdydeni.library.android.utils.lifecycle

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * 更加简洁的语法封装
 *
 * 在 [Fragment.getViewLifecycleOwner] 达到 [minActiveState] 时，启动一个新的协程并重复 [block] 并在 [Lifecycle.Event.ON_DESTROY] 时，取消协程。
 *
 * 警告：不要在其他 CoroutineScope 中调用本函数！！！
 *
 * 如果在 CoroutineScope 中调用本函数，会隐式持有外部作用域
 *
 * 但是并不会保留外部作用域的上下文，也就是无法实现结构化并发
 *
 * @param newCoroutineContext 协程上下文 默认 [Dispatchers.Main]
 * @param minActiveState 需要达到的最小生命周期状态 默认 [Lifecycle.State.STARTED]
 * @param block 协程代码块
 */
inline fun Fragment.launchAndRepeatWithViewLifecycle(
    newCoroutineContext: CoroutineContext = Dispatchers.Main,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch(newCoroutineContext) {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    launchAndRepeatWithViewLifecycle(Dispatchers.Main,minActiveState,block)
}

/**
 * 更加简洁的语法封装
 *
 * 在 [FragmentActivity.getLifecycle] 达到 [minActiveState] 时，启动一个新的协程并重复 [block] 并在 [Lifecycle.Event.ON_DESTROY] 时，取消协程。
 *
 * 警告：不要在其他 CoroutineScope 中调用本函数！！！
 *
 * 如果在 CoroutineScope 中调用本函数，会隐式持有外部作用域
 *
 * 但是并不会保留外部作用域的上下文，也就是无法实现结构化并发
 *
 * @param newCoroutineContext 协程上下文 默认 [Dispatchers.Main]
 * @param minActiveState 需要达到的最小生命周期状态 默认 [Lifecycle.State.STARTED]
 * @param block 协程代码块
 */
inline fun FragmentActivity.launchAndRepeatWithViewLifecycle(
    newCoroutineContext: CoroutineContext = Dispatchers.Main,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch(newCoroutineContext) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

inline fun FragmentActivity.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    launchAndRepeatWithViewLifecycle(Dispatchers.Main,minActiveState,block)
}