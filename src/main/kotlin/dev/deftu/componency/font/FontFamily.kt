package dev.deftu.componency.font

import java.nio.file.Path

public abstract class FontFamily @JvmOverloads constructor(
    public val name: String,
    public val path: Path,
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
