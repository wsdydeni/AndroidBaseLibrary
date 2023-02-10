package wsdydeni.widget.library.viewbind

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.RestrictTo
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.viewbinding.ViewBinding


@PublishedApi
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class ViewGroupViewBindingProperty<in V : ViewGroup, out T : ViewBinding>(
    viewBinder: (V) -> T) : LifecycleViewBindingProperty<V, T>(viewBinder)
{
    override fun getLifecycleOwner(thisRef: V): LifecycleOwner {
        return checkNotNull(ViewTreeLifecycleOwner.get(thisRef)) {
            "Fragment doesn't have view associated with it or the view has been destroyed"
        }
    }
}


inline fun <T : ViewBinding> ViewGroup.viewBinding(
    crossinline vbFactory: (ViewGroup) -> T
) : ViewBindingProperty<ViewGroup, T>
{
    return viewBinding(lifecycleAware = false, vbFactory = vbFactory)
}


inline fun <T : ViewBinding> ViewGroup.viewBinding(
    lifecycleAware: Boolean, crossinline vbFactory: (ViewGroup) -> T)
    : ViewBindingProperty<ViewGroup, T>
{
    return if (lifecycleAware) {
        ViewGroupViewBindingProperty { viewGroup -> vbFactory(viewGroup) }
    } else {
        LazyViewBindingProperty { viewGroup -> vbFactory(viewGroup) }
    }
}


inline fun <T : ViewBinding> ViewGroup.viewBinding(
    @IdRes viewBindingRootId: Int, crossinline vbFactory: (View) -> T
) : ViewBindingProperty<ViewGroup, T>
{
    return viewBinding(viewBindingRootId, lifecycleAware = false, vbFactory = vbFactory)
}


inline fun <T : ViewBinding> ViewGroup.viewBinding(
    @IdRes viewBindingRootId: Int, lifecycleAware: Boolean, crossinline vbFactory: (View) -> T)
    : ViewBindingProperty<ViewGroup, T>
{
    if (lifecycleAware) return ViewGroupViewBindingProperty { viewGroup -> vbFactory(viewGroup) }
    return LazyViewBindingProperty { viewGroup: ViewGroup ->
        vbFactory(viewGroup.requireViewByIdCompat(viewBindingRootId))
    }
}