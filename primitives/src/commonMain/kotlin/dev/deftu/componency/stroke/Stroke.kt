package dev.deftu.componency.stroke

import dev.deftu.componency.color.Color
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

public data class Stroke @JvmOverloads constructor(
    public val color: Color,
    public val width: Float,
    public val sides: Set<StrokeSide> = StrokeSide.ALL
) {

    public companion object {

        @JvmField
        public val NONE: Stroke = Stroke(Color.TRANSPARENT, 0f)

        @JvmStatic
        public fun isNone(stroke: Stroke): Boolean {
            return stroke == NONE || (stroke.color == Color.TRANSPARENT && stroke.width == 0f)
        }

    }

    public val isNone: Boolean
        get() = isNone(this)

    public fun withColor(color: Color): Stroke {
        return copy(color = color)
    }

    public fun withWidth(width: Float): Stroke {
        return copy(width = width)
    }

}
