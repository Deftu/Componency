package dev.deftu.componency

public data class Density(
    val pxPerDp: Float,
    val fontScale: Float,
) {
    public companion object {
        public val Default: Density = Density(1f, 1f)
    }
}
