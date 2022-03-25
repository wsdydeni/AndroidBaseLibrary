package com.wsdydeni.baselib.request


abstract class BaseResponse<T> {

    abstract fun isSuccess(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): String

    abstract fun getResponseMsg(): String

}