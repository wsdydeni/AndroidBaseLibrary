package wsdydeni.widget.library.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

open class BaseViewModel : ViewModel() {
  private val _effect = Channel<ViewModelEffect>()
  val effect = _effect.receiveAsFlow()

  suspend fun setEffect(builder: () -> ViewModelEffect) {
    val newEffect = builder()
    _effect.send(newEffect)
  }
}