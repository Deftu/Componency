package dev.deftu.componency.properties.impl

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.Component
import dev.deftu.componency.easings.Easing
import dev.deftu.componency.properties.*
import dev.deftu.componency.stroke.Stroke
import dev.deftu.componency.utils.interpolateColor
import dev.deftu.componency.utils.interpolateFloat
import dev.deftu.componency.utils.interpolateStroke
import kotlin.math.max

public abstract class AnimatingProperty<T, P : Property<T>>(
    public val easing: Easing,
    public val totalFrames: Int,
    public val from: P,
    public val to: P,
) : Property<T> {

    private var elapsedFrames = 0
    private var isPaused = false

    public val isFinished: Boolean
        get() = elapsedFrames >= totalFrames

    public val progress: Float
        get() = easing.ease(elapsedFrames.toFloat() / max(totalFrames, 1).toFloat())

    override fun animationFrame(deltaTime: Float) {
        super.animationFrame(deltaTime)
        if (isPaused || isFinished) {
            return
        }

        elapsedFrames++
    }

    public fun pause() {
        isPaused = true
    }

    public fun resume() {
        isPaused = false
    }

    public fun reset() {
        elapsedFrames = 0
    }

    public fun stop() {
        elapsedFrames = totalFrames
    }

}

public class AnimatingXProperty(
    easing: Easing,
    totalFrames: Int,
    from: XProperty,
    to: XProperty,
) : XProperty, AnimatingProperty<Float, XProperty>(easing, totalFrames, from, to) {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun calculateX(component: Component<*, *>): Float {
        return interpolateFloat(from.getX(component), to.getX(component), progress)
    }

}

public class AnimatingYProperty(
    easing: Easing,
    totalFrames: Int,
    from: YProperty,
    to: YProperty,
) : YProperty, AnimatingProperty<Float, YProperty>(easing, totalFrames, from, to) {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun calculateY(component: Component<*, *>): Float {
        return interpolateFloat(from.getY(component), to.getY(component), progress)
    }

}

public class AnimatingWidthProperty(
    easing: Easing,
    totalFrames: Int,
    from: WidthProperty,
    to: WidthProperty,
) : WidthProperty, AnimatingProperty<Float, WidthProperty>(easing, totalFrames, from, to) {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun calculateWidth(component: Component<*, *>): Float {
        return interpolateFloat(from.getWidth(component), to.getWidth(component), progress)
    }

}

public class AnimatingHeightProperty(
    easing: Easing,
    totalFrames: Int,
    from: HeightProperty,
    to: HeightProperty,
) : HeightProperty, AnimatingProperty<Float, HeightProperty>(easing, totalFrames, from, to) {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun calculateHeight(component: Component<*, *>): Float {
        return interpolateFloat(from.getHeight(component), to.getHeight(component), progress)
    }

}

public class AnimatingColorProperty(
    easing: Easing,
    totalFrames: Int,
    from: ColorProperty,
    to: ColorProperty,
) : ColorProperty, AnimatingProperty<Color, ColorProperty>(easing, totalFrames, from, to) {

    override var cachedValue: Color = Color.TRANSPARENT
    override var needsRecalculate: Boolean = true

    override fun calculateColor(component: Component<*, *>): Color {
        return interpolateColor(from.getColor(component), to.getColor(component), progress)
    }

}

public class AnimatingStrokeProperty(
    easing: Easing,
    totalFrames: Int,
    from: StrokeProperty,
    to: StrokeProperty,
) : StrokeProperty, AnimatingProperty<Stroke, StrokeProperty>(easing, totalFrames, from, to) {

    override var cachedValue: Stroke = Stroke.NONE
    override var needsRecalculate: Boolean = true

    override fun calculateStroke(component: Component<*, *>): Stroke {
        return interpolateStroke(from.getStroke(component), to.getStroke(component), progress)
    }

}

public class AnimatingRadialProperty(
    easing: Easing,
    totalFrames: Int,
    from: RadialProperty,
    to: RadialProperty,
) : RadialProperty, AnimatingProperty<Float, RadialProperty>(easing, totalFrames, from, to) {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun calculateRadius(component: Component<*, *>): Float {
        return interpolateFloat(from.getRadius(component), to.getRadius(component), progress)
    }

}

public class AnimatingAngleProperty(
    easing: Easing,
    totalFrames: Int,
    from: AngleProperty,
    to: AngleProperty,
) : AngleProperty, AnimatingProperty<Float, AngleProperty>(easing, totalFrames, from, to) {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun calculateAngle(component: Component<*, *>): Float {
        return interpolateFloat(from.getAngle(component), to.getAngle(component), progress)
    }

}
