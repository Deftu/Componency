package dev.deftu.componency.dsl

import dev.deftu.componency.components.BaseComponent
import dev.deftu.componency.effects.ScissorEffect
import java.awt.Color

fun <T : BaseComponent> ConfigurationScope<T>.scissor(
    x1: Int,
    y1: Int,
    x2: Int,
    y2: Int,
    scissorIntersection: Boolean = true
) = ScissorEffect(x1, y1, x2, y2, scissorIntersection)

fun <T : BaseComponent> ConfigurationScope<T>.scissor(
    boundingBox: BaseComponent? = null,
    scissorIntersection: Boolean = true
) = ScissorEffect(boundingBox, scissorIntersection)
