package com.wsdydeni.baselib.utils.viewbind

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding


private class FragmentViewBindingProperty<in F: Fragment,out T: ViewBinding>(viewBinder: (F) -> T)
    : LifecycleViewBindingProperty<F, T>(viewBinder)
{

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        try {
            return thisRef.viewLifecycleOwner
        }catch (ignored: IllegalStateException) {
            error("Fragment doesn't have view associated with it or the view has been destroyed")
        }
    }

}


@JvmName("viewBindingFragment")
public fun <F: Fragment,T: ViewBinding> Fragment.viewBinding(
    viewBinder: (F: Fragment) -> T
): ViewBindingProperty<F, T>
{
    return if(this is DialogFragment) DialogFragmentViewBindingProperty(viewBinder)
    else FragmentViewBindingProperty(viewBinder)
}


@JvmName("viewBindingFragment")
public inline fun <F: Fragment,T: ViewBinding> Fragment.viewBinding(
    crossinline viewBindingFactory: (View) -> T,
    crossinline viewBinderProvider: (F: Fragment) -> View = Fragment::requireView
) : ViewBindingProperty<F, T>
{
    return viewBinding { fragment: Fragment -> viewBindingFactory(viewBinderProvider(fragment)) }
}


@JvmName("viewBindingFragment")
public inline fun <F: Fragment,T: ViewBinding> Fragment.viewBinding(
    crossinline viewBindingFactory: (View) -> T,
    @IdRes viewBindingRootId: Int
) : ViewBindingProperty<F, T>
{
    return if(this is DialogFragment) {
        dialogFragmentViewBinding { viewBindingFactory(getRootView(viewBindingRootId)) }
    }else {
        viewBinding(viewBindingFactory) { fragment ->
            fragment.requireView().requireViewByIdCompat(viewBindingRootId)
        }
    }
}