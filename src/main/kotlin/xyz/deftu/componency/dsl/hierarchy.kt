package xyz.deftu.componency.dsl

import xyz.deftu.componency.components.BaseComponent

fun <T : BaseComponent> T.attachTo(parent: BaseComponent) = apply {
    parent.attachChild(this)
}
