package com.wsdydeni.baselib.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.wsdydeni.baselib.utils.immersion.fitNavigationBar
import com.wsdydeni.baselib.utils.immersion.immersionStatusBar
import com.wsdydeni.baselib.utils.immersion.immersiveNavigationBar
import com.wsdydeni.baselib.utils.immersion.setLightNavigationBar
import com.wsdydeni.baselib.utils.keyboard.addKeyboardMonitor
import com.wsdydeni.baselib.utils.lifecycle.repeatOnLifecycle
import com.wsdydeni.baselib.view.LoadingDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


open class BaseActivity : AppCompatActivity {

    constructor() : super()

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    private val baseHandler = Handler(Looper.getMainLooper())

    var loadingDialog: LoadingDialog? = null

    private var lastClickTime: Long = 0L

    open var openImmersion = true

    open var openKeyboardMonitor = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if(openImmersion) {
            immersionStatusBar(isShowSystemStatusBar = false, isLightStatusBar = true)
            immersiveNavigationBar()
            fitNavigationBar(true)
            setLightNavigationBar(true)
        }
        if(openKeyboardMonitor) {
            addKeyboardMonitor()
        }
        super.onCreate(savedInstanceState)
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT.also { requestedOrientation = it }
        initView()
    }

    override fun onStart() {
        super.onStart()
        initData()
    }

    /**
     * 防止多次点击
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(ev?.action == MotionEvent.ACTION_DOWN) {
            if(System.currentTimeMillis() - lastClickTime < 500L) {
                return true
            }
            lastClickTime = System.currentTimeMillis()
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 监听单次事件
     *
     * @param viewModel 基类ViewModel
     */
    fun observerEffect(viewModel: BaseViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect {
                    when(it) {
                        is ToastEffect ->
                            Toast.makeText(this@BaseActivity, it.text, Toast.LENGTH_SHORT).show()
                        is ToastDebugEffect ->
                            Toast.makeText(this@BaseActivity, it.text, Toast.LENGTH_SHORT).show()
                        is DialogShowEffect -> {
                            if(!isFinishing && !isDestroyed) {
                                baseHandler.post {
                                    if(!isFinishing && !isDestroyed) {
                                        loadingDialog?.let { dialog ->
                                            if(!dialog.isShowing) dialog.show()
                                        }
                                    }
                                }
                            }
                        }
                        is DialogDismissEffect -> {
                            loadingDialog?.let { dialog ->
                                if(dialog.isShowing) {
                                    dialog.dismissNormally()
                                }
                                loadingDialog = null
                            }
                        }
                    }
                }
            }
        }
    }

    open fun initView() {}
    open fun initData() {}

}