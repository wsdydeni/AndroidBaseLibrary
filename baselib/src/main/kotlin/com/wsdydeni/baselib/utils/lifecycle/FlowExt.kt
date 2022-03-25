package com.wsdydeni.baselib.utils.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/** 解决Flow Collect在ON_STOP时只是挂起而不是销毁 */
class FlowObserver<T> (
    lifecycleOwner: LifecycleOwner,
    private val flow: Flow<T>,
    private val collector: suspend (T) -> Unit
) {

    private var job: Job? = null

    init {
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver {
                source: LifecycleOwner, event: Lifecycle.Event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    job = source.lifecycleScope.launch {
                        flow.collect { collector(it) }
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    job?.cancel()
                    job = null
                }
                else -> { }
            }
        })
    }
}

@Deprecated(
    message = "不建议使用这个方法，仅限于单次事件监听",
    replaceWith = ReplaceWith(
        expression = "launchAndRepeatWithViewLifecycle {"
                + "\n"
                + "this.collect{ "
                + "\n"
                + "collector() "
                + "\n"
                + "}"
                + "\n"
                + "}"
    ),
    level = DeprecationLevel.WARNING
)
inline fun <reified T> Flow<T>.observeOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) = FlowObserver(lifecycleOwner, this, collector)

@Deprecated(
    message = "不建议使用这个方法，仅限于单次事件监听",
    replaceWith = ReplaceWith(
        expression = "launchAndRepeatWithViewLifecycle {"
                + "\n"
                + "this.collect{ }"
                + "\n"
                + "}"
    ),
    level = DeprecationLevel.WARNING
)
inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this, {})