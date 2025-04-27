package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.input.MouseButton

private sealed interface PositionedPointerEvent {

    val component: Component<*, *>
    val x: Double
    val y: Double

    val relativeX: Double
        get() = x - component.left

    val relativeY: Double
        get() = y - component.top

}

public data class PointerClickEvent(
    override val component: Component<*, *>,
    override val x: Double,
    override val y: Double,
    public val button: MouseButton,
    public val clickCount: Int
) : CancellableComponentEvent(component), PositionedPointerEvent

public data class PointerReleaseEvent(
    override val component: Component<*, *>,
    override val x: Double,
    override val y: Double,
    public val button: MouseButton
) : CancellableComponentEvent(component), PositionedPointerEvent

public data class PointerDragEvent(
    override val component: Component<*, *>,
    override val x: Double,
    override val y: Double,
    public val button: MouseButton
) : CancellableComponentEvent(component), PositionedPointerEvent

public data class HoverEvent(
    override val component: Component<*, *>,
    override val x: Double,
    override val y: Double
) : ComponentEvent(component), PositionedPointerEvent

public data class ScrollEvent(
    override val component: Component<*, *>,
    public val delta: Double
) : CancellableComponentEvent(component)
