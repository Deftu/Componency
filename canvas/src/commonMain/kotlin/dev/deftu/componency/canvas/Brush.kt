package dev.deftu.componency.canvas

import dev.deftu.componency.Bounds
import dev.deftu.componency.Image
import dev.deftu.kolor.Color

public sealed interface Brush {
    public data class Solid(public val color: Color) : Brush

    public data class LinearGradient(
        public val startX: Float,
        public val startY: Float,
        public val endX: Float,
        public val endY: Float,
        public val stops: List<GradientStop>,
        public val mode: TileMode = TileMode.CLAMP,
    ) : Brush

    public data class RadialGradient(
        public val centerX: Float,
        public val centerY: Float,
        public val radius: Float,
        public val stops: List<GradientStop>,
        public val mode: TileMode = TileMode.CLAMP,
    ) : Brush

    public data class ImageBrush(
        public val image: Image,
        public val src: Bounds? = null,
        public val sampling: Sampling = Sampling.LINEAR,
        public val tileModeX: TileMode = TileMode.CLAMP,
        public val tileModeY: TileMode = TileMode.CLAMP,
    ) : Brush
}
