package xyz.deftu.componency.constraints

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.multi.MultiMouse
import xyz.deftu.state.SimpleState
import xyz.deftu.state.State

class MouseBasedConstraint(
    alignCentered: State<Boolean> = SimpleState(false)
) : PositionalConstraint {
    constructor(alignCentered: Boolean) : this(SimpleState(alignCentered))

    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    private val alignCenteredState = alignCentered.map { it }
    private var alignCentered: Boolean
        get() = alignCenteredState.get()
        set(value) {
            alignCenteredState.set(value)
        }

    fun bindAlignCentered(alignCentered: State<Boolean>) = apply {
        alignCenteredState.rebind(alignCentered)
    }

    override fun getImplValueForX(component: BaseComponent): Float {
        return if (alignCentered) MultiMouse.scaledX.toFloat() - component.getWidth() / 2 else MultiMouse.scaledX.toFloat()
    }

    override fun getImplValueForY(component: BaseComponent): Float {
        return if (alignCentered) MultiMouse.scaledY.toFloat() - component.getHeight() / 2 else MultiMouse.scaledY.toFloat()
    }
}
