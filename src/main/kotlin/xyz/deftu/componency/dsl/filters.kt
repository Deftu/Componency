package xyz.deftu.componency.dsl

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.componency.filters.GaussianBlurFilter

fun <T : BaseComponent> ConfigurationScope<T>.gaussianBlur(radius: Float) = GaussianBlurFilter(radius)
