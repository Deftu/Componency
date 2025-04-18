package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.VectorProperty
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf

public class RootRelativeProperty(
    valueState: State<Float>
) : VectorProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    private val valueState = mappedMutableStateOf(valueState) { it }

    public var value: Float by mutableStateBound(this.valueState)

    public constructor(value: Number) : this(stateOf(value.toFloat()))

    override fun calculateX(component: Component<*, *>): Float {
        return Component.findRoot(component).left + getWidth(component)
    }

    override fun calculateY(component: Component<*, *>): Float {
        return Component.findRoot(component).top + getHeight(component)
    }

    override fun calculateWidth(component: Component<*, *>): Float {
        return Component.findRoot(component).width * value
    }

    override fun calculateHeight(component: Component<*, *>): Float {
        return Component.findRoot(component).height * value
    }

    override fun calculateRadius(component: Component<*, *>): Float {
        return (Component.findRoot(component).width * value) / 2
    }

}
