package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component

public data class MouseClickEvent(
    override val component: Component,
    public val x: Double,
    public val y: Double,
    public val button: Int
) : CancellableComponentEvent(component)

public data class MouseReleaseEvent(
    override val component: Component,
    public val x: Double,
    public val y: Double,
    public val button: Int
) : CancellableComponentEvent(component)

public data class MouseDragEvent(
    override val component: Component,
    public val x: Double,
    public val y: Double,
    public val button: Int
) : CancellableComponentEvent(component)

public data class MouseHoverEvent(
    override val component: Component,
    public val x: Double,
    public val y: Double
) : ComponentEvent(component)

public data class MouseScrollEvent(
    override val component: Component,
    public val delta: Double
) : CancellableComponentEvent(component)
