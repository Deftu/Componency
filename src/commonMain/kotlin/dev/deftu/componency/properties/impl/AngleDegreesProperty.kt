package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.AngleProperty
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf

public class AngleDegreesProperty(
    valueState: State<Float>
) : AngleProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    private val valueState = mappedMutableStateOf(valueState) { it }

    public var value: Float by mutableStateBound(this.valueState)

    public constructor(value: Number) : this(stateOf(value.toFloat()))

    override fun calculateAngle(component: Component<*, *>): Float {
        return value
    }

    public fun rebindValue(valueState: State<Float>) {
        this.valueState.rebind(valueState)
    }

}
