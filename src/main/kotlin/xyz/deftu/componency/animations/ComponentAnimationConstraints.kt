package xyz.deftu.componency.animations

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.componency.components.WindowComponent
import xyz.deftu.componency.constraints.*

class ComponentAnimationConstraints(
    component: BaseComponent,
    constraints: ComponentConstraints
) : ComponentConstraints(component) {
    private val completionListeners = mutableListOf<() -> Unit>()

    init {
        copyFrom(constraints)
        fontProvider.setAnimating(true)
    }

    fun whenComplete(listener: () -> Unit) = apply {
        completionListeners.add(listener)
    }

    fun animateX(
        handler: AnimationHandler,
        time: Float,
        newValue: XConstraint,
        delay: Float = 0f
    ) = apply {
        val frames = getTotalAndDelayFrames(time, delay)
        x = XAnimationConstraint(
            handler,
            frames.first,
            x,
            newValue,
            frames.second
        )
    }

    fun animateY(
        handler: AnimationHandler,
        time: Float,
        newValue: YConstraint,
        delay: Float = 0f
    ) = apply {
        val frames = getTotalAndDelayFrames(time, delay)
        y = YAnimationConstraint(
            handler,
            frames.first,
            y,
            newValue,
            frames.second
        )
    }

    fun animateWidth(
        handler: AnimationHandler,
        time: Float,
        newValue: WidthConstraint,
        delay: Float = 0f
    ) = apply {
        val frames = getTotalAndDelayFrames(time, delay)
        width = WidthAnimationConstraint(
            handler,
            frames.first,
            width,
            newValue,
            frames.second
        )
    }

    fun animateHeight(
        handler: AnimationHandler,
        time: Float,
        newValue: HeightConstraint,
        delay: Float = 0f
    ) = apply {
        val frames = getTotalAndDelayFrames(time, delay)
        height = HeightAnimationConstraint(
            handler,
            frames.first,
            height,
            newValue,
            frames.second
        )
    }

    fun animateRadius(
        handler: AnimationHandler,
        time: Float,
        newValue: RadiusConstraint,
        delay: Float = 0f
    ) = apply {
        val frames = getTotalAndDelayFrames(time, delay)
        radius = RadiusAnimationConstraint(
            handler,
            frames.first,
            radius,
            newValue,
            frames.second
        )
    }

    fun animateColor(
        handler: AnimationHandler,
        time: Float,
        newValue: ColorConstraint,
        delay: Float = 0f
    ) = apply {
        val frames = getTotalAndDelayFrames(time, delay)
        color = ColorAnimationConstraint(
            handler,
            frames.first,
            color,
            newValue,
            frames.second
        )
    }

    private fun getTotalAndDelayFrames(
        time: Float,
        delay: Float
    ): Pair<Int, Int> {
        val window = WindowComponent.find(component)

        val total = time * window.trueAnimationFramerate
        val delay = delay * window.trueAnimationFramerate

        return Pair(total.toInt(), delay.toInt())
    }

    override fun handleAnimate() {
        super.handleAnimate()
        fontProvider.setAnimating(true)

        var stillAnimating = false

        if (x is XAnimationConstraint) {
            val x = x as XAnimationConstraint
            if (x.isFinished()) {
                this.x = x.new
            } else stillAnimating = true
        }

        if (y is YAnimationConstraint) {
            val y = y as YAnimationConstraint
            if (y.isFinished()) {
                this.y = y.new
            } else stillAnimating = true
        }

        if (width is WidthAnimationConstraint) {
            val width = width as WidthAnimationConstraint
            if (width.isFinished()) {
                this.width = width.new
            } else stillAnimating = true
        }

        if (height is HeightAnimationConstraint) {
            val height = height as HeightAnimationConstraint
            if (height.isFinished()) {
                this.height = height.new
            } else stillAnimating = true
        }

        if (radius is RadiusAnimationConstraint) {
            val radius = radius as RadiusAnimationConstraint
            if (radius.isFinished()) {
                this.radius = radius.new
            } else stillAnimating = true
        }

        if (color is ColorAnimationConstraint) {
            val color = color as ColorAnimationConstraint
            if (color.isFinished()) {
                this.color = color.new
            } else stillAnimating = true
        }

        if (!stillAnimating) {
            // Reset constraints
            fontProvider.setAnimating(false)
            component.constraints = ComponentConstraints(component)
            component.constraints.copyFrom(this)

            // Call completion listeners
            completionListeners.forEach { it() }
            completionListeners.clear()
        }
    }
}
