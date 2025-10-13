package dev.deftu.componency.canvas

import dev.deftu.componency.Bounds
import dev.deftu.componency.Density
import dev.deftu.componency.Image
import dev.deftu.componency.Size
import dev.deftu.componency.canvas.path.Path

public interface Canvas {
    public val density: Density

    public fun save()
    public fun restore()

    public fun translate(x: Float, y: Float)
    public fun scale(scaleX: Float, scaleY: Float)
    public fun rotate(degrees: Float)

    public fun clip(x: Float, y: Float, width: Float, height: Float, antialias: Boolean = true)
    public fun clipPath(path: Path, antialias: Boolean = true)

    public fun drawRect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        paint: Paint,
    )

    public fun drawRoundedRect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        radiusX: Float,
        radiusY: Float,
        paint: Paint,
    )

    public fun drawPath(
        path: Path,
        paint: Paint,
    )

    public fun drawImage(
        image: Image,
        dst: Bounds,
        src: Bounds? = null,
        sampling: Sampling = Sampling.LINEAR,
    )

    public fun drawNineSlice(
        image: Image,
        spec: NineSliceSpec,
        dst: Bounds,
        src: Bounds? = null,
        sampling: Sampling = Sampling.LINEAR,
    )

    public fun drawGlyphRun(
        run: GlyphRun,
        x: Float,
        y: Float,
        paint: Paint,
    )

    public fun withLayer(
        size: Size,
        alpha: Float = 1f,
        blending: BlendMode = BlendMode.SRC_OVER,
        block: Canvas.() -> Unit,
    )
}
