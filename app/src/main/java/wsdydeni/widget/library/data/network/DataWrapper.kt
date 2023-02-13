package wsdydeni.widget.library.data.network

import com.google.gson.annotations.SerializedName
import wsdydeni.library.android_network.network.BaseResponse
import java.io.Serializable

data class DataWrapper<T>(
  @SerializedName("data") var data: T,
  @SerializedName("errorMsg") var message : String,
  @SerializedName("errorCode") var code : Int
) : Serializable, BaseResponse<T>() {

  override fun getResponseCode(): String = code.toString()

  override fun getResponseData(): T = data

  override fun getResponseMsg(): String = message

  override fun isSuccess(): Boolean = code == 0

}