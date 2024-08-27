package dev.deftu.componency.color

public class StaticColor(
    hue: Float,
    saturation: Float,
    brightness: Float,
    alpha: Int = 255,
) : Color(alpha, hue, saturation, brightness) {

    public constructor(
        values: Triple<Float, Float, Float>,
        alpha: Int = 255
    ) : this(values.first, values.second, values.third, alpha)

    public constructor(
        values: FloatArray,
        alpha: Int = 255
    ) : this(values[0], values[1], values[2], alpha)

    public constructor(
        red: Int,
        green: Int,
        blue: Int,
        alpha: Int = 255
    ) : this(RGBtoHSB(red, green, blue), alpha)

    override fun toString(): String {
        return "StaticColor(hue=$hue, saturation=$saturation, brightness=$brightness, rgb=$rgb, argb=$argb, rgba=$rgba, alpha=$alpha)"
    }

}
