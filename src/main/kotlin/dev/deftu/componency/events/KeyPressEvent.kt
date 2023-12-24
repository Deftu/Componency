package dev.deftu.componency.events

import dev.deftu.multi.MultiKeyboard

data class KeyPressEvent(
    val keyCode: Int,
    val typedChar: Char,
    val modifiers: MultiKeyboard.KeyboardModifiers
) : CancellableComponentEvent()
