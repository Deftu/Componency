package dev.deftu.componency.constraints

import dev.deftu.componency.components.BaseComponent
import dev.deftu.componency.dsl.*
import dev.deftu.componency.font.FontProvider
import dev.deftu.componency.font.impl.VanillaFontProvider
import java.awt.Color

open class ComponentConstraints(
    protected val component: BaseComponent
) {
    var x: XConstraint = 0.px
        set(value) {
            // TODO - Notify event listeners
            field = value
        }
    var y: YConstraint = 0.px
        set(value) {
            // TODO - Notify event listeners
            field = value
        }
    var width: WidthConstraint = 0.px
        set(value) {
            // TODO - Notify event listeners
            field = value
        }
    var height: HeightConstraint = 0.px
        set(value) {
            // TODO - Notify event listeners
            field = value
        }
    var fontProvider: FontProvider = VanillaFontProvider
        set(value) {
            // TODO - Notify event listeners
            field = value
        }
    var radius: RadiusConstraint = 0.px
        set(value) {
            // TODO - Notify event listeners
            field = value
        }
    var color: ColorConstraint = Color.WHITE.toConstraint()
        set(value) {
            // TODO - Notify event listeners
            field = value
        }

    open fun getX() = x.getValueForX(component)
    open fun getY() = y.getValueForY(component)
    open fun getWidth() = width.getValueForWidth(component)
    open fun getHeight() = height.getValueForHeight(component)
    open fun getRadius() = radius.getValueForRadius(component)
    open fun getColor() = color.getValueForColor(component)

    fun onWindowResize() {
        x.recalculate = true
        y.recalculate = true
        width.recalculate = true
        height.recalculate = true
        radius.recalculate = true
    }

    open fun handleDestroy() {
        x.handleDestroy()
        y.handleDestroy()
        width.handleDestroy()
        height.handleDestroy()
        fontProvider.handleDestroy()
        radius.handleDestroy()
        color.handleDestroy()
    }

    open fun handleAnimate() {
        x.handleAnimate()
        y.handleAnimate()
        width.handleAnimate()
        height.handleAnimate()
        fontProvider.setAnimating(false)
        radius.handleAnimate()
        color.handleAnimate()
    }

    fun copyFrom(other: ComponentConstraints) {
        x = other.x
        y = other.y
        width = other.width
        height = other.height
        fontProvider = other.fontProvider
        radius = other.radius
        color = other.color
    }
}
