package dev.deftu.componency.webcanvas.platform.font

import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontFamily
import dev.deftu.componency.font.FontWeight

public class CanvasFontFamily(name: String) : FontFamily(name) {

    private data class FontStyle(
        val weight: FontWeight,
        val isItalic: Boolean
    )

    private val loadedStyles: MutableSet<FontStyle> = mutableSetOf()

    public fun registerStyle(weight: FontWeight, isItalic: Boolean) {
        loadedStyles.add(FontStyle(weight, isItalic))
    }

    override fun has(weight: FontWeight, isItalic: Boolean): Boolean {
        return loadedStyles.contains(FontStyle(weight, isItalic))
    }

    override fun get(weight: FontWeight, letterSpacing: Float, lineSpacing: Float, isItalic: Boolean): Font {
        if (!has(weight, isItalic)) {
            if (isItalic && has(weight, false)) {
                return get(weight, letterSpacing, lineSpacing, false)
            }

            throw IllegalArgumentException("Font style for '$name' not registered: weight=$weight, italic=$isItalic")
        }

        return Font(
            name = name,
            data = ByteArray(0), // Placeholder for actual font data
            letterSpacing = letterSpacing,
            lineSpacing = lineSpacing,
            isItalic = isItalic,
            weight = weight
        )
    }

}
