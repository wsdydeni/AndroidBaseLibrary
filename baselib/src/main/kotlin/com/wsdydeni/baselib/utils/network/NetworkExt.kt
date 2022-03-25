package com.wsdydeni.baselib.utils.network

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat


@Suppress("DEPRECATION")
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isNetworkConnected(): Boolean {
    val isHavePermission = PackageManager.PERMISSION_GRANTED != ActivityCompat
        .checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
    if(isHavePermission) {
        return false
    }
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = manager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Context.getNetworkType(): String {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        val activeNetworkInfo = manager.activeNetworkInfo ?: return "UNKNOWN"
        return when (activeNetworkInfo.type) {
            ConnectivityManager.TYPE_WIFI -> "WIFI"
            ConnectivityManager.TYPE_MOBILE -> getMobileNetworkSubType()
            else -> "UNKNOWN"
        }
    } else {
        val activeNetwork = manager.activeNetwork
        val capabilities = manager.getNetworkCapabilities(activeNetwork)
        if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            return if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                "WIFI"
            } else {
                getMobileNetworkSubType()
            }
        }
        return "UNKNOWN"
    }
}


private fun Context.getMobileNetworkSubType(): String {
    val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        return "UNKNOWN"
    }
    @Suppress("DEPRECATION")
    return when (manager.networkType) {
        TelephonyManager.NETWORK_TYPE_GPRS,
        TelephonyManager.NETWORK_TYPE_EDGE,
        TelephonyManager.NETWORK_TYPE_CDMA,
        TelephonyManager.NETWORK_TYPE_1xRTT,
        TelephonyManager.NETWORK_TYPE_IDEN -> "2G"
        TelephonyManager.NETWORK_TYPE_UMTS,
        TelephonyManager.NETWORK_TYPE_EVDO_0,
        TelephonyManager.NETWORK_TYPE_EVDO_A,
        TelephonyManager.NETWORK_TYPE_HSDPA,
        TelephonyManager.NETWORK_TYPE_HSUPA,
        TelephonyManager.NETWORK_TYPE_HSPA,
        TelephonyManager.NETWORK_TYPE_EVDO_B,
        TelephonyManager.NETWORK_TYPE_EHRPD,
        TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
        TelephonyManager.NETWORK_TYPE_LTE -> "4G"
        TelephonyManager.NETWORK_TYPE_NR -> "5G"
        else -> "UNKNOWN"
    }
}

@Suppress("DEPRECATION")
fun Context.isNetworkAvailable() : Boolean {
    val connectivityManager : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    activeNetworkInfo?.let {
        if(it.isAvailable) return true
    }
    return false
}