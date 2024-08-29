package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.StrokeProperty
import dev.deftu.componency.styling.Stroke
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf

public class StaticStrokeProperty(
    valueState: State<Stroke>
) : StrokeProperty {

    override var cachedValue: Stroke = Stroke.NONE
    override var needsRecalculate: Boolean = true

    private val valueState = mappedMutableStateOf(valueState) { it }

    public var value: Stroke by mutableStateBound(this.valueState)

    public constructor(value: Stroke) : this(stateOf(value))

    override fun calculateStroke(component: Component): Stroke {
        return value
    }

}
