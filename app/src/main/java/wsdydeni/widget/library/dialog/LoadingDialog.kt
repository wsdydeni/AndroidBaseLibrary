package wsdydeni.widget.library.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import kotlinx.coroutines.Job
import wsdydeni.widget.library.R
import wsdydeni.widget.library.databinding.DialogLoadingBinding
import java.lang.ref.WeakReference

class LoadingDialog(context: Context,job: Job? = null) : Dialog(context, R.style.TransparentDialog) {

    private val jobRef by lazy {
        WeakReference(job)
    }

    private fun getJob() : Job? {
        return jobRef.get()
    }

    private var dialogLoadingBinding: DialogLoadingBinding

    init {
        window?.let {
            it.setGravity(Gravity.CENTER)
            it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            it.setWindowAnimations(R.style.PopWindowAnimStyle)
        }
        dialogLoadingBinding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        setContentView(dialogLoadingBinding.root)
        setOnCancelListener {
            getJob()?.cancel()
        }
    }

    fun setLoadingText(title: String?) : LoadingDialog {
        title?.let { dialogLoadingBinding.tipTextView.text = it  }
        return this
    }

    /**
     * 弹窗显示时启动Job
     */
    override fun show() {
        super.show()
        getJob()?.start()
    }

    /**
     * 是否在异常关闭时取消Job
     */
    fun dismissCancelJob(isCancel: Boolean) {
        if(isCancel) getJob()?.cancel()
        super.dismiss()
    }

    /**
     * 是否可以从外部取消弹窗
     */
    fun isCanCancelByReturn(isReturnClose: Boolean) : LoadingDialog {
        if(isReturnClose) setCanceledOnTouchOutside(false) else setCancelable(false)
        return this
    }

}

