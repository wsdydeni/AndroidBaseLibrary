package wsdydeni.library.android.utils.another

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

inline fun <reified T> noOpDelegate() : T {
  val javaClass = T::class.java
  return Proxy.newProxyInstance(javaClass.classLoader, arrayOf(javaClass),noOpHandler) as T
}

val noOpHandler = InvocationHandler { _, _, _ -> }