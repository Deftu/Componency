package dev.deftu.componency.canvas

import dev.deftu.componency.Bounds
import dev.deftu.componency.Density
import dev.deftu.componency.Image
import dev.deftu.componency.Size
import dev.deftu.componency.canvas.path.Path

public class RecordingCanvas(override val density: Density, private val output: DrawList) : Canvas {
    override fun save() {
        output.ops.add(DrawOp.Save)
    }

    override fun restore() {
        output.ops.add(DrawOp.Restore)
    }

    override fun translate(x: Float, y: Float) {
        output.ops.add(DrawOp.Translate(x, y))
    }

    override fun scale(scaleX: Float, scaleY: Float) {
        output.ops.add(DrawOp.Scale(scaleX, scaleY))
    }

    override fun rotate(degrees: Float) {
        output.ops.add(DrawOp.Rotate(degrees))
    }

    override fun clip(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        antialias: Boolean
    ) {
        output.ops.add(DrawOp.ClipRect(x, y, width, height, antialias))
    }

    override fun clipPath(path: Path, antialias: Boolean) {
        output.ops.add(DrawOp.ClipPath(path, antialias))
    }

    override fun drawRect(x: Float, y: Float, width: Float, height: Float, paint: Paint) {
        output.ops.add(DrawOp.DrawRect(x, y, width, height, paint))
    }

    override fun drawRoundedRect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        radiusX: Float,
        radiusY: Float,
        paint: Paint
    ) {
        output.ops.add(DrawOp.DrawRoundedRect(x, y, width, height, radiusX, radiusY, paint))
    }

    override fun drawPath(path: Path, paint: Paint) {
        output.ops.add(DrawOp.DrawPath(path, paint))
    }

    override fun drawImage(image: Image, dst: Bounds, src: Bounds?, sampling: Sampling) {
        output.ops.add(DrawOp.DrawImage(image, src, dst, sampling))
    }

    override fun drawNineSlice(image: Image, spec: NineSliceSpec, dst: Bounds, src: Bounds?, sampling: Sampling) {
        output.ops.add(DrawOp.DrawNineSlice(image, spec, dst, src, sampling))
    }

    override fun drawGlyphRun(run: GlyphRun, x: Float, y: Float, paint: Paint) {
        output.ops.add(DrawOp.DrawGlyphRun(run, x, y, paint))
    }

    override fun withLayer(
        size: Size,
        alpha: Float,
        blending: BlendMode,
        block: Canvas.() -> Unit
    ) {
        val list = DrawList()
        val subCanvas = RecordingCanvas(density, list)
        block(subCanvas)

        output.ops.add(DrawOp.Layer(size.width, size.height, alpha, blending, list))
    }
}
