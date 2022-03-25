package wsdydeni.library.android.utils.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.DynamicDrawableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import wsdydeni.library.android.utils.display.PixelUtil

/**
 * https://segmentfault.com/a/1190000040344725
 * 来源于上面的链接，做了一些细节改动和优化。
 */

class SpannableExtensions(@DrawableRes val selectDrawableId: Int, @DrawableRes val unSelectDrawableId: Int) {

    private var isChecked = false

    fun isChecked(): Boolean {
        return isChecked
    }

    fun setChecked(checked: Boolean) {
        isChecked = checked
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }

    interface OnImageClickListener {
        fun onSelect(@DrawableRes selectDrawableId: Int)
        fun onUnSelect(@DrawableRes unSelectDrawableId: Int)
    }

    class CustomImageSpan(drawable: Drawable, verticalAlignment: Int) : ImageSpan(drawable, verticalAlignment) {
        override fun draw(
            canvas: Canvas, text: CharSequence, start: Int, end: Int,
            x: Float, top: Int, y: Int, bottom: Int, paint: Paint
        ) {
            val drawable = drawable
            canvas.save()
            //获取画笔的文字绘制时的具体测量数据
            val fm = paint.fontMetricsInt
            var transY = bottom - drawable.bounds.bottom
            if (mVerticalAlignment == ALIGN_BASELINE) {
                transY -= fm.descent
            } else if (mVerticalAlignment == ALIGN_CENTER) { //自定义居中对齐
                //与文字的中间线对齐（这种方式不论是否设置行间距都能保障文字的中间线和图片的中间线是对齐的）
                // y+ascent得到文字内容的顶部坐标，y+descent得到文字的底部坐标，（顶部坐标+底部坐标）/2=文字内容中间线坐标
                transY = (y + fm.descent + (y + fm.ascent)) / 2 - drawable.bounds.bottom / 2
            }
            canvas.translate(x, transY.toFloat())
            drawable.draw(canvas)
            canvas.restore()
        }
    }

    // Object匿名内部类实现更改为静态内部类实现(解决ClickableSpan内存泄漏问题)
    class CustomClickableSpan constructor(
        private val update: ((textPaint: TextPaint) -> Unit),
        private val click: (widget: View) -> Unit
    ): ClickableSpan() {
        override fun onClick(widget: View) {
            click.invoke(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            update.invoke(ds)
        }
    }

}

fun SpannableStringBuilder.append(text: CharSequence,color: Int) : SpannableStringBuilder {
    val start: Int = length
    append(text)
    val end: Int = length
    setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun SpannableStringBuilder.replace(text: CharSequence,color: Int,vararg replaces: String) : SpannableStringBuilder {
    append(text)
    for (replace in replaces) {
        val start = text.toString().indexOf(replace)
        if (start >= 0) {
            val end = start + replace.length
            setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    return this
}

fun SpannableStringBuilder.click(text: CharSequence,color: Int,onClickListener: SpannableExtensions.OnClickListener,vararg clickTexts: String) : SpannableStringBuilder {
    append(text)
    for (i in clickTexts.indices) {
        val clickText = clickTexts[i]
        val start = text.toString().indexOf(clickText)
        if (start >= 0) {
            val end = start + clickText.length
            setSpan(
                SpannableExtensions.CustomClickableSpan({
                    it.color = color
                    it.isUnderlineText = false }
                ) { onClickListener.onClick(i) }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    return this
}

fun SpannableStringBuilder.setImageSpan(context: Context,drawable: Drawable) {
    drawable.setBounds(0, 0, PixelUtil.dip2px(context, 18f), PixelUtil.dip2px(context, 18f))
    val verticalAlignment: Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            DynamicDrawableSpan.ALIGN_CENTER  //居中对齐
        } else {
            DynamicDrawableSpan.ALIGN_BASELINE
        }
    val imageSpan = SpannableExtensions.CustomImageSpan(drawable, verticalAlignment)
    setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}

fun SpannableStringBuilder.select(
    spannableExtensions: SpannableExtensions,
    onImageClickListener: SpannableExtensions.OnImageClickListener) : SpannableStringBuilder
{
    setSpan(SpannableExtensions.CustomClickableSpan({
        it.color = Color.WHITE
        it.isUnderlineText = false
    }){
        spannableExtensions.setChecked(!spannableExtensions.isChecked())
        if (spannableExtensions.isChecked()) {
            onImageClickListener.onSelect(spannableExtensions.selectDrawableId)
        } else {
            onImageClickListener.onUnSelect(spannableExtensions.unSelectDrawableId)
        }
    },0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun SpannableStringBuilder.clickInto(textView: TextView) {
    textView.movementMethod = LinkMovementMethod.getInstance() //设置可点击状态
    textView.highlightColor = Color.TRANSPARENT //设置点击后的颜色为透明
    textView.text = this
}