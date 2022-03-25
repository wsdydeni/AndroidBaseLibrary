package wsdydeni.library.android.request

sealed class NetResponse<out T> {

    data class Success<T>(private val value: BaseResponse<T>) : NetResponse<T>() {
        val data = value.getResponseData()
        override fun toString() = "[NetResponse.Success](data=$data)"
    }

    data class Failure<T>(private val response: BaseResponse<T>) : NetResponse<T>() {
        val errorCode = response.getResponseCode()
        val errorMsg = response.getResponseMsg()
        override fun toString(): String =
            "[NetResponse.Failure] (errorCode=$errorCode errorMsg=$errorMsg)"
    }

}