package dev.deftu.componency.utils

import dev.deftu.componency.components.Component

public fun <T : Component> T.attachTo(parent: Component): T = apply {
    parent.addChild(this)
}

public fun <T : Component> T.detachFromParent(): T = apply {
    parent?.removeChild(this)
}
