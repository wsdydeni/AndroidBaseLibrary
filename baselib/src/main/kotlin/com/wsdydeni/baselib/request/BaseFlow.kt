package com.wsdydeni.baselib.request

import com.wsdydeni.baselib.base.BaseViewModel
import com.wsdydeni.baselib.utils.another.LogUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


suspend fun <T> BaseViewModel.associatedView(
    request: suspend () -> BaseResponse<T>,
    onError: suspend (errorMsg: String,errorCode: String) -> Unit,
    isShowDialog: Boolean = true
): Flow<T> {
    return flow {
        if(isShowDialog) showDialog()
        executeResponse(request()).suspendOnSuccess {
            if(isShowDialog) dismissDialog()
            emit(data)
        }.suspendOnFailure {
            if(isShowDialog) dismissDialog()
            onError(errorMsg, errorCode)
            LogUtil.e("associatedView onFailure: ${message()}" )
        }
    }.catch {
        LogUtil.e("associatedView catch: ${it.message}")
        if(isShowDialog) dismissDialog()
        onError(it.message ?: "网络请求异常", "")
    }
}
