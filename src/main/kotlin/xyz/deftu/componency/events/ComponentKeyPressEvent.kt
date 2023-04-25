package xyz.deftu.componency.events

import xyz.deftu.multi.MultiKeyboard

data class ComponentKeyPressEvent(
    val keyCode: Int,
    val typedChar: Char,
    val modifiers: MultiKeyboard.KeyboardModifiers
) {
    var isCancelled = false
}
