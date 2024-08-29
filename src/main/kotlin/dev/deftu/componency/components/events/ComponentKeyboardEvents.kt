package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component
import dev.deftu.componency.input.Key

public data class KeyboardModifiers(
    public val isShift: Boolean,
    public val isCtrl: Boolean,
    public val isAlt: Boolean,
    public val isSuper: Boolean,
)

public data class KeyPressEvent(
    override val component: Component,
    public val key: Key,
    public val modifiers: KeyboardModifiers,
) : CancellableComponentEvent(component)

public data class KeyReleaseEvent(
    override val component: Component,
    public val key: Key,
    public val modifiers: KeyboardModifiers,
) : CancellableComponentEvent(component)
