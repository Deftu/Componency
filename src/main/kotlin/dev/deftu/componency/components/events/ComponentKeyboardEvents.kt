package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component

public class KeyPressEvent(
    component: Component,
    public val keyCode: Int,
    public val typedChar: Char,
    // public val modifiers: MultiKeyboard.KeyboardModifiers
) : CancellableComponentEvent(component)
