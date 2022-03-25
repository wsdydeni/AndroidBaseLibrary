package wsdydeni.library.android.utils.viewbind

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.annotation.RestrictTo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import wsdydeni.library.android.utils.another.LogUtil
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * ViewBinding只读代理对象属性
 *
 * @param R 目标对象
 * @param T 要代理的属性
 */
interface ViewBindingProperty<in R : Any, out T : ViewBinding> : ReadOnlyProperty<R, T> {
    @MainThread fun clear()
}


/**
 * 实现ViewBindingProperty
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
open class LazyViewBindingProperty<in R : Any, out T : ViewBinding>(protected val viewBinder: (R) -> T) :
    ViewBindingProperty<R, T> {

    private var viewBinding: Any? = null

    @Suppress("UNCHECKED_CAST")
    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        return viewBinding as? T ?: viewBinder(thisRef).also { viewBinding ->
            this.viewBinding = viewBinding
        }
    }

    @MainThread
    override fun clear() {
        viewBinding = null
    }
}


/**
 * 实现ViewBindingProperty同时获取调用者的Lifecycle组件实现生命周期同步
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
abstract class LifecycleViewBindingProperty<in R : Any, out T : ViewBinding>(private val viewBinder: (R) -> T) :
    ViewBindingProperty<R, T> {

    private var viewBinding: T? = null

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        viewBinding?.let { return it }

        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        val viewBinding = viewBinder(thisRef)
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            LogUtil.w("Access to viewBinding after Lifecycle is destroyed or hasn't created yet. "
                    + "The instance of viewBinding will be not cached.")
        } else {
            lifecycle.addObserver(ClearOnDestroyLifecycleObserver())
            this.viewBinding = viewBinding
        }
        return viewBinding
    }

    @MainThread
    override fun clear() {
        mainHandler.post { viewBinding = null }
    }

    private inner class ClearOnDestroyLifecycleObserver: LifecycleEventObserver {
        @MainThread
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if(event == Lifecycle.Event.ON_DESTROY) {
                clear()
            }
        }
    }

    private companion object {
        private val mainHandler = Handler(Looper.getMainLooper())
    }
}