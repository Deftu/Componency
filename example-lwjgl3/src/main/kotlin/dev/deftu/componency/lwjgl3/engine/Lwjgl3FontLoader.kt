package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontFamily
import dev.deftu.componency.font.FontWeight
import dev.deftu.componency.font.StaticFontFamily
import dev.deftu.componency.platform.font.FontLoader
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readBytes

class Lwjgl3FontLoader : FontLoader {

    override fun loadFontEmbedded(
        name: String,
        path: String,
        letterSpacing: Float,
        lineSpacing: Float,
        isItalic: Boolean,
        weight: FontWeight,
        family: FontFamily?
    ): Font? {
        val stream = Lwjgl3FontLoader::class.java.getResourceAsStream(path) ?: return null

        return Font(
            name = name,
            data = stream.readBytes(),
            letterSpacing = letterSpacing,
            lineSpacing = lineSpacing,
            isItalic = isItalic,
            weight = weight
        )
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
        val location = Path(path)
        if (!location.exists()) {
            return null
        }

        val data = location.readBytes()
        return Font(
            name = name,
            data = data,
            letterSpacing = letterSpacing,
            lineSpacing = lineSpacing,
            isItalic = isItalic,
            weight = weight
        )
    }

    override fun loadFontFamilyEmbedded(name: String, path: String): StaticFontFamily {
        return StaticFontFamily(
            { requestedPath ->
                Lwjgl3FontLoader::class.java.getResourceAsStream(requestedPath)?.readBytes()
            },
            name,
            path
        )
    }

    override fun loadFontFamily(name: String, path: String): StaticFontFamily? {
        val location = Path(path)
        if (!location.exists()) {
            return null
        }

        return StaticFontFamily(
            { requestedPath ->
                val filePath = location.parent.resolve(requestedPath)
                if (filePath.exists()) {
                    filePath.readBytes()
                } else {
                    null
                }
            },
            name,
            path
        )
    }

}
