package xyz.deftu.componency.effects

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.multi.MultiMatrixStack

interface Effect {
    fun applyPreRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float)
    fun applyPostRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float)

    fun handleAnimate(component: BaseComponent) {
    }
}
