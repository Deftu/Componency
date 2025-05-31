package dev.deftu.componency.webcanvas.platform.font

import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontFamily
import dev.deftu.componency.font.FontWeight
import dev.deftu.componency.platform.font.FontLoader
import kotlinx.browser.document

public object CanvasFontLoader : FontLoader {

    override fun loadFontEmbedded(
        name: String,
        path: String,
        letterSpacing: Float,
        lineSpacing: Float,
        isItalic: Boolean,
        weight: FontWeight,
        family: FontFamily?
    ): Font? {
        return loadFont(name, path, letterSpacing, lineSpacing, isItalic, weight, family)
    }

    override fun loadFont(
        name: String,
        path: String,
        letterSpacing: Float,
        lineSpacing: Float,
        isItalic: Boolean,
        weight: FontWeight,
        family: FontFamily?
    ): Font? {
        val familyName = family?.name ?: name
        val style = if (isItalic) "italic" else "normal"
        val cssRule = """
            @font-face {
                font-family: '$familyName';
                src: $path;
                font-weight: ${weight.value};
                font-style: $style;
            }
        """.trimIndent()

        injectCss(cssRule)

        return Font(
            name = familyName,
            data = ByteArray(0), // no data on web, stub it
            letterSpacing = letterSpacing,
            lineSpacing = lineSpacing,
            isItalic = isItalic,
            weight = weight,
            family = family
        )
    }

    override fun loadFontFamilyEmbedded(name: String, path: String): FontFamily {
        return loadFontFamily(name, path)
    }

    override fun loadFontFamily(name: String, path: String): FontFamily {
        return CanvasFontFamily(name)
    }

    private fun injectCss(rule: String) {
        val style = document.createElement("style")
        style.textContent = rule
        document.head?.appendChild(style)
    }

}
