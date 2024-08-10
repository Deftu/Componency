package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.engine.Engine
import dev.deftu.componency.properties.PositionalProperty
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf

public class MousePositionProperty(
    centerAlignState: State<Boolean>
) : PositionalProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    private val centerAlignState = mappedMutableStateOf(centerAlignState) { it }

    public var centerAlign: Boolean by mutableStateBound(this.centerAlignState)

    public constructor(centerAlign: Boolean) : this(stateOf(centerAlign))

    public constructor() : this(false)

    override fun calculateX(component: Component): Float {
        val mouseX = Engine.get(component).inputEngine.mouseX
        println("Mouse X: $mouseX")
        return if (centerAlign) {
            mouseX - (component.width / 2)
        } else {
            mouseX
        }
    }

    override fun calculateY(component: Component): Float {
        val mouseY = Engine.get(component).inputEngine.mouseY
        println("Mouse Y: $mouseY")
        return if (centerAlign) {
            mouseY - (component.height / 2)
        } else {
            mouseY
        }
    }

}
