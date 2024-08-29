package dev.deftu.componency.defign

import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontFamily
import dev.deftu.componency.font.FontWeight

public class StaticFontFamily(
    name: String,
    private val path: String
) : FontFamily(name) {

    private data class FontStyle(
        val weight: FontWeight,
        val isItalic: Boolean
    ) {

        fun asDescriptor(): String {
            return "${when (weight) {
                FontWeight.REGULAR -> "Regular"
                FontWeight.LIGHT -> "Light"
                FontWeight.BOLD -> "Bold"
                FontWeight.EXTRA_LIGHT -> "ExtraLight"
                FontWeight.THIN -> "Thin"
                FontWeight.MEDIUM -> "Medium"
                FontWeight.SEMI_BOLD -> "SemiBold"
                FontWeight.EXTRA_BOLD -> "ExtraBold"
                FontWeight.BLACK -> "Black"
            }}${if (isItalic) "Italic" else ""}"
        }

    }

    private val styles: MutableMap<FontStyle, Font> = mutableMapOf()

    override fun has(weight: FontWeight, isItalic: Boolean): Boolean {
        return styles.containsKey(FontStyle(weight, isItalic))
    }

    override fun get(weight: FontWeight, letterSpacing: Float, lineSpacing: Float, isItalic: Boolean): Font {
        val style = FontStyle(weight, isItalic)
        if (styles.containsKey(style)) {
            return styles[style]!!
        }

        // Attempt to load the italic font first. If it doesn't exist, fallback to the regular font.
        val descriptor = style.asDescriptor()
        val path = "/$path/$name-$descriptor.ttf"
        val stream = StaticFontFamily::class.java.getResourceAsStream(path)
        if (stream == null) {
            if (isItalic) {
                return get(weight, letterSpacing, lineSpacing, false)
            }

            throw IllegalArgumentException("Font $name-$descriptor.ttf does not exist.")
        }

        val font = Font(
            name = name,
            inputStream = stream,
            letterSpacing = letterSpacing,
            lineSpacing = lineSpacing,
            isItalic = isItalic,
            weight = weight
        )

        styles[style] = font
        return font
    }

}
