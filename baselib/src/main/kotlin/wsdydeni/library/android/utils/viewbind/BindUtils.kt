package wsdydeni.library.android.utils.viewbind

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.RestrictTo
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment


@Suppress("NOTHING_TO_INLINE")
@RestrictTo(RestrictTo.Scope.LIBRARY)
inline fun <V : View> View.requireViewByIdCompat(@IdRes id: Int): V {
    return ViewCompat.requireViewById(this, id)
}


@Suppress("NOTHING_TO_INLINE")
@RestrictTo(RestrictTo.Scope.LIBRARY)
inline fun <V: View> Activity.requireViewByIdCompat(@IdRes id: Int): V {
    return ActivityCompat.requireViewById(this,id)
}


/**
 * 从目标活动获取根视图
 *
 * @param activity 目标活动
 * @return 根视图
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun findRootView(activity: Activity): View {
    val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
    checkNotNull(contentView) { "Activity has no content view" }
    return when (contentView.childCount) {
        1 -> contentView.getChildAt(0)
        0 -> error("Content view has no children. Provide root view explicitly")
        else -> error("More than one child view found in Activity content view")
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun DialogFragment.getRootView(viewBindingRootId: Int): View {
    val dialog = checkNotNull(dialog) {
        "DialogFragment doesn't have dialog. Use viewBinding delegate after onCreateDialog"
    }
    val window = checkNotNull(dialog.window) { "Fragment's Dialog has no window" }
    return with(window.decorView) { if (viewBindingRootId != 0) requireViewByIdCompat(viewBindingRootId) else this }
}
