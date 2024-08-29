package dev.deftu.componency.defign.dsl

import dev.deftu.componency.defign.components.ToggleComponent

public fun <T : ToggleComponent> T.whenChange(block: (Boolean) -> Unit): T = apply {
    changeListeners.add(block)
}
