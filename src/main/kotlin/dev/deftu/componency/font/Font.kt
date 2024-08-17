package dev.deftu.componency.font

import java.io.InputStream
import java.nio.file.Path

public class Font(
    public val name: String,
    public val data: ByteArray,
    public val letterSpacing: Float,
    public val lineSpacing: Float,
    public val isItalic: Boolean,
    public val weight: FontWeight,
    public val family: FontFamily? = null
) {

    public constructor(
        name: String,
        inputStream: InputStream,
        letterSpacing: Float,
        lineSpacing: Float,
        isItalic: Boolean,
        weight: FontWeight,
        family: FontFamily? = null
    ) : this(
        name = name,
        data = inputStream.readBytes(),
        letterSpacing = letterSpacing,
        lineSpacing = lineSpacing,
        isItalic = isItalic,
        weight = weight,
        family = family
    )

    public constructor(
        name: String,
        path: Path,
        letterSpacing: Float,
        lineSpacing: Float,
        isItalic: Boolean,
        weight: FontWeight,
        family: FontFamily? = null
    ) : this(
        name = name,
        data = path.toFile().readBytes(),
        letterSpacing = letterSpacing,
        lineSpacing = lineSpacing,
        isItalic = isItalic,
        weight = weight,
        family = family
    )

    public fun asWeight(weight: FontWeight): Font {
        require(family != null) { "Can only get a font with another weight if the font is aware of its font family" }
        return family.get(weight, letterSpacing, lineSpacing, isItalic)
    }

    public fun asItalic(): Font {
        require(family != null) { "Can only get a font as italic if the font is aware of its font family" }
        return family.get(weight, letterSpacing, lineSpacing, true)
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Font

        if (letterSpacing != other.letterSpacing) return false
        if (lineSpacing != other.lineSpacing) return false
        if (family != other.family) return false
        if (isItalic != other.isItalic) return false
        if (weight != other.weight) return false

        return true
    }

}
