package wsdydeni.library.android.base

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
import kotlinx.coroutines.launch
import wsdydeni.library.android.utils.immersion.fitNavigationBar
import wsdydeni.library.android.utils.immersion.immersionStatusBar
import wsdydeni.library.android.utils.immersion.immersiveNavigationBar
import wsdydeni.library.android.utils.immersion.setLightNavigationBar
import wsdydeni.library.android.utils.keyboard.addKeyboardMonitor
import wsdydeni.library.android.utils.lifecycle.repeatOnLifecycle
import wsdydeni.library.android.view.LoadingDialog


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
    fun observerEffect(viewModel: wsdydeni.library.android.base.BaseViewModel) {
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