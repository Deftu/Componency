package dev.deftu.componency.dsl

import dev.deftu.componency.components.BaseComponent

fun <T : BaseComponent> T.attachTo(parent: BaseComponent) = apply {
    parent.attachChild(this)
}
