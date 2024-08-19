package dev.deftu.componency.font

public abstract class FontFamily @JvmOverloads constructor(
    public val name: String,
    public val fallback: FontFamily? = null
) {

    public abstract fun has(
        weight: FontWeight,
        isItalic: Boolean
    ): Boolean

    public abstract fun get(
        weight: FontWeight,
        letterSpacing: Float,
        lineSpacing: Float,
        isItalic: Boolean
    ): Font

}
