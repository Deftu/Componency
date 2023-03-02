package xyz.deftu.componency.constraints

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.state.SimpleState
import xyz.deftu.state.State
import java.awt.Color

class StaticColorConstraint(
    value: State<Color>
) : ColorConstraint {
    constructor(
        value: Color
    ) : this(SimpleState(value))

    override var cached = Color.WHITE
    override var recalculate = false
    override var attachedTo: BaseComponent? = null

    private val valueState = value.map { it }

    var value: Color
        get() = valueState.get()
        set(value) {
            valueState.set(value)
        }

    fun bindValue(value: State<Color>) = apply {
        valueState.rebind(value)
    }

    override fun getImplValueForColor(component: BaseComponent) = value
}
