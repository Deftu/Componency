package dev.deftu.componency.platform.rendering

import dev.deftu.componency.color.Color
import dev.deftu.componency.image.Image

public interface ImageRenderer {

    public fun image(
        image: Image,
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomRightRadius: Float,
        bottomLeftRadius: Float
    )

    public fun image(
        image: Image,
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color
    ) {
        image(image, x1, y1, x2, y2, color, 0f, 0f, 0f, 0f)
    }

}
