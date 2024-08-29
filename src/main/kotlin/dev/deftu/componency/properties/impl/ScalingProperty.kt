package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.*
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf

public class ScalingProperty(
    private val value: Property<Float>,
    scaleState: State<Float>
) : VectorProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    private val scaleState = mappedMutableStateOf(scaleState) { it }

    public var scale: Float by mutableStateBound(this.scaleState)

    public constructor(
        value: Property<Float>,
        scale: Float
    ) : this(value, stateOf(scale))

    override fun frame() {
        super.frame()
        value.frame()
    }

    override fun calculateX(component: Component): Float {
        val valueX = value as XProperty
        return valueX.getX(component) * scale
    }

    override fun calculateY(component: Component): Float {
        val valueY = value as YProperty
        return valueY.getY(component) * scale
    }

    override fun calculateWidth(component: Component): Float {
        val valueWidth = value as WidthProperty
        return valueWidth.getWidth(component) * scale
    }

    override fun calculateHeight(component: Component): Float {
        val valueHeight = value as HeightProperty
        return valueHeight.getHeight(component) * scale
    }

    override fun calculateRadius(component: Component): Float {
        val valueRadius = value as RadialProperty
        return valueRadius.getRadius(component) * scale
    }

}
