package wsdydeni.widget.library

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import wsdydeni.library.android.utils.another.LogUtil
import wsdydeni.library.android_network.network.BaseResponse
import wsdydeni.library.android_network.network.associatedView
import wsdydeni.widget.library.base.BaseViewModel

class MainViewModel : BaseViewModel() {

  class TestResponse : BaseResponse<Int>() {
    override fun getResponseCode(): String = "200"
    override fun getResponseData(): Int = 20
    override fun getResponseMsg(): String = "success"
    override fun isSuccess(): Boolean = true
  }

  private suspend fun request1() : TestResponse {
    return TestResponse()
  }

  fun send() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
      coroutineContext[Job]?.cancel()
      LogUtil.d("MainViewModel coroutineExceptionHandler send" + throwable.message)
    }
    viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
      associatedView(suspend { request1() },{
        // show dialog
      },{
        // close dialog
      }) { errorMsg, errorCode ->
        // close dialog
        // show error message
      }
    }
  }
}