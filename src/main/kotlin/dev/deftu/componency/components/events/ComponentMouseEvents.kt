package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component

public class MouseClickEvent(
    component: Component,
    public val x: Double,
    public val y: Double,
    public val button: Int
) : CancellableComponentEvent(component)

public class MouseReleaseEvent(
    component: Component,
    public val x: Double,
    public val y: Double,
    public val button: Int
) : CancellableComponentEvent(component)

public class MouseDragEvent(
    component: Component,
    public val x: Double,
    public val y: Double,
    public val button: Int
) : CancellableComponentEvent(component)

public class MouseHoverEvent(
    component: Component,
    public val x: Double,
    public val y: Double
) : ComponentEvent(component)

public class MouseScrollEvent(
    component: Component,
    public val x: Double,
    public val y: Double,
    public val delta: Double
) : CancellableComponentEvent(component)
