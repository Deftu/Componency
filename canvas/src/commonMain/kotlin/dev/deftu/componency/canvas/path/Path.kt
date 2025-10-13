package dev.deftu.componency.canvas.path

public class Path : AutoCloseable {
    private val commands = mutableListOf<PathCommand>()

    public fun moveTo(x: Float, y: Float) {
        commands.add(PathCommand.MoveTo(x, y))
    }

    public fun lineTo(x: Float, y: Float) {
        commands.add(PathCommand.LineTo(x, y))
    }

    public fun cubicTo(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        x3: Float,
        y3: Float
    ) {
        commands.add(PathCommand.CubicTo(x1, y1, x2, y2, x3, y3))
    }

    public fun quadraticTo(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float
    ) {
        commands.add(PathCommand.QuadraticTo(x1, y1, x2, y2))
    }

    public fun arcTo(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        radiusX: Float,
        radiusY: Float
    ) {
        commands.add(PathCommand.ArcTo(x1, y1, x2, y2, radiusX, radiusY))
    }

    override fun close() {
        commands.add(PathCommand.Close)
    }
}
