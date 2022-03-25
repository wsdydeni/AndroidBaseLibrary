package com.wsdydeni.baselib.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.wsdydeni.baselib.R
import com.wsdydeni.baselib.databinding.DialogLoadingBinding
import kotlinx.coroutines.Job


class LoadingDialog(context: Context) : Dialog(context, R.style.TransparentDialog) {

    private var dialogLoadingBinding: DialogLoadingBinding

    private var isReturnExit = true

    fun getIsReturnExit() : Boolean {
        return isReturnExit
    }

    private fun setIsReturnExitToFalse() {
        this.isReturnExit = false
    }

    init {
        window?.let {
            it.setGravity(Gravity.CENTER)
            it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            it.setWindowAnimations(R.style.PopWindowAnimStyle)
        }
        dialogLoadingBinding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        setContentView(dialogLoadingBinding.root)
    }

    fun setLoadingText(title: String?) : LoadingDialog {
        title?.let { dialogLoadingBinding.tipTextView.text = it  }
        return this
    }

    inline fun setOnDismissCallBack(job: Job, crossinline onDismiss: () -> Unit) : LoadingDialog {
        setOnDismissListener {
            /**
             * 非正常关闭 取消请求 回调[onDismiss]
             */
            if(getIsReturnExit()) {
                job.cancel()
                onDismiss()
            }
        }
        return this
    }

    /**
     * 请求成功正常关闭
     */
    fun dismissNormally() {
        setIsReturnExitToFalse()
        super.dismiss()
    }

    fun isCanCancelByReturn(isCan: Boolean) : LoadingDialog {
        if(isCan) setCanceledOnTouchOutside(false) else setCancelable(false)
        return this
    }

}

