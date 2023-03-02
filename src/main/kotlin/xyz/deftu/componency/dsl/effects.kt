package xyz.deftu.componency.dsl

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.componency.effects.Effect
import xyz.deftu.componency.effects.ScissorEffect

fun <T : BaseComponent> ConfigurationScope<T>.scissor(
    x1: Int,
    y1: Int,
    x2: Int,
    y2: Int,
    scissorIntersection: Boolean = true
) = apply {
    effects += ScissorEffect(x1, y1, x2, y2, scissorIntersection)
}

fun <T : BaseComponent> ConfigurationScope<T>.scissor(
    boundingBox: BaseComponent? = null,
    scissorIntersection: Boolean = true
) = apply {
    effects += ScissorEffect(boundingBox, scissorIntersection)
}
