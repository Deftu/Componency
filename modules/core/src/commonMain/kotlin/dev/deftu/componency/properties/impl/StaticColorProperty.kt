package dev.deftu.componency.properties.impl

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.ColorProperty
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf
import kotlin.math.roundToInt

public class StaticColorProperty(
    valueState: State<Color>
) : ColorProperty {

    override var cachedValue: Color = Color.WHITE
    override var needsRecalculate: Boolean = true

    private val valueState = mappedMutableStateOf(valueState) { it }

    public var value: Color by mutableStateBound(this.valueState)

    public constructor(color: Color) : this(stateOf(color))

    override fun calculateColor(component: Component<*, *>): Color {
        return value
    }

    public fun bindValue(valueState: State<Color>) {
        this.valueState.rebind(valueState)
    }

}
