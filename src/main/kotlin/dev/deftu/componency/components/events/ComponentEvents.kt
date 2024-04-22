package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component

public data class ComponentParentChangeEvent(
    val oldParent: Component?,
    val newParent: Component?
)
