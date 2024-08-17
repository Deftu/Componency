package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component

public data class KeyboardModifiers(
    public val isShift: Boolean,
    public val isCtrl: Boolean,
    public val isAlt: Boolean,
    public val isSuper: Boolean,
)

public data class KeyPressEvent(
    override val component: Component,
    public val keyCode: Int,
    public val typedChar: Char,
    public val modifiers: KeyboardModifiers,
) : CancellableComponentEvent(component)
