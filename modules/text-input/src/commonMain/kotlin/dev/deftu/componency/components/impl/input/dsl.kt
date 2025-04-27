package dev.deftu.componency.components.impl.input

public fun <T : AbstractTextInputComponent> T.whenSubmit(listener: String.() -> Unit): T = apply {
    submitListeners.add(listener)
}
