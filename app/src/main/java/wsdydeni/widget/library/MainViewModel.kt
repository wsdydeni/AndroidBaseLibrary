package wsdydeni.widget.library

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import wsdydeni.library.android.utils.another.LogUtil
import wsdydeni.library.android_network.network.associatedView
import wsdydeni.widget.library.base.BaseViewModel
import wsdydeni.widget.library.base.DialogDismissEffect
import wsdydeni.widget.library.base.DialogShowEffect
import wsdydeni.widget.library.base.ToastShowEffect
import wsdydeni.widget.library.data.network.ArticleList
import wsdydeni.widget.library.data.network.WandroidService
import wsdydeni.widget.library.data.network.getApiService

class MainViewModel : BaseViewModel() {

  private val articleService by lazy { getApiService<WandroidService>() }

  private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
    viewModelScope.launch { setEffect { DialogDismissEffect(true) } }
  }

  private val _articleLists = MutableSharedFlow<ArticleList>()
  val articleList = _articleLists.asSharedFlow()

  fun getArticle() : Job {
    return viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler, start = CoroutineStart.LAZY) {
      associatedView(
        request = suspend { articleService.getArticleList() },
        onRequestSuccess = { delay(1000L)
          setEffect { DialogDismissEffect() } },
        onRequestError = { errorMsg, _ ->
          setEffect { DialogDismissEffect(true) }
          setEffect { ToastShowEffect(errorMsg) }
        }
      ).collect {
        _articleLists.emit(it)
      }
    }
  }
}