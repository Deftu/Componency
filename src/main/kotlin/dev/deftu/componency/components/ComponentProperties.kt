package dev.deftu.componency.components

import dev.deftu.componency.color.Color
import dev.deftu.componency.dsl.asProperty
import dev.deftu.componency.dsl.deg
import dev.deftu.componency.dsl.px
import dev.deftu.componency.easings.Easing
import dev.deftu.componency.font.Font
import dev.deftu.componency.properties.*
import dev.deftu.componency.properties.impl.*
import dev.deftu.componency.stroke.Stroke
import dev.deftu.componency.time.AnimationTime
import org.intellij.lang.annotations.Pattern
import java.lang.reflect.Field
import kotlin.reflect.KMutableProperty

public open class ComponentProperties<T : Component<T, C>, C : ComponentProperties<T, C>>(
    public val component: Component<T, C>
) {

    public companion object {

        public const val NAME_REGEX: String = "[a-z0-9_]+"

    }

    private val animatingProperties: MutableSet<Pair<(Property<*>) -> Unit, () -> Property<*>>> = mutableSetOf()

    @field:Pattern(NAME_REGEX)
    public var name: String? = null
        set(value) {
            if (value != null && !value.matches(NAME_REGEX.toRegex())) {
                throw IllegalArgumentException("Name must match regex $NAME_REGEX")
            }

            field = value
        }

    public var x: XProperty = 0.px

    public var y: YProperty = 0.px

    public var width: WidthProperty = 0.px

    public var height: HeightProperty = 0.px

    public var fill: ColorProperty = Color.WHITE.asProperty

    public var stroke: StrokeProperty = Stroke.NONE.asProperty

    public var radius: RadialProperty
        get() {
            if (topLeftRadius == topRightRadius && topRightRadius == bottomRightRadius && bottomRightRadius == bottomLeftRadius) {
                return topLeftRadius
            } else {
                error("Radius values are mixed")
            }
        }
        set(value) {
            topLeftRadius = value
            topRightRadius = value
            bottomRightRadius = value
            bottomLeftRadius = value
        }

    public var topLeftRadius: RadialProperty = 0.px

    public var topRightRadius: RadialProperty = 0.px

    public var bottomLeftRadius: RadialProperty = 0.px

    public var bottomRightRadius: RadialProperty = 0.px

    public var angle: AngleProperty = 0.deg

    public var font: Font? = null

    public var fontSize: HeightProperty = 0.px

    init {
        component.parent?.properties?.let(::inheritFrom)

        defineAnimatingProperty(::x)
        defineAnimatingProperty(::y)
        defineAnimatingProperty(::width)
        defineAnimatingProperty(::height)
        defineAnimatingProperty(::fill)
        defineAnimatingProperty(::stroke)
        defineAnimatingProperty(::topLeftRadius)
        defineAnimatingProperty(::topRightRadius)
        defineAnimatingProperty(::bottomLeftRadius)
        defineAnimatingProperty(::bottomRightRadius)
        defineAnimatingProperty(::angle)
    }

    public open fun animationFrame(deltaTime: Float) {
        x.animationFrame(deltaTime)
        y.animationFrame(deltaTime)
        width.animationFrame(deltaTime)
        height.animationFrame(deltaTime)
        fill.animationFrame(deltaTime)
        stroke.animationFrame(deltaTime)
        topLeftRadius.animationFrame(deltaTime)
        topRightRadius.animationFrame(deltaTime)
        bottomLeftRadius.animationFrame(deltaTime)
        bottomRightRadius.animationFrame(deltaTime)
        angle.animationFrame(deltaTime)

        var isStillAnimating = false
        for ((setter, getter) in animatingProperties) {
            val value = getter.invoke()
            if (value !is AnimatingProperty<*, *>) {
                continue
            }

            if (value.isFinished) {
                setter.invoke(value.to)
            } else {
                isStillAnimating = true
            }
        }

        // TODO
    }

    public open fun recalculate() {
        x.needsRecalculate = true
        y.needsRecalculate = true
        width.needsRecalculate = true
        height.needsRecalculate = true
        fill.needsRecalculate = true
        stroke.needsRecalculate = true
        topLeftRadius.needsRecalculate = true
        topRightRadius.needsRecalculate = true
        bottomLeftRadius.needsRecalculate = true
        bottomRightRadius.needsRecalculate = true
        angle.needsRecalculate = true
    }

    public open fun inheritFrom(parent: ComponentProperties<*, *>) {
        font = parent.font
        fontSize = parent.fontSize
    }

    public open fun copyFrom(properties: ComponentProperties<*, *>): ComponentProperties<T, C> = apply {
        x = properties.x
        y = properties.y
        width = properties.width
        height = properties.height
        fill = properties.fill
        stroke = properties.stroke
        topLeftRadius = properties.topLeftRadius
        topRightRadius = properties.topRightRadius
        bottomLeftRadius = properties.bottomLeftRadius
        bottomRightRadius = properties.bottomRightRadius
        angle = properties.angle
        font = properties.font
        fontSize = properties.fontSize
    }

    public fun animateX(
        easing: Easing,
        duration: AnimationTime,
        to: XProperty
    ) {
        x = AnimatingXProperty(
            easing = easing,
            totalFrames = calculateFrameTime(duration).toInt(),
            from = x,
            to = to
        )
    }

    public fun animateY(
        easing: Easing,
        duration: AnimationTime,
        to: YProperty
    ) {
        y = AnimatingYProperty(
            easing = easing,
            totalFrames = calculateFrameTime(duration).toInt(),
            from = y,
            to = to
        )
    }

    public fun animateWidth(
        easing: Easing,
        duration: AnimationTime,
        to: WidthProperty
    ) {
        width = AnimatingWidthProperty(
            easing = easing,
            totalFrames = calculateFrameTime(duration).toInt(),
            from = width,
            to = to
        )
    }

    public fun animateHeight(
        easing: Easing,
        duration: AnimationTime,
        to: HeightProperty
    ) {
        height = AnimatingHeightProperty(
            easing = easing,
            totalFrames = calculateFrameTime(duration).toInt(),
            from = height,
            to = to
        )
    }

    public fun animateFill(
        easing: Easing,
        duration: AnimationTime,
        to: ColorProperty
    ) {
        fill = AnimatingColorProperty(
            easing = easing,
            totalFrames = calculateFrameTime(duration).toInt(),
            from = fill,
            to = to
        )
    }

    public fun animateStroke(
        easing: Easing,
        duration: AnimationTime,
        to: StrokeProperty
    ) {
        stroke = AnimatingStrokeProperty(
            easing = easing,
            totalFrames = calculateFrameTime(duration).toInt(),
            from = stroke,
            to = to
        )
    }

    public fun animateTopLeftRadius(
        easing: Easing,
        duration: AnimationTime,
        to: RadialProperty
    ) {
        topLeftRadius = AnimatingRadialProperty(
            easing = easing,
            totalFrames = calculateFrameTime(duration).toInt(),
            from = topLeftRadius,
            to = to
        )
    }

    public fun animateTopRightRadius(
        easing: Easing,
        duration: AnimationTime,
        to: RadialProperty
    ) {
        topRightRadius = AnimatingRadialProperty(
            easing = easing,
            totalFrames = calculateFrameTime(duration).toInt(),
            from = topRightRadius,
            to = to
        )
    }

    public fun animateBottomLeftRadius(
        easing: Easing,
        duration: AnimationTime,
        to: RadialProperty
    ) {
        bottomLeftRadius = AnimatingRadialProperty(
            easing = easing,
            totalFrames = calculateFrameTime(duration).toInt(),
            from = bottomLeftRadius,
            to = to
        )
    }

    public fun animateBottomRightRadius(
        easing: Easing,
        duration: AnimationTime,
        to: RadialProperty
    ) {
        bottomRightRadius = AnimatingRadialProperty(
            easing = easing,
            totalFrames = calculateFrameTime(duration).toInt(),
            from = bottomRightRadius,
            to = to
        )
    }

    public fun animateAngle(
        easing: Easing,
        duration: AnimationTime,
        to: AngleProperty
    ) {
        angle = AnimatingAngleProperty(
            easing = easing,
            totalFrames = calculateFrameTime(duration).toInt(),
            from = angle,
            to = to
        )
    }

    public inline fun XProperty.animateTo(
        easing: Easing,
        duration: AnimationTime,
        to: XProperty,
        block: XProperty.() -> Unit = {}
    ) {
        animateX(easing, duration, to)
        block(this)
    }

    public inline fun YProperty.animateTo(
        easing: Easing,
        duration: AnimationTime,
        to: YProperty,
        block: YProperty.() -> Unit = {}
    ) {
        animateY(easing, duration, to)
        block(this)
    }

    public fun WidthProperty.animateTo(
        easing: Easing,
        duration: AnimationTime,
        to: WidthProperty,
        block: WidthProperty.() -> Unit = {}
    ) {
        animateWidth(easing, duration, to)
        block(this)
    }

    public inline fun HeightProperty.animateTo(
        easing: Easing,
        duration: AnimationTime,
        to: HeightProperty,
        block: HeightProperty.() -> Unit = {}
    ) {
        animateHeight(easing, duration, to)
        block(this)
    }

    public inline fun ColorProperty.animateTo(
        easing: Easing,
        duration: AnimationTime,
        to: ColorProperty,
        block: ColorProperty.() -> Unit = {}
    ) {
        animateFill(easing, duration, to)
        block(this)
    }

    public inline fun StrokeProperty.animateTo(
        easing: Easing,
        duration: AnimationTime,
        to: StrokeProperty,
        block: StrokeProperty.() -> Unit = {}
    ) {
        animateStroke(easing, duration, to)
        block(this)
    }

    public inline fun RadialProperty.animateTo(
        easing: Easing,
        duration: AnimationTime,
        to: RadialProperty,
        block: RadialProperty.() -> Unit = {}
    ) {
        animateTopLeftRadius(easing, duration, to)
        block(this)
    }

    public inline fun AngleProperty.animateTo(
        easing: Easing,
        duration: AnimationTime,
        to: AngleProperty,
        block: AngleProperty.() -> Unit = {}
    ) {
        animateAngle(easing, duration, to)
        block(this)
    }

    protected fun defineAnimatingProperty(field: Field) {
        field.isAccessible = true
        animatingProperties.add({ newValue: Property<*> ->
            field.set(this, newValue)
        } to {
            field.get(this) as Property<*>
        })
    }

    protected fun defineAnimatingProperty(property: KMutableProperty<*>) {
        animatingProperties.add({ newValue: Property<*> ->
            property.setter.call(newValue)
        } to {
            property.getter.call() as Property<*>
        })
    }

    private fun calculateFrameTime(duration: AnimationTime): Double {
        return (duration.millis / 1000f) * Component.findPlatform(component).targetFramerate
    }

}
