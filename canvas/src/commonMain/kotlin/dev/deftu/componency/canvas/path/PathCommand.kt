package dev.deftu.componency.canvas.path

public sealed interface PathCommand {
    public data class MoveTo(val x: Float, val y: Float) : PathCommand

    public data class LineTo(val x: Float, val y: Float) : PathCommand

    public data class CubicTo(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float,
        val x3: Float,
        val y3: Float
    ) : PathCommand

    public data class QuadraticTo(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float
    ) : PathCommand

    public data class ArcTo(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float,
        val radiusX: Float,
        val radiusY: Float
    ) : PathCommand

    public data object Close : PathCommand
}
