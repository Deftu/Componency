package dev.deftu.componency.color

import kotlin.math.floor

public abstract class Color(
    public val alpha: Int,
    hue: Float = 0f,
    saturation: Float = 0f,
    brightness: Float = 0f
) {

    public companion object {

        @JvmField
        public val TRANSPARENT: Color = rgba(red = 0, green = 0, blue = 0, alpha = 0)

        @JvmField
        public val WHITE: Color = rgb(red = 255, green = 255, blue = 255)

        @JvmField
        public val LIGHT_GRAY: Color = rgb(red = 192, green = 192, blue = 192)

        @JvmField
        public val GRAY: Color = rgb(red = 128, green = 128, blue = 128)

        @JvmField
        public val DARK_GRAY: Color = rgb(red = 64, green = 64, blue = 64)

        @JvmField
        public val BLACK: Color = rgb(red = 0, green = 0, blue = 0)

        @JvmField
        public val RED: Color = rgb(red = 255, green = 0, blue = 0)

        @JvmField
        public val PINK: Color = rgb(red = 255, green = 175, blue = 175)

        @JvmField
        public val ORANGE: Color = rgb(red = 255, green = 200, blue = 0)

        @JvmField
        public val YELLOW: Color = rgb(red = 255, green = 255, blue = 0)

        @JvmField
        public val GREEN: Color = rgb(red = 0, green = 255, blue = 0)

        @JvmField
        public val MAGENTA: Color = rgb(red = 255, green = 0, blue = 255)

        @JvmField
        public val CYAN: Color = rgb(red = 0, green = 255, blue = 255)

        @JvmField
        public val BLUE: Color = rgb(red = 0, green = 0, blue = 255)

        @JvmStatic
        public fun rgba(red: Int, green: Int, blue: Int, alpha: Int): Color {
            return StaticColor(red = red, green = green, blue = blue, alpha = alpha)
        }

        @JvmStatic
        public fun rgb(red: Int, green: Int, blue: Int): Color {
            return StaticColor(red = red, green = green, blue = blue)
        }

        @JvmStatic
        public fun argb(alpha: Int, red: Int, green: Int, blue: Int): Color {
            return StaticColor(red = red, green = green, blue = blue, alpha = alpha)
        }

        @JvmStatic
        @JvmOverloads
        public fun hsb(hue: Float, saturation: Float, brightness: Float, alpha: Int = 255): Color {
            return StaticColor(hue = hue, saturation = saturation, brightness = brightness, alpha = alpha)
        }

        @JvmStatic
        @JvmOverloads
        public fun gradient(first: Color, second: Color, type: GradientType, alpha: Int = 255): GradientColor {
            return GradientColor(first, second, type, alpha)
        }

        @JvmStatic
        @Suppress("FunctionName")
        public fun HSBtoRGB(hue: Float, saturation: Float, brightness: Float): Int {
            val r: Int
            val g: Int
            val b: Int
            if (saturation == 0f) {
                r = (brightness * 255.0f + 0.5f).toInt()
                g = r
                b = r
            } else {
                val h = (hue - floor(hue)) * 6.0f
                val f = h - floor(h)
                val p = brightness * (1.0f - saturation)
                val q = brightness * (1.0f - saturation * f)
                val t = brightness * (1.0f - saturation * (1.0f - f))
                when (h.toInt()) {
                    0 -> {
                        r = (brightness * 255.0f + 0.5f).toInt()
                        g = (t * 255.0f + 0.5f).toInt()
                        b = (p * 255.0f + 0.5f).toInt()
                    }

                    1 -> {
                        r = (q * 255.0f + 0.5f).toInt()
                        g = (brightness * 255.0f + 0.5f).toInt()
                        b = (p * 255.0f + 0.5f).toInt()
                    }

                    2 -> {
                        r = (p * 255.0f + 0.5f).toInt()
                        g = (brightness * 255.0f + 0.5f).toInt()
                        b = (t * 255.0f + 0.5f).toInt()
                    }

                    3 -> {
                        r = (p * 255.0f + 0.5f).toInt()
                        g = (q * 255.0f + 0.5f).toInt()
                        b = (brightness * 255.0f + 0.5f).toInt()
                    }

                    4 -> {
                        r = (t * 255.0f + 0.5f).toInt()
                        g = (p * 255.0f + 0.5f).toInt()
                        b = (brightness * 255.0f + 0.5f).toInt()
                    }

                    5 -> {
                        r = (brightness * 255.0f + 0.5f).toInt()
                        g = (p * 255.0f + 0.5f).toInt()
                        b = (q * 255.0f + 0.5f).toInt()
                    }

                    else -> {
                        r = 0
                        g = 0
                        b = 0
                    }
                }
            }
            return (r shl 16) or (g shl 8) or b
        }

        @JvmStatic
        @Suppress("FunctionName")
        public fun RGBtoHSB(red: Int, green: Int, blue: Int, output: FloatArray? = null): FloatArray {
            val output = output ?: FloatArray(3)

            var cmax = if (red > green) red else green
            if (blue > cmax) {
                cmax = blue
            }

            var cmin = if (red < green) red else green
            if (blue < cmin) {
                cmin = blue
            }

            val brightness = cmax.toFloat() / 255.0f
            val saturation = if (cmax != 0) {
                (cmax - cmin).toFloat() / cmax.toFloat()
            } else {
                0f
            }

            val hue = if (saturation != 0f) {
                val redc = (cmax - red).toFloat() / (cmax - cmin).toFloat()
                val greenc = (cmax - green).toFloat() / (cmax - cmin).toFloat()
                val bluec = (cmax - blue).toFloat() / (cmax - cmin).toFloat()
                (when (cmax) {
                    red -> {
                        bluec - greenc
                    }

                    green -> {
                        2f + redc - bluec
                    }

                    else -> {
                        4f + greenc - redc
                    }
                } / 6.0f).coerceAtLeast(0f)
            } else {
                0f
            }

            output[0] = hue
            output[1] = saturation
            output[2] = brightness

            return output
        }

    }

    public open val hue: Float = hue

    public open val saturation: Float = saturation

    public open val brightness: Float = brightness

    /**
     * Creates an integer-packed colour value using this color's hue, saturation, and brightness values converted to RGB.
     *
     * @return A bitshifted integer containing the RGB values of this color - 0bRRRRRRRRGGGGGGGGBBBBBBBB (0xRRGGBB)
     */
    public open val rgb: Int
        get() = HSBtoRGB(hue, saturation, brightness)

    /**
     * Creates an integer-packed colour value using this color's red, green, blue, and alpha values.
     *
     * @return A bitshifted integer containing the ARGB values of this color - 0bAAAAAAAARRRRRRRRGGGGGGGGBBBBBBBB (0xAARRGGBB)
     */
    public open val argb: Int
        get() = ((alpha * 255f).toInt() shl 24) or rgb

    /**
     * Creates an integer-packed colour value using this color's red, green, and blue values.
     *
     * @return A bitshifted integer containing the RGB values of this color - 0bRRRRRRRRGGGGGGGGBBBBBBBB (0xRRGGBB)
     */
    public open val rgba: Int
        get() = rgb or (alpha shl 24)

    public open val red: Int
        get() = (rgba shr 16) and 0xFF

    public open val green: Int
        get() = (rgba shr 8) and 0xFF

    public open val blue: Int
        get() = rgba and 0xFF

    public open val isOpaque: Boolean
        get() = alpha == 255

    public open val isTranslucent: Boolean
        get() = alpha != 255

    public open val isTransparent: Boolean
        get() = alpha == 0

    public open val isGrayscale: Boolean
        get() = red == green && green == blue

    public constructor(
        values: Triple<Float, Float, Float>,
        alpha: Int = 255
    ) : this(alpha, values.first, values.second, values.third)

    public constructor(
        values: FloatArray,
        alpha: Int = 255
    ) : this(alpha, values[0], values[1], values[2])

    public constructor(
        red: Int,
        green: Int,
        blue: Int,
        alpha: Int = 255
    ) : this(RGBtoHSB(red, green, blue), alpha)

}
