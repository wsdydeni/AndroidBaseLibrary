package wsdydeni.library.android.utils.device

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Size
import androidx.core.app.ActivityCompat
import org.json.JSONException
import org.json.JSONObject
import wsdydeni.library.android.utils.network.getNetworkType
import wsdydeni.library.android.utils.network.isNetworkConnected


@SuppressLint("MissingPermission")
private fun Context.getImei(): String? {
    val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val isHavePermission = PackageManager.PERMISSION_GRANTED ==
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
    if (isHavePermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return telephonyManager.imei
        }
    }
    return null
}

@Throws(PackageManager.NameNotFoundException::class)
private fun Context.getAppVersion(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageManager.getPackageInfo(packageName, 0).longVersionCode.toInt()
    } else {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(packageName, 0).versionCode
    }
}

/**
 * 获取设备信息
 */
fun Context.getDeviceInfo() : String {
    val json = JSONObject()
    try {
        json.put("brand", Build.BRAND)
        json.put("model", Build.MODEL)
        json.put("imei", getImei())
        json.put("sdkVersion", Build.VERSION.SDK_INT)
        json.put("appVersion", getAppVersion())
        json.put("networkType", if(isNetworkConnected()) getNetworkType() else "UNKNOWN" )
    } catch (e: JSONException) {
        e.printStackTrace()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return json.toString()
}

fun Activity.getScreenSize(): Size {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Size(windowManager.currentWindowMetrics.bounds.width(), windowManager.currentWindowMetrics.bounds.height())
    } else {
        Size(windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
    }
}