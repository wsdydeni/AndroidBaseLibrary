package wsdydeni.library.android.utils.display

import android.content.Context

class PixelUtil {
    companion object {
        @JvmStatic
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        @JvmStatic
        fun px2dp(context: Context, pxValue: Int): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }
    }
}