package dev.deftu.componency.effects

import dev.deftu.componency.components.BaseComponent
import dev.deftu.multi.MultiMatrixStack

interface Effect {
    fun applyPreRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float)
    fun applyPostRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float)

    fun handleAnimate(component: BaseComponent) {
    }
}
