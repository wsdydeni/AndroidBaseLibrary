package wsdydeni.library.android.base

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import wsdydeni.library.android.utils.another.noOpDelegate
import java.lang.ref.WeakReference



open class MyHandler<T>(private val instance: T? = null, looper: Looper = Looper.getMainLooper()) : Handler(looper) {
  private val ref by lazy {
    WeakReference(instance)
  }

  fun getInstance() : T? {
    return ref.get()
  }

  val lifecycleCallbacks = object : DefaultLifecycleObserver by noOpDelegate() {
    override fun onDestroy(owner: LifecycleOwner) {
      removeCallbacksAndMessages(null)
    }
  }
}

open class BaseActivityHandler(activity: BaseActivity) : MyHandler<BaseActivity>(activity) {
  init {
    activity.lifecycle.addObserver(lifecycleCallbacks)
  }
}

open class BaseFragmentHandler(fragment: BaseFragment) : MyHandler<BaseFragment>() {
  init {
    fragment.viewLifecycleOwner.lifecycle.addObserver(lifecycleCallbacks)
  }
}

