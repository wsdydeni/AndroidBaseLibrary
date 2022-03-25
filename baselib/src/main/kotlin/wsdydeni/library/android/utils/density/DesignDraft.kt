package wsdydeni.library.android.utils.density

/**
 * @Date: 2020/4/20
 * @Author: hugo
 * @Description: 设计稿，包含尺寸，适配方向基准(基于width或者height)和字体缩放
 */
class DesignDraft @JvmOverloads constructor(
    /**
     * 是否以最短边为基准
     */
    val isBaseOnSmallestWidth: Boolean = true,
    /**
     * 设计稿的基准尺寸，如常用的:360dp、375dp
     */
    val designSize: Float,
    /**
     * 字体是否跟着变
     */
    val isEnableScaledFont: Boolean = true
)