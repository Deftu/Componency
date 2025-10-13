package dev.deftu.componency.canvas

public data class Paint(
    public val brush: Brush,
    public val style: PaintStyle = PaintStyle.Fill,
    public val stroke: Stroke? = null,
    val blending: BlendMode = BlendMode.SRC_OVER,
    val alpha: Float = 1f,
)
