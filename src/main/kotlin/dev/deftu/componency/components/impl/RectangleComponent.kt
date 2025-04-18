@file:Suppress("FunctionName")

package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.dsl.px
import org.intellij.lang.annotations.Pattern

public open class RectangleComponentProperties(component: RectangleComponent) : ComponentProperties<RectangleComponent, RectangleComponentProperties>(component)

public open class RectangleComponent : Component<RectangleComponent, RectangleComponentProperties>(::RectangleComponentProperties) {

    override fun render() {
        val platform = findPlatform(this)

        val fill = properties.fill.getColor(this)
        val topLeftRadius = properties.topLeftRadius.getRadius(this)
        val topRightRadius = properties.topRightRadius.getRadius(this)
        val bottomRightRadius = properties.bottomRightRadius.getRadius(this)
        val bottomLeftRadius = properties.bottomLeftRadius.getRadius(this)
        platform.renderer.fill(
            x1 = left,
            y1 = top,
            x2 = right,
            y2 = bottom,
            color = fill,
            topLeftRadius = topLeftRadius,
            topRightRadius = topRightRadius,
            bottomRightRadius = bottomRightRadius,
            bottomLeftRadius = bottomLeftRadius
        )

        val stroke = properties.stroke.getStroke(this)
        if (!stroke.isNone) {
            val strokeColor = stroke.color
            val strokeWidth = stroke.width.px.getWidth(this)
            val strokeSides = stroke.sides
            platform.renderer.stroke(
                x1 = left,
                y1 = top,
                x2 = right,
                y2 = bottom,
                color = strokeColor,
                strokeWidth = strokeWidth,
                strokeSides = strokeSides,
                topLeftRadius = topLeftRadius,
                topRightRadius = topRightRadius,
                bottomRightRadius = bottomRightRadius,
                bottomLeftRadius = bottomLeftRadius
            )
        }
    }

}

public fun Rectangle(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: RectangleComponentProperties.() -> Unit = {}
): RectangleComponent {
    val component = RectangleComponent()
    component.properties.name = name
    component.properties.block()
    return component
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.Rectangle(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: RectangleComponentProperties.() -> Unit = {}
): T {
    val component = RectangleComponent()
    component.properties.name = name
    component.properties.block()
    component.attachTo(this@Rectangle.component)
    return component as T
}
