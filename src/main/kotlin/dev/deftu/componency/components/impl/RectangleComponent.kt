package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.engine.Engine

public class RectangleComponent : Component() {

    override fun render() {
        val engine = Engine.get(this)

        val fill = config.properties.fill.getColor(this)
        val topLeftRadius = config.properties.topLeftRadius.getRadius(this)
        val topRightRadius = config.properties.topRightRadius.getRadius(this)
        val bottomRightRadius = config.properties.bottomRightRadius.getRadius(this)
        val bottomLeftRadius = config.properties.bottomLeftRadius.getRadius(this)

        engine.renderEngine.fill(
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
    }

}
