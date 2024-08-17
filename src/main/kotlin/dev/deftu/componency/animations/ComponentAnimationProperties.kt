package dev.deftu.componency.animations

import dev.deftu.componency.animations.properties.*
import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.engine.Engine
import dev.deftu.componency.properties.*

public class ComponentAnimationProperties(
    component: Component
) : ComponentProperties(component) {

    private val listeners = mutableListOf<() -> Unit>()

    public fun whenComplete(listener: () -> Unit): ComponentAnimationProperties = apply {
        listeners.add(listener)
    }

    public fun whenComplete(listener: Runnable): ComponentAnimationProperties = apply {
        listeners.add(listener::run)
    }

    override fun frame() {
        super.frame()

        var isAnimating = false

        val x = this.x
        if (x is XAnimatingProperty) {
            if (x.isFinished) {
                this.x = x.newValue
            } else {
                isAnimating = true
            }
        }

        val y = this.y
        if (y is YAnimatingProperty) {
            if (y.isFinished) {
                this.y = y.newValue
            } else {
                isAnimating = true
            }
        }

        val width = this.width
        if (width is WidthAnimatingProperty) {
            if (width.isFinished) {
                this.width = width.newValue
            } else {
                isAnimating = true
            }
        }

        val height = this.height
        if (height is HeightAnimatingProperty) {
            if (height.isFinished) {
                this.height = height.newValue
            } else {
                isAnimating = true
            }
        }

        val fill = this.fill
        if (fill is ColorAnimatingProperty) {
            if (fill.isFinished) {
                this.fill = fill.newValue
            } else {
                isAnimating = true
            }
        }

        // val stroke = this.stroke
        // if (stroke is StrokeAnimatingProperty) {
        //     if (stroke.isFinished) {
        //         this.stroke = stroke.newValue
        //     } else {
        //         isAnimating = true
        //     }
        // }

        val topLeftRadius = this.topLeftRadius
        if (topLeftRadius is RadialAnimatingProperty) {
            if (topLeftRadius.isFinished) {
                this.topLeftRadius = topLeftRadius.newValue
            } else {
                isAnimating = true
            }
        }

        val topRightRadius = this.topRightRadius
        if (topRightRadius is RadialAnimatingProperty) {
            if (topRightRadius.isFinished) {
                this.topRightRadius = topRightRadius.newValue
            } else {
                isAnimating = true
            }
        }

        val bottomRightRadius = this.bottomRightRadius
        if (bottomRightRadius is RadialAnimatingProperty) {
            if (bottomRightRadius.isFinished) {
                this.bottomRightRadius = bottomRightRadius.newValue
            } else {
                isAnimating = true
            }
        }

        val bottomLeftRadius = this.bottomLeftRadius
        if (bottomLeftRadius is RadialAnimatingProperty) {
            if (bottomLeftRadius.isFinished) {
                this.bottomLeftRadius = bottomLeftRadius.newValue
            } else {
                isAnimating = true
            }
        }

        val angle = this.angle
        if (angle is AngleAnimatingProperty) {
            if (angle.isFinished) {
                this.angle = angle.newValue
            } else {
                isAnimating = true
            }
        }

        if (!isAnimating) {
            component.config.properties = ComponentProperties(component).copyFrom(this)
            listeners.forEach { it() }
        }
    }

    @JvmOverloads
    public fun animateX(
        easing: Easing,
        time: AnimationTime,
        newValue: XProperty,
        delay: AnimationTime = AnimationTime.ZERO
    ): ComponentAnimationProperties = apply {
        val (totalFrames, delayFrames) = calcFrames(time, delay)
        x = XAnimatingProperty(
            easing,
            totalFrames,
            x,
            newValue,
            delayFrames
        )
    }

    @JvmOverloads
    public fun animateY(
        easing: Easing,
        time: AnimationTime,
        newValue: YProperty,
        delay: AnimationTime = AnimationTime.ZERO
    ): ComponentAnimationProperties = apply {
        val (totalFrames, delayFrames) = calcFrames(time, delay)
        y = YAnimatingProperty(
            easing,
            totalFrames,
            y,
            newValue,
            delayFrames
        )
    }

    @JvmOverloads
    public fun animateWidth(
        easing: Easing,
        duration: AnimationTime,
        newValue: WidthProperty,
        delay: AnimationTime = AnimationTime.ZERO
    ): ComponentAnimationProperties = apply {
        val (totalFrames, delayFrames) = calcFrames(duration, delay)
        width = WidthAnimatingProperty(
            easing,
            totalFrames,
            width,
            newValue,
            delayFrames
        )
    }

    @JvmOverloads
    public fun animateHeight(
        easing: Easing,
        duration: AnimationTime,
        newValue: HeightProperty,
        delay: AnimationTime = AnimationTime.ZERO
    ): ComponentAnimationProperties = apply {
        val (totalFrames, delayFrames) = calcFrames(duration, delay)
        height = HeightAnimatingProperty(
            easing,
            totalFrames,
            height,
            newValue,
            delayFrames
        )
    }



    @JvmOverloads
    public fun animateFill(
        easing: Easing,
        duration: AnimationTime,
        newValue: ColorProperty,
        delay: AnimationTime = AnimationTime.ZERO
    ): ComponentAnimationProperties = apply {
        val (totalFrames, delayFrames) = calcFrames(duration, delay)
        fill = ColorAnimatingProperty(
            easing,
            totalFrames,
            fill,
            newValue,
            delayFrames
        )
    }

    // TODO
    // @JvmOverloads
    // public fun animateStroke(
    //     easing: Easing,
    //     duration: AnimationTime,
    //     newValue: StrokeProperty,
    //     delay: AnimationTime = AnimationTime.ZERO
    // ): ComponentAnimationProperties = apply {
    //     val (totalFrames, delayFrames) = calcFrames(duration, delay)
    //     stroke = StrokeAnimationProperty(
    //         easing,
    //         totalFrames,
    //         stroke,
    //         newValue,
    //         delayFrames
    //     )
    // }

    @JvmOverloads
    public fun animateTopLeftRadius(
        easing: Easing,
        duration: AnimationTime,
        newValue: RadialProperty,
        delay: AnimationTime = AnimationTime.ZERO
    ): ComponentAnimationProperties = apply {
        val (totalFrames, delayFrames) = calcFrames(duration, delay)
        topLeftRadius = RadialAnimatingProperty(
            easing,
            totalFrames,
            topLeftRadius,
            newValue,
            delayFrames
        )
    }

    @JvmOverloads
    public fun animateTopRightRadius(
        easing: Easing,
        duration: AnimationTime,
        newValue: RadialProperty,
        delay: AnimationTime = AnimationTime.ZERO
    ): ComponentAnimationProperties = apply {
        val (totalFrames, delayFrames) = calcFrames(duration, delay)
        topRightRadius = RadialAnimatingProperty(
            easing,
            totalFrames,
            topRightRadius,
            newValue,
            delayFrames
        )
    }

    @JvmOverloads
    public fun animateBottomRightRadius(
        easing: Easing,
        duration: AnimationTime,
        newValue: RadialProperty,
        delay: AnimationTime = AnimationTime.ZERO
    ): ComponentAnimationProperties = apply {
        val (totalFrames, delayFrames) = calcFrames(duration, delay)
        bottomRightRadius = RadialAnimatingProperty(
            easing,
            totalFrames,
            bottomRightRadius,
            newValue,
            delayFrames
        )
    }

    @JvmOverloads
    public fun animateBottomLeftRadius(
        easing: Easing,
        duration: AnimationTime,
        newValue: RadialProperty,
        delay: AnimationTime = AnimationTime.ZERO
    ): ComponentAnimationProperties = apply {
        val (totalFrames, delayFrames) = calcFrames(duration, delay)
        bottomLeftRadius = RadialAnimatingProperty(
            easing,
            totalFrames,
            bottomLeftRadius,
            newValue,
            delayFrames
        )
    }

    @JvmOverloads
    public fun animateAngle(
        easing: Easing,
        duration: AnimationTime,
        newValue: AngleProperty,
        delay: AnimationTime = AnimationTime.ZERO
    ): ComponentAnimationProperties = apply {
        val (totalFrames, delayFrames) = calcFrames(duration, delay)
        angle = AngleAnimatingProperty(
            easing,
            totalFrames,
            angle,
            newValue,
            delayFrames
        )
    }

    private fun calcFrames(
        duration: AnimationTime,
        delay: AnimationTime
    ): Pair<Int, Int> {
        val engine = Engine.get(component)
        val animationFps = engine.renderEngine.animationFps

        val durationSeconds = duration.unit.toSeconds(duration.value)
        val delaySeconds = delay.unit.toSeconds(delay.value)

        val totalFrames = durationSeconds * animationFps
        val delayFrames = delaySeconds * animationFps

        return Pair(totalFrames.toInt(), delayFrames.toInt())
    }

}
