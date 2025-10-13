package dev.deftu.componency.canvas

public data class Stroke(
    public val width: Float = 1f,
    public val join: Join = Join.Miter,
    public val cap: Cap = Cap.Butt,
    public val miterLimit: Float = 4f,
    public val dash: List<Float>? = null,
    public val dashPhase: Float = 0f,
) {
    public enum class Join {
        Round,
        Bevel,
        Miter,
    }

    public enum class Cap {
        Butt,
        Round,
        Square,
    }
}
