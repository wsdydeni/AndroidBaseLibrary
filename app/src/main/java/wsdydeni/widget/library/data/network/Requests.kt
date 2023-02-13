package wsdydeni.widget.library.data.network

import com.google.gson.Gson
import com.safframework.http.interceptor.AndroidLoggingInterceptor
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

inline fun <reified T> getApiService() : T {
  val retrofit = Retrofit.Builder()
    .baseUrl("https://www.wanandroid.com")
    .client(getOkhttp())
    .addConverterFactory(GsonConverterFactory.create(Gson()))
    .build()
  return retrofit.create(T::class.java)
}


fun getOkhttp() : OkHttpClient {
  return OkHttpClient.Builder()
    .connectTimeout(2000L,TimeUnit.MILLISECONDS)
    .readTimeout(2000L,TimeUnit.MILLISECONDS)
    .addNetworkInterceptor(AndroidLoggingInterceptor.build())
    .build()
}