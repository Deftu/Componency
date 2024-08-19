package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.engine.Engine
import dev.deftu.componency.properties.VectorProperty
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf

public class ParentRelativeProperty(
    valueState: State<Float>
) : VectorProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    private val valueState = mappedMutableStateOf(valueState) { it }

    public var value: Float by mutableStateBound(this.valueState)

    public constructor(value: Number) : this(stateOf(value.toFloat()))

    override fun calculateX(component: Component): Float {
        return if (component.hasParent) {
            component.parent!!.left + getWidth(component)
        } else {
            throw UnsupportedOperationException("Cannot calculate relative X position on root component")
        }
    }

    override fun calculateY(component: Component): Float {
        return if (component.hasParent) {
            component.parent!!.top + getHeight(component)
        } else {
            throw UnsupportedOperationException("Cannot calculate relative Y position on root component")
        }
    }

    override fun calculateWidth(component: Component): Float {
        return if (component.hasParent) {
            component.parent!!.width * value
        } else {
            Engine.get(component).renderEngine.viewportWidth.toFloat()
        }
    }

    override fun calculateHeight(component: Component): Float {
        return if (component.hasParent) {
            component.parent!!.height * value
        } else {
            Engine.get(component).renderEngine.viewportHeight.toFloat()
        }
    }

    override fun calculateRadius(component: Component): Float {
        return if (component.hasParent) {
            (component.parent!!.width * value) / 2
        } else {
            Engine.get(component).renderEngine.viewportWidth.toFloat() / 2
        }
    }

    public fun rebindValue(valueState: State<Float>) {
        this.valueState.rebind(valueState)
    }

}
