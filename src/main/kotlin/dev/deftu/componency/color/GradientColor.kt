package dev.deftu.componency.color

public class GradientColor(
    public val firstColor: Color,
    public val secondColor: Color,
    public val type: GradientType,
    alpha: Int
) : Color(
    alpha = alpha
) {

    override val hue: Float
        get() = firstColor.hue

    override val saturation: Float
        get() = firstColor.saturation

    override val brightness: Float
        get() = firstColor.brightness

    override val isOpaque: Boolean
        get() = firstColor.isOpaque && secondColor.isOpaque

    override val isTranslucent: Boolean
        get() = firstColor.isTranslucent || secondColor.isTranslucent

    override val isTransparent: Boolean
        get() = firstColor.isTransparent && secondColor.isTransparent

    override val isGrayscale: Boolean
        get() = firstColor.isGrayscale && secondColor.isGrayscale

}
