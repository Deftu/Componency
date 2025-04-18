package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.PositionalProperty
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf

public class PointerPositionProperty(
    centerAlignState: State<Boolean>
) : PositionalProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    private val centerAlignState = mappedMutableStateOf(centerAlignState) { it }

    public var centerAlign: Boolean by mutableStateBound(this.centerAlignState)

    public constructor(centerAlign: Boolean) : this(stateOf(centerAlign))

    public constructor() : this(false)

    override fun calculateX(component: Component<*, *>): Float {
        val pointerX = Component.findPlatform(component).inputHandler.pointerInput.pointerX
        return if (centerAlign) {
            pointerX - (component.width / 2)
        } else {
            pointerX
        }
    }

    override fun calculateY(component: Component<*, *>): Float {
        val pointerY = Component.findPlatform(component).inputHandler.pointerInput.pointerY
        return if (centerAlign) {
            pointerY - (component.height / 2)
        } else {
            pointerY
        }
    }

}
