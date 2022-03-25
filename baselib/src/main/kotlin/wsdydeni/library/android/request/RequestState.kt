package wsdydeni.library.android.request

sealed class RequestState<out T> {
    object Loading : RequestState<Nothing>()
    data class Success<out T>(val data: T?) : RequestState<T>()
    data class Error(val errorMsg: String,val errorCode: Int) : RequestState<Nothing>()
}