package dev.deftu.componency.animations

import dev.deftu.componency.components.BaseComponent
import dev.deftu.componency.constraints.*
import java.awt.Color
import kotlin.math.max
import kotlin.math.roundToInt

internal sealed class AnimationConstraint<T>(
    val handler: AnimationHandler,
    val total: Int,
    val delay: Int
) : Constraint<T> {
    var frames = 0
    var paused = false

    override fun handleAnimate() {
        super.handleAnimate()

        if (
            paused ||
            isFinished()
        ) return
        frames++
    }

    fun pause() = apply {
        paused = true
    }

    fun resume() = apply {
        paused = false
    }

    fun isFinished() = frames - delay >= total
    fun getProgress() = handler.getValue(max(frames, 0).toFloat() / total.toFloat())
}

internal class XAnimationConstraint(
    handler: AnimationHandler,
    frames: Int,
    val old: XConstraint,
    val new: XConstraint,
    delay: Int
) : AnimationConstraint<Float>(handler, frames, delay), XConstraint {
    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    override fun handleAnimate() {
        super<AnimationConstraint>.handleAnimate()
        old.handleAnimate()
        new.handleAnimate()
    }

    override fun getImplValueForX(component: BaseComponent): Float {
        val startX = old.getImplValueForX(component)
        val endX = new.getImplValueForX(component)
        return startX + (endX - startX) * getProgress()
    }
}

internal class YAnimationConstraint(
    handler: AnimationHandler,
    frames: Int,
    val old: YConstraint,
    val new: YConstraint,
    delay: Int
) : AnimationConstraint<Float>(handler, frames, delay), YConstraint {
    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    override fun handleAnimate() {
        super<AnimationConstraint>.handleAnimate()
        old.handleAnimate()
        new.handleAnimate()
    }

    override fun getImplValueForY(component: BaseComponent): Float {
        val startY = old.getImplValueForY(component)
        val endY = new.getImplValueForY(component)
        return startY + (endY - startY) * getProgress()
    }
}

internal class WidthAnimationConstraint(
    handler: AnimationHandler,
    frames: Int,
    val old: WidthConstraint,
    val new: WidthConstraint,
    delay: Int
) : AnimationConstraint<Float>(handler, frames, delay), WidthConstraint {
    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    override fun handleAnimate() {
        super<AnimationConstraint>.handleAnimate()
        old.handleAnimate()
        new.handleAnimate()
    }

    override fun getImplValueForWidth(component: BaseComponent): Float {
        val startWidth = old.getImplValueForWidth(component)
        val endWidth = new.getImplValueForWidth(component)
        return startWidth + (endWidth - startWidth) * getProgress()
    }
}

internal class HeightAnimationConstraint(
    handler: AnimationHandler,
    frames: Int,
    val old: HeightConstraint,
    val new: HeightConstraint,
    delay: Int
) : AnimationConstraint<Float>(handler, frames, delay), HeightConstraint {
    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    override fun handleAnimate() {
        super<AnimationConstraint>.handleAnimate()
        old.handleAnimate()
        new.handleAnimate()
    }

    override fun getImplValueForHeight(component: BaseComponent): Float {
        val startHeight = old.getImplValueForHeight(component)
        val endHeight = new.getImplValueForHeight(component)
        return startHeight + (endHeight - startHeight) * getProgress()
    }
}

internal class RadiusAnimationConstraint(
    handler: AnimationHandler,
    frames: Int,
    val old: RadiusConstraint,
    val new: RadiusConstraint,
    delay: Int
) : AnimationConstraint<Float>(handler, frames, delay), RadiusConstraint {
    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    override fun handleAnimate() {
        super<AnimationConstraint>.handleAnimate()
        old.handleAnimate()
        new.handleAnimate()
    }

    override fun getImplValueForRadius(component: BaseComponent): Float {
        val startRadius = old.getImplValueForRadius(component)
        val endRadius = new.getImplValueForRadius(component)
        return startRadius + (endRadius - startRadius) * getProgress()
    }
}

internal class ColorAnimationConstraint(
    handler: AnimationHandler,
    frames: Int,
    val old: ColorConstraint,
    val new: ColorConstraint,
    delay: Int
) : AnimationConstraint<Color>(handler, frames, delay), ColorConstraint {
    override var cached = Color.WHITE
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    override fun handleAnimate() {
        super<AnimationConstraint>.handleAnimate()
        old.handleAnimate()
        new.handleAnimate()
    }

    override fun getImplValueForColor(component: BaseComponent): Color {
        val startColor = old.getImplValueForColor(component)
        val endColor = new.getImplValueForColor(component)

        val newRed = startColor.red + (endColor.red - startColor.red) * getProgress()
        val newGreen = startColor.green + (endColor.green - startColor.green) * getProgress()
        val newBlue = startColor.blue + (endColor.blue - startColor.blue) * getProgress()
        val newAlpha = startColor.alpha + (endColor.alpha - startColor.alpha) * getProgress()

        return Color(newRed.roundToInt(), newGreen.roundToInt(), newBlue.roundToInt(), newAlpha.roundToInt())
    }
}
