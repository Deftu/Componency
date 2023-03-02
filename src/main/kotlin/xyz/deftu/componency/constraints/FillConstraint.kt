package xyz.deftu.componency.constraints

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.state.SimpleState
import xyz.deftu.state.State

class FillConstraint(
    useSiblings: State<Boolean> = SimpleState(true)
) : SizeConstraint {
    constructor(useSiblings: Boolean = true) : this(SimpleState(useSiblings))

    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    private val useSiblingsState = useSiblings.map { it }
    private var useSiblings: Boolean
        get() = useSiblingsState.get()
        set(value) {
            useSiblingsState.set(value)
        }

    fun bindUseSiblings(useSiblings: State<Boolean>) = apply {
        useSiblingsState.rebind(useSiblings)
    }

    override fun getImplValueForWidth(component: BaseComponent): Float {
        val target = attachedTo ?: component.parent
        return if (useSiblings) {
            target.getWidth() - target.children.filter { it != component }.sumOf { it.getWidth().toDouble() }.toFloat()
        } else target.getRight() - component.getX()
    }

    override fun getImplValueForHeight(component: BaseComponent): Float {
        val target = attachedTo ?: component.parent
        return if (useSiblings) {
            target.getHeight() - target.children.filter { it != component }.sumOf { it.getHeight().toDouble() }.toFloat()
        } else target.getBottom() - component.getY()
    }

    override fun getImplValueForRadius(component: BaseComponent): Float {
        val target = attachedTo ?: component.parent
        return if (useSiblings) {
            target.getRadius() - target.children.filter { it != component }.sumOf { it.getRadius().toDouble() }.toFloat()
        } else (target.getRadius() - component.getX()) / 2
    }
}
