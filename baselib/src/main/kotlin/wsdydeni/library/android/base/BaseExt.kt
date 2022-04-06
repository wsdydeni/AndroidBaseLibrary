package wsdydeni.library.android.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import wsdydeni.library.android.utils.another.LogUtil
import wsdydeni.library.android.utils.network.isNetworkAvailable
import wsdydeni.library.android.view.LoadingDialog
import wsdydeni.library.android.utils.lifecycle.launchAndRepeatWithViewLifecycle


fun BaseActivity.createLoadingDialog(text: String = "加载中", isCanCancelByReturn: Boolean) {
    this.loadingDialog = LoadingDialog(this)
        .setLoadingText(text)
        .isCanCancelByReturn(isCanCancelByReturn)
}

/**
 * 网络请求同时显示弹窗
 *
 * 警告：不要在[FragmentActivity.launchAndRepeatWithViewLifecycle]使用，会重复请求
 *
 * 正确使用：在[LifecycleCoroutineScope.launch]中调用
 *
 * @param request 网络请求Job
 * @param text 弹窗显示的文字
 * @param isCanCancelByReturn 是否能够通过返回关闭弹窗
 * @return 请求是否发起成功
 */
suspend fun BaseActivity.requestWithDialog(request: Job, text: String = "加载中", isCanCancelByReturn: Boolean = true) : Boolean {
    if(!isNetworkAvailable()) {
        Toast.makeText(this, "无网络连接,请检查网络", Toast.LENGTH_SHORT).show()
        return false
    }
    createLoadingDialog(text,isCanCancelByReturn)
    this.loadingDialog?.setOnDismissCallBack(request) {}
    request.join()
    return true
}

/**
 * 网络请求同时显示弹窗
 *
 * 警告：不要在[FragmentActivity.launchAndRepeatWithViewLifecycle]使用，会重复请求
 *
 * 正确使用：在[LifecycleCoroutineScope.launch]中调用
 *
 * @param request 网络请求Job
 * @param text 弹窗显示的文字
 * @param isCanCancelByReturn 是否能够通过返回关闭弹窗
 * @param onDismissBack 弹窗关闭时回调
 * @return 请求是否发起成功
 */
suspend inline fun BaseActivity.requestWithDialog(request: Job, text: String = "加载中", isCanCancelByReturn: Boolean = true, crossinline onDismissBack: (() -> Unit)) : Boolean {
    if(!isNetworkAvailable()) {
        Toast.makeText(this, "无网络连接,请检查网络", Toast.LENGTH_SHORT).show()
        return false
    }
    createLoadingDialog(text,isCanCancelByReturn)
    this.loadingDialog?.setOnDismissCallBack(request) {
        onDismissBack.invoke()
    }
    request.join()
    return true
}

/**
 * 网络请求同时显示弹窗
 *
 * 警告：不要在[Fragment.launchAndRepeatWithViewLifecycle]使用，会重复请求
 *
 * 正确使用：使用[Fragment.getViewLifecycleOwner]调用[LifecycleCoroutineScope.launch]中使用
 *
 * @param job 网络请求Job
 * @param text 弹窗显示的文字
 * @param isCanCancelByReturn 是否能够通过返回关闭弹窗
 * @return 请求是否发起成功
 */
suspend fun BaseFragment.requestWithDialog(job: Job, text: String = "加载中",isCanCancelByReturn: Boolean = true) : Boolean {
    return if(null == activity) {
        LogUtil.e("BaseFragment#createLoadingDialog activity is null")
        false
    }else {
        (activity as BaseActivity).requestWithDialog(job, text,isCanCancelByReturn)
    }
}

/**
 * 网络请求同时显示弹窗
 *
 * 警告：不要在[Fragment.launchAndRepeatWithViewLifecycle]使用，会重复请求
 *
 * 正确使用：使用[Fragment.getViewLifecycleOwner]调用[LifecycleCoroutineScope.launch]中使用
 *
 * @param job 网络请求Job
 * @param text 弹窗显示的文字
 * @param isCanCancelByReturn 是否能够通过返回关闭弹窗
 * @param onDismissBack 弹窗关闭时回调
 * @return 请求是否发起成功
 */
suspend fun BaseFragment.requestWithDialog(job: Job, text: String = "加载中",isCanCancelByReturn: Boolean = true,onDismissBack: (() -> Unit)) : Boolean {
    return if(null == activity) {
        LogUtil.e("BaseFragment#createLoadingDialog activity is null")
        false
    }else {
        (activity as BaseActivity).requestWithDialog(job, text,isCanCancelByReturn,onDismissBack)
    }
}