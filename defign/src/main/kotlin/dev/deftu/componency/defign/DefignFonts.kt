package dev.deftu.componency.defign

import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.dsl.px
import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontWeight

public object DefignFonts {

    private const val OUTFIT_PATH: String = "fonts/outfit"
    private const val INTER_PATH: String = "fonts/inter"

    public const val ATTENTION1_SIZE: Int = 57
    public const val ATTENTION1_LINE_HEIGHT: Float = 64f
    public const val ATTENTION2_SIZE: Int = 45
    public const val ATTENTION2_LINE_HEIGHT: Float = 52f
    public const val ATTENTION3_SIZE: Int = 36
    public const val ATTENTION3_LINE_HEIGHT: Float = 44f

    public const val HEADER1_SIZE: Int = 32
    public const val HEADER1_LINE_HEIGHT: Float = 40f
    public const val HEADER2_SIZE: Int = 24
    public const val HEADER2_LINE_HEIGHT: Float = 36f
    public const val HEADER3_SIZE: Int = 24
    public const val HEADER3_LINE_HEIGHT: Float = 32f

    public const val TITLE1_SIZE: Int = 22
    public const val TITLE1_LINE_HEIGHT: Float = 28f
    public const val TITLE2_SIZE: Int = 16
    public const val TITLE2_LINE_HEIGHT: Float = 24f
    public const val TITLE3_SIZE: Int = 14
    public const val TITLE3_LINE_HEIGHT: Float = 20f

    public const val BODY1_SIZE: Int = 16
    public const val BODY1_LINE_HEIGHT: Float = 24f
    public const val BODY2_SIZE: Int = 14
    public const val BODY2_LINE_HEIGHT: Float = 20f
    public const val BODY3_SIZE: Int = 12
    public const val BODY3_LINE_HEIGHT: Float = 16f

    public const val CAPTION1_SIZE: Int = 14
    public const val CAPTION1_LINE_HEIGHT: Float = 20f
    public const val CAPTION2_SIZE: Int = 12
    public const val CAPTION2_LINE_HEIGHT: Float = 16f
    public const val CAPTION3_SIZE: Int = 11
    public const val CAPTION3_LINE_HEIGHT: Float = 16f

    private val outfitFamily = StaticFontFamily("Outfit", OUTFIT_PATH)
    private val interFamily = StaticFontFamily("Inter", INTER_PATH)

    @JvmStatic
    public val attention1: Font
        get() {
            return outfitFamily.get(FontWeight.BOLD, -0.25f, ATTENTION1_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val attention2: Font
        get() {
            return outfitFamily.get(FontWeight.BOLD, 0f, ATTENTION2_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val attention3: Font
        get() {
            return outfitFamily.get(FontWeight.BOLD, 0f, ATTENTION3_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val header1: Font
        get() {
            return outfitFamily.get(FontWeight.SEMI_BOLD, 0f, HEADER1_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val header2: Font
        get() {
            return outfitFamily.get(FontWeight.SEMI_BOLD, 0f, HEADER2_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val header3: Font
        get() {
            return outfitFamily.get(FontWeight.SEMI_BOLD, 0f, HEADER3_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val title1: Font
        get() {
            return outfitFamily.get(FontWeight.SEMI_BOLD, 0f, TITLE1_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val title2: Font
        get() {
            return outfitFamily.get(FontWeight.SEMI_BOLD, 0f, TITLE2_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val title3: Font
        get() {
            return outfitFamily.get(FontWeight.SEMI_BOLD, 0f, TITLE3_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val body1: Font
        get() {
            return interFamily.get(FontWeight.REGULAR, 0.5f, BODY1_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val body2: Font
        get() {
            return interFamily.get(FontWeight.REGULAR, 0.25f, BODY2_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val body3: Font
        get() {
            return interFamily.get(FontWeight.REGULAR, 0.4f, BODY3_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val caption1: Font
        get() {
            return interFamily.get(FontWeight.REGULAR, 0.1f, CAPTION1_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val caption2: Font
        get() {
            return interFamily.get(FontWeight.REGULAR, 0.5f, CAPTION2_LINE_HEIGHT, false)
        }

    @JvmStatic
    public val caption3: Font
        get() {
            return interFamily.get(FontWeight.REGULAR, 0.5f, CAPTION3_LINE_HEIGHT, false)
        }

    /**
     * Simply references all fonts to preload them.
     */
    @JvmStatic
    public fun preload() {
        attention1
        attention2
        attention3
        header1
        header2
        header3
        title1
        title2
        title3
        body1
        body2
        body3
        caption1
        caption2
        caption3
    }

    @JvmStatic
    @JvmOverloads
    public fun useAttention1(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = attention1
        properties.fontSize = (ATTENTION1_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useAttention2(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = attention2
        properties.fontSize = (ATTENTION2_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useAttention3(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = attention3
        properties.fontSize = (ATTENTION3_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useHeader1(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = header1
        properties.fontSize = (HEADER1_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useHeader2(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = header2
        properties.fontSize = (HEADER2_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useHeader3(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = header3
        properties.fontSize = (HEADER3_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useTitle1(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = title1
        properties.fontSize = (TITLE1_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useTitle2(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = title2
        properties.fontSize = (TITLE2_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useTitle3(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = title3
        properties.fontSize = (TITLE3_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useBody1(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = body1
        properties.fontSize = (BODY1_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useBody2(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = body2
        properties.fontSize = (BODY2_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useBody3(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = body3
        properties.fontSize = (BODY3_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useCaption1(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = caption1
        properties.fontSize = (CAPTION1_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useCaption2(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = caption2
        properties.fontSize = (CAPTION2_SIZE / scalingFactor).px
    }

    @JvmStatic
    public fun useCaption3(properties: ComponentProperties, scalingFactor: Float = 1f) {
        properties.font = caption3
        properties.fontSize = (CAPTION3_SIZE / scalingFactor).px
    }

}
