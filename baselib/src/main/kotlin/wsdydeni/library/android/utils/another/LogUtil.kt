package wsdydeni.library.android.utils.another

import android.util.Log
import java.util.Formatter

/**
 * link: https://www.jianshu.com/p/22fd7999107f
 */
object LogUtil {
    private const val MIN_STACK_OFFSET = 3

    private var defaultTag = "LogUtil"

    private const val V = Log.VERBOSE
    private const val D = Log.DEBUG
    private const val I = Log.INFO
    private const val W = Log.WARN
    private const val E = Log.ERROR

    private const val TOP_BORDER = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════"
    private const val LEFT_BORDER = "║ "
    private const val BOTTOM_BORDER = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════"
    private const val MAX_LEN = 1000
    private var open = true

    fun init(isLogEnable: Boolean) {
        open = isLogEnable
        if(!open) {
            closeLog()
        }
    }

    private fun processTagAndHead(): String {
        val elements = Thread.currentThread().stackTrace
        val offset = getStackOffset(elements)
        val targetElement = elements[offset]
        val head = Formatter()
            .format("%s [%s(%s:%d)]",
                "In Thread: " + Thread.currentThread().name,
                targetElement.methodName,
                targetElement.fileName,
                targetElement.lineNumber)

        return head.toString()
    }

    private fun processMsgBody(msg: String, flag: Int, tag: String = defaultTag) {
        printTop(flag, tag)
        // 首先打印调用信息
        printLog(flag, tag)

        val lineCount = msg.length / MAX_LEN
        if (lineCount == 0) {
            printLog(flag, tag, msg)
        } else {
            var index = 0
            var i = 0
            while (true) {
                printLog(flag, tag, msg.substring(index, index + MAX_LEN))
                index += MAX_LEN
                if ((++i) >= lineCount)
                    break
            }
        }
        printBottom(flag, tag)
    }

    private fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != LogUtil::class.java.name) {
                return i
            }
            i++
        }
        return 2
    }

    /* 虽然 kotlin 有默认值这种操作，但是 Log.i(tag,msg) 这种比较符合平时的操作，所以还是提供类似的重载，
     * 而非 LogUtil.i(msg: String,tag: String = defaultTAG) 这种带默认值参数的方法 */

    fun v(msg: String) {
        v(defaultTag, msg)
    }

    fun i(msg: String) {
        i(defaultTag, msg)
    }

    fun d(msg: String) {
        d(defaultTag, msg)
    }

    fun w(msg: String) {
        w(defaultTag, msg)
    }

    fun e(msg: String) {
        e(defaultTag, msg)
    }

    private fun v(tag: String, msg: String) {
        if (!open) {
            return
        }
        processMsgBody(msg, V, tag)
    }

    private fun i(tag: String, msg: String) {
        if (!open) {
            return
        }
        processMsgBody(msg, I, tag)
    }

    private fun d(tag: String, msg: String) {
        if (!open) {
            return
        }
        processMsgBody(msg, D, tag)
    }

    private fun w(tag: String, msg: String) {
        if (!open) {
            return
        }
        processMsgBody(msg, W, tag)
    }

    private fun e(tag: String, msg: String) {
        if (!open) {
            return
        }
        processMsgBody(msg, E, tag)
    }

    private fun printLog(flag: Int, tag: String, msg: String = processTagAndHead()) {
        Log.println(flag, tag, LEFT_BORDER + msg)
    }

    private fun printBottom(flag: Int, tag: String) {
        Log.println(flag, tag, BOTTOM_BORDER)
    }

    private fun printTop(flag: Int, tag: String) {
        Log.println(flag, tag, TOP_BORDER)
    }

    private fun closeLog() {
        open = false
    }


}