package dev.deftu.componency.platform.rendering

import dev.deftu.componency.color.Color
import dev.deftu.componency.font.Font

public interface TextRenderer {

    public data class TextSize(
        val width: Float,
        val height: Float
    )

    public fun text(
        font: Font,
        text: String,
        x: Float,
        y: Float,
        color: Color,
        fontSize: Float
    )

    public fun textSize(
        font: Font,
        text: String,
        fontSize: Float
    ): TextSize

}
