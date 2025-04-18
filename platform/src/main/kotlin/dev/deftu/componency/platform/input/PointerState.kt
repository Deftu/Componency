package dev.deftu.componency.platform.input

import dev.deftu.componency.input.MouseButton

public data class PointerState(
    val index: Int,
    val x: Float,
    val y: Float,
    val pressedButtons: Set<MouseButton> = emptySet(),
)
