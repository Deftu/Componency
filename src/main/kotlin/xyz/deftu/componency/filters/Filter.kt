package xyz.deftu.componency.filters

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.multi.MultiMatrixStack

interface Filter {
    fun applyPreRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float)
    fun applyPostRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float)

    fun onDestroyed(component: BaseComponent) {
    }

    fun handleAnimate(component: BaseComponent) {
    }
}