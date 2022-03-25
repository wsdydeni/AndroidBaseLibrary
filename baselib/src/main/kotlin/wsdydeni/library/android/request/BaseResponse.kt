package wsdydeni.library.android.request


abstract class BaseResponse<T> {

    abstract fun isSuccess(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): String

    abstract fun getResponseMsg(): String

}