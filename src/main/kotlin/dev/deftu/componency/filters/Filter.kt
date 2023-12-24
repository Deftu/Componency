package dev.deftu.componency.filters

import dev.deftu.componency.components.BaseComponent
import dev.deftu.multi.MultiMatrixStack

interface Filter {
    fun applyPreRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float)
    fun applyPostRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float)

    fun onDestroyed(component: BaseComponent) {
    }

    fun handleAnimate(component: BaseComponent) {
    }
}