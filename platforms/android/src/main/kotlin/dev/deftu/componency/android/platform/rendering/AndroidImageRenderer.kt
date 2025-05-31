package dev.deftu.componency.android.platform.rendering

import android.graphics.*
import dev.deftu.componency.color.Color
import dev.deftu.componency.image.Image
import dev.deftu.componency.platform.rendering.ImageRenderer

public class AndroidImageRenderer(
    private val canvasProvider: () -> Canvas?
) : ImageRenderer {

    private val bitmapCache = mutableMapOf<Image, Bitmap>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun image(
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
    ) {
        val canvas = canvasProvider() ?: return
        val bitmap = getBitmap(image) ?: return

        val dstRect = RectF(x1, y1, x2, y2)

        if (topLeftRadius == 0f && topRightRadius == 0f && bottomRightRadius == 0f && bottomLeftRadius == 0f) {
            // No rounded corners
            configurePaint(color)
            canvas.drawBitmap(bitmap, null, dstRect, paint)
        } else {
            // Draw with rounded corners
            val save = canvas.saveLayer(dstRect, null)

            val path = Path().apply {
                addRoundRect(
                    dstRect,
                    floatArrayOf(
                        topLeftRadius, topLeftRadius,
                        topRightRadius, topRightRadius,
                        bottomRightRadius, bottomRightRadius,
                        bottomLeftRadius, bottomLeftRadius
                    ),
                    Path.Direction.CW
                )
            }

            configurePaint(color)
            canvas.clipPath(path)
            canvas.drawBitmap(bitmap, null, dstRect, paint)

            canvas.restoreToCount(save)
        }
    }

    private fun configurePaint(color: Color) {
        paint.reset()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.colorFilter = if (color.isWhite) {
            null
        } else {
            PorterDuffColorFilter(
                android.graphics.Color.argb(
                    (color.alpha * 255).toInt().coerceIn(0, 255),
                    (color.red * 255).toInt().coerceIn(0, 255),
                    (color.green * 255).toInt().coerceIn(0, 255),
                    (color.blue * 255).toInt().coerceIn(0, 255)
                ),
                PorterDuff.Mode.MULTIPLY
            )
        }
    }

    private fun getBitmap(image: Image): Bitmap? {
        return bitmapCache.getOrPut(image) {
            BitmapFactory.decodeByteArray(image.data, 0, image.data.size)
        }
    }

    private val Color.isWhite: Boolean
        get() = red >= 0.99f && green >= 0.99f && blue >= 0.99f && alpha >= 0.99f

}
