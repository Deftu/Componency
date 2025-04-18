@file:Suppress("FunctionName")

package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.dsl.px
import org.intellij.lang.annotations.Pattern

public class CircleComponentProperties(component: CircleComponent) : ComponentProperties<CircleComponent, CircleComponentProperties>(component)

public class CircleComponent : Component<CircleComponent, CircleComponentProperties>(::CircleComponentProperties) {

    override fun render() {
        val platform = findPlatform(this)

        val fill = properties.fill.getColor(this)
        platform.renderer.fill(
            x1 = left,
            y1 = top,
            x2 = right,
            y2 = bottom,
            color = fill,
            topLeftRadius = width / 2,
            topRightRadius = width / 2,
            bottomRightRadius = width / 2,
            bottomLeftRadius = width / 2
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
                topLeftRadius = width / 2,
                topRightRadius = width / 2,
                bottomRightRadius = width / 2,
                bottomLeftRadius = width / 2
            )
        }
    }

}

public fun Circle(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: CircleComponentProperties.() -> Unit = {}
): CircleComponent {
    val component = CircleComponent()
    component.properties.name = name
    component.properties.block()
    return component
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.Circle(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: CircleComponentProperties.() -> Unit = {}
): T {
    val component = CircleComponent()
    component.properties.name = name
    component.properties.block()
    component.attachTo(this@Circle.component)
    return component as T
}
