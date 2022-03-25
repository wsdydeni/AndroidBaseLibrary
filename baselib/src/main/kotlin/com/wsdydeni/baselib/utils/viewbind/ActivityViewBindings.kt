package com.wsdydeni.baselib.utils.viewbind

import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.annotation.RestrictTo
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding


@RestrictTo(RestrictTo.Scope.LIBRARY)
private class ActivityViewBindingProperty<in A: ComponentActivity,out T: ViewBinding>(viewBinder: (A) -> T)
    : LifecycleViewBindingProperty<A, T>(viewBinder)
{
    override fun getLifecycleOwner(thisRef: A): LifecycleOwner {
        return thisRef
    }
}


@JvmName("viewBindingActivity")
public fun <A : ComponentActivity,T : ViewBinding> viewBinding(viewBinder: (A) -> T)
    : ViewBindingProperty<A, T> = ActivityViewBindingProperty(viewBinder)


/**
 * 通过Activity获取根视图生成对应的ViewBinding对象
 *
 * @param A 目标Activity
 * @param T 目标Activity的ViewBinding
 * @param viewBindingFactory 通过Activity根视图生成对应的ViewBinding
 * @param viewProvider 接收一个Activity实例获取Activity的根视图
 * @return ViewBinding的代理对象
 */
@JvmName("viewBindingActivity")
public inline fun <A : ComponentActivity,T : ViewBinding> viewBinding(
    crossinline viewBindingFactory: (View) -> T,
    crossinline viewProvider: (A) -> View = ::findRootView
) : ViewBindingProperty<A, T> {
    return viewBinding { activity -> viewBindingFactory(viewProvider(activity)) }
}


/**
 * 和上面思路一致（Activity实例换成了Activity根视图）
 */
@JvmName("viewBindingActivity")
public inline fun <T : ViewBinding> viewBinding(
    crossinline viewBindingFactory: (View) -> T,
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<ComponentActivity, T> {
    return viewBinding { activity -> viewBindingFactory(activity.requireViewByIdCompat(viewBindingRootId)) }
}

