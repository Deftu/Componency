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
    public fun animateRadius(
        easing: Easing,
        duration: AnimationTime,
        newValue: RadialProperty,
        delay: AnimationTime = AnimationTime.ZERO
    ): ComponentAnimationProperties = apply {
        val (totalFrames, delayFrames) = calcFrames(duration, delay)
        radius = RadialAnimatingProperty(
            easing,
            totalFrames,
            radius,
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
