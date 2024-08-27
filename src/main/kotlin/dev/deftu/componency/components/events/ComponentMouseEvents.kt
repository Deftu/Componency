package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component
import dev.deftu.componency.input.MouseButton

private sealed interface PositionedMouseEvent {

    val component: Component
    val x: Double
    val y: Double

    val relativeX: Double
        get() = x - component.left

    val relativeY: Double
        get() = y - component.top

}

public data class MouseClickEvent(
    override val component: Component,
    override val x: Double,
    override val y: Double,
    public val button: MouseButton,
    public val clickCount: Int
) : CancellableComponentEvent(component), PositionedMouseEvent

public data class MouseReleaseEvent(
    override val component: Component,
    override val x: Double,
    override val y: Double,
    public val button: MouseButton
) : CancellableComponentEvent(component), PositionedMouseEvent

public data class MouseDragEvent(
    override val component: Component,
    override val x: Double,
    override val y: Double,
    public val button: MouseButton
) : CancellableComponentEvent(component), PositionedMouseEvent

public data class MouseHoverEvent(
    override val component: Component,
    override val x: Double,
    override val y: Double
) : ComponentEvent(component), PositionedMouseEvent

public data class MouseScrollEvent(
    override val component: Component,
    public val delta: Double
) : CancellableComponentEvent(component)
