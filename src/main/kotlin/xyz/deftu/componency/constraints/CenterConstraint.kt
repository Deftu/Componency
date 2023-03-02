package xyz.deftu.componency.constraints

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.componency.utils.roundToScaledPixels

class CenterConstraint : PositionalConstraint {
    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    override fun getImplValueForX(component: BaseComponent): Float {
        return if (component.isAlreadyCentered()) {
            (attachedTo ?: component.parent).getX() + ((attachedTo ?: component.parent).getWidth() / 2).roundToScaledPixels()
        } else {
            (attachedTo ?: component.parent).getX() + ((attachedTo ?: component.parent).getWidth() / 2 - component.getWidth() / 2).roundToScaledPixels()
        }
    }

    override fun getImplValueForY(component: BaseComponent): Float {
        return if (component.isAlreadyCentered()) {
            (attachedTo ?: component.parent).getY() + ((attachedTo ?: component.parent).getHeight() / 2).roundToScaledPixels()
        } else {
            (attachedTo ?: component.parent).getY() + ((attachedTo ?: component.parent).getHeight() / 2 - component.getHeight() / 2).roundToScaledPixels()
        }
    }
}
