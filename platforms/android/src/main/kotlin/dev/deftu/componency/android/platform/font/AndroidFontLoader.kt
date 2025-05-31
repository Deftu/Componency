package dev.deftu.componency.android.platform.font

import android.content.Context
import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontFamily
import dev.deftu.componency.font.FontWeight
import dev.deftu.componency.font.StaticFontFamily
import dev.deftu.componency.platform.font.FontLoader
import java.io.File

public class AndroidFontLoader(
    private val context: Context
) : FontLoader {

    override fun loadFontEmbedded(
        name: String,
        path: String,
        letterSpacing: Float,
        lineSpacing: Float,
        isItalic: Boolean,
        weight: FontWeight,
        family: FontFamily?
    ): Font? {
        return try {
            val inputStream = context.assets.open(path.removePrefix("/"))
            val bytes = inputStream.readBytes()
            inputStream.close()

            Font(
                name = name,
                data = bytes,
                letterSpacing = letterSpacing,
                lineSpacing = lineSpacing,
                isItalic = isItalic,
                weight = weight,
                family = family
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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
        return try {
            val file = File(path)
            if (!file.exists()) return null
            val bytes = file.readBytes()

            Font(
                name = name,
                data = bytes,
                letterSpacing = letterSpacing,
                lineSpacing = lineSpacing,
                isItalic = isItalic,
                weight = weight,
                family = family
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun loadFontFamilyEmbedded(name: String, path: String): StaticFontFamily? {
        return StaticFontFamily(
            loader = { relativePath ->
                try {
                    val fullPath = if (relativePath.startsWith("/")) relativePath.removePrefix("/") else relativePath
                    context.assets.open(fullPath).use { it.readBytes() }
                } catch (e: Exception) {
                    null
                }
            },
            name = name,
            path = path.trim('/')
        )
    }

    override fun loadFontFamily(name: String, path: String): StaticFontFamily? {
        return StaticFontFamily(
            loader = { relativePath ->
                try {
                    val fullPath = File(path, relativePath.removePrefix("/"))
                    if (fullPath.exists()) {
                        fullPath.readBytes()
                    } else {
                        null
                    }
                } catch (e: Exception) {
                    null
                }
            },
            name = name,
            path = path.trim('/')
        )
    }
}
