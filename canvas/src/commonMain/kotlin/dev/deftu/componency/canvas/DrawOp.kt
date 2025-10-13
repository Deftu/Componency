package dev.deftu.componency.canvas

import dev.deftu.componency.Bounds
import dev.deftu.componency.Image
import dev.deftu.componency.canvas.path.Path

public interface DrawOp {
    public data object Save : DrawOp
    public data object Restore : DrawOp
    public data class Translate(val x: Float, val y: Float) : DrawOp
    public data class Scale(val scaleX: Float, val scaleY: Float) : DrawOp
    public data class Rotate(val degrees: Float) : DrawOp
    public data class ClipRect(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        val antialias: Boolean,
    ) : DrawOp
    public data class ClipPath(val path: Path, val antialias: Boolean) : DrawOp
    public data class DrawRect(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        val paint: Paint,
    ) : DrawOp
    public data class DrawRoundedRect(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        val radiusX: Float,
        val radiusY: Float,
        val paint: Paint,
    ) : DrawOp
    public data class DrawPath(val path: Path, val paint: Paint) : DrawOp
    public data class DrawImage(
        val image: Image,
        val src: Bounds?,
        val dst: Bounds,
        val sampling: Sampling,
    ) : DrawOp
    public data class DrawNineSlice(
        val image: Image,
        val spec: NineSliceSpec,
        val dst: Bounds,
        val src: Bounds?,
        val sampling: Sampling,
    ) : DrawOp
    public data class DrawGlyphRun(
        val run: GlyphRun,
        val x: Float,
        val y: Float,
        val paint: Paint,
    ) : DrawOp
    public data class Layer(
        val width: Float,
        val height: Float,
        val alpha: Float,
        val blending: BlendMode,
        val content: DrawList,
    ) : DrawOp
}
