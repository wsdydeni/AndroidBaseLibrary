package com.wsdydeni.baselib.request

fun <T> executeResponse(
    response: BaseResponse<T>
): NetResponse<T> {
    return if (response.isSuccess()) {
        NetResponse.Success(response)
    } else {
        NetResponse.Failure(response)
    }
}

suspend fun <T> NetResponse<T>.suspendOnSuccess(
    onResult: suspend NetResponse.Success<T>.() -> Unit
): NetResponse<T> {
    if (this is NetResponse.Success) {
        onResult(this)
    }
    return this
}

suspend fun <T> NetResponse<T>.suspendOnFailure(onResult: suspend NetResponse.Failure<*>.() -> Unit): NetResponse<T> {
    if (this is NetResponse.Failure<*>) {
        onResult(this)
    }
    return this
}

fun <T> NetResponse.Failure<T>.message(): String = toString()