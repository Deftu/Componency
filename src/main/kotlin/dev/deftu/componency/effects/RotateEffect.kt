package dev.deftu.componency.effects

import dev.deftu.componency.components.BaseComponent
import dev.deftu.multi.MultiMatrixStack

/**
 * Rotates the rendered output of a given component.
 *
 * @param angle The angle to rotate the component by.
 * @see [BaseComponent]
 * @see [Effect]
 * @since 1.0.0
 * @author Deftu
 */
@Deprecated(
    message = "This effect is not recommended to be used as it does not modify how other components perceive the position of the component, hence meaning that other components' constraint calculations will not be able to interact with the component as if it were rotated. If you wish to rotate a more complex component, it is recommended to create a custom component that handles the rotation internally.",
    level = DeprecationLevel.WARNING
) class RotateEffect(
    private val angle: Float
) : Effect {
    override fun applyPreRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float) {
        stack.push()

        val x = (component.getX() + (component.getRight() - component.getX())) / 2
        val y = (component.getY() + (component.getBottom() - component.getY())) / 2

        stack.translate(x, y, 0f)
        stack.rotate(angle, 0f, 0f, 1f)
        stack.translate(-x, -y, 0f)
    }

    override fun applyPostRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float) {
        stack.pop()
    }
}
