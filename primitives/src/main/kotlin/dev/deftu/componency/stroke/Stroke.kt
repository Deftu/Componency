package dev.deftu.componency.stroke

import dev.deftu.componency.color.Color
import dev.deftu.componency.dsl.px
import dev.deftu.componency.properties.WidthProperty

public data class Stroke @JvmOverloads constructor(
    public val color: Color,
    public val width: WidthProperty,
    public val sides: Set<StrokeSide> = StrokeSide.ALL
) {

    public companion object {

        @JvmField
        public val NONE: Stroke = Stroke(Color.TRANSPARENT, 0.px)

        @JvmStatic
        public fun isNone(stroke: Stroke): Boolean {
            return stroke == NONE || (stroke.color == Color.TRANSPARENT && stroke.width == 0.px)
        }

    }

    public val isNone: Boolean
        get() = isNone(this)

    public fun withColor(color: Color): Stroke {
        return copy(color = color)
    }

    public fun withWidth(width: WidthProperty): Stroke {
        return copy(width = width)
    }

}
