package dev.deftu.componency.defign

import dev.deftu.componency.color.Color
import dev.deftu.componency.dsl.withAlphaPercentage

public data class DefignPalette(
    public val primary: Color,
    public val primary2: Color,
    public val success: Color,
    public val warning: Color,
    public val error: Color,
    public val danger: Color,

    public val background1: Color,
    public val background2: Color,
    public val text1: Color,
    public val text2: Color,
    public val text3: Color,
) {

    public companion object {

        public const val PRIMARY: Int = 0xC33F3F
        public const val PRIMARY2: Int = 0xA63A3A

        public const val SUCCESS: Int = 0x3BC95F
        public const val WARNING: Int = 0xF0B94F
        public const val ERROR: Int = 0xC92A43
        public const val DANGER: Int = 0xE33131

        @JvmField
        public val LIGHT: DefignPalette = DefignPalette(
            Color.rgb(PRIMARY),
            Color.rgb(PRIMARY2),
            Color.rgb(SUCCESS),
            Color.rgb(WARNING),
            Color.rgb(ERROR),
            Color.rgb(DANGER),
            Color.rgb(Light.BACKGROUND1),
            Color.rgb(Light.BACKGROUND2),
            Color.rgb(Light.TEXT1),
            Color.rgb(Light.TEXT2).withAlphaPercentage(Light.TEXT2_ALPHA),
            Color.rgb(Light.TEXT3).withAlphaPercentage(Light.TEXT3_ALPHA),
        )

        @JvmField
        public val DARK: DefignPalette = DefignPalette(
            Color.rgb(PRIMARY),
            Color.rgb(PRIMARY2),
            Color.rgb(SUCCESS),
            Color.rgb(WARNING),
            Color.rgb(ERROR),
            Color.rgb(DANGER),
            Color.rgb(Dark.BACKGROUND1),
            Color.rgb(Dark.BACKGROUND2),
            Color.rgb(Dark.TEXT1),
            Color.rgb(Dark.TEXT2).withAlphaPercentage(Dark.TEXT2_ALPHA),
            Color.rgb(Dark.TEXT3).withAlphaPercentage(Dark.TEXT3_ALPHA),
        )

        @JvmStatic
        public fun get(isDarkMode: Boolean): DefignPalette {
            return if (isDarkMode) {
                DARK
            } else {
                LIGHT
            }
        }

    }

    public object Light {

        public const val BACKGROUND1: Int = 0xDDDDDD
        public const val BACKGROUND2: Int = 0xD1D1D1
        public const val TEXT1: Int = 0x28282B
        public const val TEXT2: Int = 0x28282B
        public const val TEXT3: Int = 0x333333

        public const val TEXT2_ALPHA: Float = 0.65f
        public const val TEXT3_ALPHA: Float = 0.40f

    }

    public object Dark {

        public const val BACKGROUND1: Int = 0x28282B
        public const val BACKGROUND2: Int = 0x212125
        public const val TEXT1: Int = 0xFDFBF9
        public const val TEXT2: Int = 0xD2CDC6
        public const val TEXT3: Int = 0xD2CDC6

        public const val TEXT2_ALPHA: Float = 0.65f
        public const val TEXT3_ALPHA: Float = 0.40f

    }

}
