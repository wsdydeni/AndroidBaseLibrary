package wsdydeni.widget.library

import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import wsdydeni.library.android.base.BaseActivity
import wsdydeni.library.android.base.BaseActivityHandler
import wsdydeni.library.android.utils.another.LogUtil
import wsdydeni.library.android.utils.density.AutoDensity
import wsdydeni.library.android.utils.display.PixelUtil
import wsdydeni.library.android.utils.immersion.getNavBarHeight
import wsdydeni.library.android.utils.immersion.getStatusBarHeight
import wsdydeni.library.android.utils.immersion.showStatusBarView
import wsdydeni.library.android.utils.keyboard.addKeyboardMonitor
import wsdydeni.library.android.utils.lifecycle.launchAndRepeatWithViewLifecycle
import wsdydeni.library.android.utils.lifecycle.repeatOnLifecycle
import wsdydeni.widget.library.base.DialogDismissEffect
import wsdydeni.widget.library.base.DialogShowEffect
import wsdydeni.widget.library.base.ToastShowEffect
import wsdydeni.widget.library.dialog.LoadingDialog

class MainActivity : BaseActivity(R.layout.activity_main) {

    private var lastClickTime: Long = 0L

    override var isLightSystemBar = true

    override var immersiveNavigation = true

    override var immersionStatus = true

    private var loadingDialog : LoadingDialog? = null

    private val myHandler = BaseActivityHandlers(this)

    override fun initView() {
        addKeyboardMonitor { keyboardHeight, _, _ ->
            if(keyboardHeight > 1000) {
                return@addKeyboardMonitor
            }
            if(keyboardHeight == 0) {
                val inputLayout = findViewById<LinearLayout>(R.id.input_layout)
                inputLayout.translationY = 0f
                return@addKeyboardMonitor
            }
            val focusView: View? = window.decorView.findFocus()
            if (focusView != null && focusView is EditText) {
                val locations = IntArray(2)
                focusView.getLocationOnScreen(locations)
                val focusEtTop = locations[1]
                val focusViewHeight = focusView.measuredHeight
                val focusBottom = focusEtTop + focusViewHeight
                val generalHeight = AutoDensity.instance.getAutoDensityConfig().heightPixels
                if (keyboardHeight > generalHeight + getStatusBarHeight() - focusBottom) {
                    LogUtil.d("屏幕高度: ${AutoDensity.instance.getAutoDensityConfig().heightPixels}")
                    LogUtil.d("状态栏高度: ${getStatusBarHeight()}")
                    LogUtil.d("导航栏高度: ${getNavBarHeight()}")
                    LogUtil.d("输入框底部高度: $focusBottom")
                    LogUtil.d("输入框底部到屏幕底部的距离: ${generalHeight + getStatusBarHeight() - focusBottom}")
                    LogUtil.d("键盘高度: $keyboardHeight")
                    val difference = (generalHeight + getStatusBarHeight()) - (focusBottom + keyboardHeight)
                    LogUtil.d("布局纵向偏移量: $difference")
                    LogUtil.d("偏移量dp: ${PixelUtil.px2dp(this,difference)}")
                    val inputLayout = findViewById<LinearLayout>(R.id.input_layout)
                    inputLayout.translationY = difference.toFloat()
                }
            }
        }
        showStatusBarView(findViewById(R.id.fillStatusBarView), ContextCompat.getColor(this, R.color.color_6d7174))

        findViewById<TextView>(R.id.send_btn).setOnClickListener {
            lifecycleScope.launch {
                createLoadingDialog(job = mainViewModel.getArticle())
                mainViewModel.setEffect { DialogShowEffect }
            }
        }
    }

    private fun createLoadingDialog(text: String = "加载中", isCanCancelByReturn: Boolean = true, job: Job? = null) {
        this.loadingDialog = LoadingDialog(this,job)
            .setLoadingText(text)
            .isCanCancelByReturn(isCanCancelByReturn)
    }

    override fun initData() {
        observeEffect()
        launchAndRepeatWithViewLifecycle {
            mainViewModel.articleList.collect {
                mainViewModel.setEffect { ToastShowEffect("加载成功") }
            }
        }
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

    private val mainViewModel by lazy { MainViewModel() }

    private fun observeEffect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.effect.collect { effect ->
                    when(effect) {
                        is DialogShowEffect -> {
                            if(!isDestroyed && !isFinishing) {
                                myHandler.post {
                                    if(!isDestroyed && !isFinishing) {
                                        loadingDialog?.let {
                                            if(!it.isShowing) it.show()
                                        }
                                    }
                                }
                            }
                        }
                        is DialogDismissEffect -> {
                            loadingDialog?.let { dialog ->
                                dialog.dismissCancelJob(effect.isCancel)
                                loadingDialog = null
                            }
                        }
                        is ToastShowEffect -> {
                            ToastUtils.show(effect.text)
                        }
                    }
                }
            }
        }
    }

    companion object {
        class BaseActivityHandlers(activity: BaseActivity) : BaseActivityHandler(activity)
    }
}