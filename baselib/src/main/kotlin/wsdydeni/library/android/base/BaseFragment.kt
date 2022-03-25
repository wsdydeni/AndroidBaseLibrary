package wsdydeni.library.android.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment


open class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    private var isInit = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        restoreState(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if(!isInit&&!isHidden) {
            initData()
            isInit = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isInit = false
    }

    open fun restoreState(savedInstanceState: Bundle?) {}
    open fun initView() {}
    open fun initData() {}
}