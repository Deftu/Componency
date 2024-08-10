package dev.deftu.componency.utils

import dev.deftu.componency.dsl.withAlpha
import dev.deftu.componency.dsl.withAlphaPercentage
import java.awt.Color

public object Colors {

    @JvmStatic
    public fun withAlpha(color: Color, alpha: Int): Color {
        return color.withAlpha(alpha)
    }

    @JvmStatic
    public fun withAlphaPercentage(color: Color, percent: Float): Color {
        return color.withAlphaPercentage(percent)
    }

}
