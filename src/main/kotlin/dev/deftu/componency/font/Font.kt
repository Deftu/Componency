package dev.deftu.componency.font

public class Font(
    public val letterSpacing: Float,
    public val lineSpacing: Float,
    public val family: FontFamily,
    public val isItalic: Boolean,
    public val weight: FontWeight
) {

    public fun asWeight(weight: FontWeight): Font {
        return family.get(weight, letterSpacing, lineSpacing, isItalic)
    }

    public fun asItalic(): Font {
        return family.get(weight, letterSpacing, lineSpacing, true)
    }

    override fun hashCode(): Int {
        var result = letterSpacing.hashCode()
        result = 31 * result + lineSpacing.hashCode()
        result = 31 * result + family.hashCode()
        result = 31 * result + isItalic.hashCode()
        result = 31 * result + weight.hashCode()
        return result
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
