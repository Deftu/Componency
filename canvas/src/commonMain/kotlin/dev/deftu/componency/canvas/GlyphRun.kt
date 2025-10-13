package dev.deftu.componency.canvas

public data class GlyphRun(
    public val font: FontRef,
    public val size: Float,
    public val glyphs: IntArray,
    public val positions: FloatArray,
    public val advances: FloatArray,
    public val clusters: IntArray,
    public val bidiLevel: Int,
    public val ascent: Float,
    public val descent: Float,
    public val leading: Float,
    public val range: IntRange?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GlyphRun

        if (size != other.size) return false
        if (bidiLevel != other.bidiLevel) return false
        if (ascent != other.ascent) return false
        if (descent != other.descent) return false
        if (leading != other.leading) return false
        if (font != other.font) return false
        if (!glyphs.contentEquals(other.glyphs)) return false
        if (!positions.contentEquals(other.positions)) return false
        if (!advances.contentEquals(other.advances)) return false
        if (!clusters.contentEquals(other.clusters)) return false
        if (range != other.range) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size.hashCode()
        result = 31 * result + bidiLevel
        result = 31 * result + ascent.hashCode()
        result = 31 * result + descent.hashCode()
        result = 31 * result + leading.hashCode()
        result = 31 * result + font.hashCode()
        result = 31 * result + glyphs.contentHashCode()
        result = 31 * result + positions.contentHashCode()
        result = 31 * result + advances.contentHashCode()
        result = 31 * result + clusters.contentHashCode()
        result = 31 * result + (range?.hashCode() ?: 0)
        return result
    }
}
