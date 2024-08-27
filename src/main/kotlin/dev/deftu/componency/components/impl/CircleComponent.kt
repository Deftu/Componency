package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.engine.Engine

/**
 * The same as a [RectangleComponent], but with a radius of 50%.
 */
public class CircleComponent : Component() {

    override fun render() {
        val engine = Engine.get(this)

        val fill = config.properties.fill.getColor(this)
        engine.renderEngine.fill(
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

        val stroke = config.properties.stroke.getStroke(this)
        if (!stroke.isNone) {
            val strokeColor = stroke.color
            val strokeWidth = stroke.width.getWidth(this)
            val strokeSides = stroke.sides
            engine.renderEngine.stroke(
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
