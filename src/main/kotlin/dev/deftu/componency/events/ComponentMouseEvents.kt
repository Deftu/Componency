package dev.deftu.componency.events

data class MouseClickEvent(
    val x: Double,
    val y: Double,
    val button: Int
) : CancellableComponentEvent()

data class MouseReleaseEvent(
    val x: Double,
    val y: Double,
    val button: Int
) : CancellableComponentEvent()

data class MouseDragEvent(
    val x: Double,
    val y: Double,
    val button: Int
) : CancellableComponentEvent()

data class MouseHoverEvent(
    val x: Double,
    val y: Double
) : ComponentEvent()

data class ComponentMouseScrollEvent(
    val x: Double,
    val y: Double,
    val delta: Double
) : CancellableComponentEvent()
