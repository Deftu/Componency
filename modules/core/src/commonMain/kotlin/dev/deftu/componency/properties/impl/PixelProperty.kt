package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.VectorProperty
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf

public class PixelProperty(
    valueState: State<Float>,
    inverseState: State<Boolean>,
) : VectorProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    private val valueState = mappedMutableStateOf(valueState) { it }
    private val inverseState = mappedMutableStateOf(inverseState) { it }

    public var value: Float by mutableStateBound(this.valueState)
    public var isInverse: Boolean by mutableStateBound(this.inverseState)

    public constructor(value: Number) : this(stateOf(value.toFloat()), stateOf(false))
    public constructor(value: Float, isInverse: Boolean) : this(stateOf(value), stateOf(isInverse))
    public constructor(value: Number, isInverse: Boolean) : this(value.toFloat(), isInverse)

    override fun calculateX(component: Component<*, *>): Float {
        return if (isInverse) {
            (component.parent?.right ?: 0f) - value - component.width
        } else {
            (component.parent?.left ?: 0f) + value
        }
    }

    override fun calculateY(component: Component<*, *>): Float {
        return if (isInverse) {
            (component.parent?.bottom ?: 0f) - value - component.height
        } else {
            (component.parent?.top ?: 0f) + value
        }
    }

    override fun calculateWidth(component: Component<*, *>): Float {
        return value
    }

    override fun calculateHeight(component: Component<*, *>): Float {
        return value
    }

    override fun calculateRadius(component: Component<*, *>): Float {
        return value
    }

    public fun rebindValue(valueState: State<Float>) {
        this.valueState.rebind(valueState)
    }

    public fun rebindInverse(inverseState: State<Boolean>) {
        this.inverseState.rebind(inverseState)
    }

}
