package dev.deftu.componency.platform.font

import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontFamily
import dev.deftu.componency.font.FontWeight
import dev.deftu.componency.font.StaticFontFamily

public interface FontLoader {

    /**
     * Loads a font file which is embedded in the application from the given path.
     */
    public fun loadFontEmbedded(
        name: String,
        path: String,
        letterSpacing: Float,
        lineSpacing: Float,
        isItalic: Boolean,
        weight: FontWeight,
        family: FontFamily? = null
    ): Font?

    /**
     * Loads a font file from the given path on the filesystem.
     */
    public fun loadFont(
        name: String,
        path: String,
        letterSpacing: Float,
        lineSpacing: Float,
        isItalic: Boolean,
        weight: FontWeight,
        family: FontFamily? = null
    ): Font?

    public fun loadFontFamilyEmbedded(
        name: String,
        path: String,
    ): StaticFontFamily?

    public fun loadFontFamily(
        name: String,
        path: String
    ): StaticFontFamily?

}
