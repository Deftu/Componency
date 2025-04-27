package dev.deftu.componency.lwjgl3.engine.rendering

import dev.deftu.componency.color.Color
import dev.deftu.componency.image.Image
import dev.deftu.componency.image.ImageType
import dev.deftu.componency.platform.rendering.ImageRenderer
import org.lwjgl.nanovg.NanoVG
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Lwjgl3ImageRenderer(
    private val renderer: Lwjgl3Renderer
) : ImageRenderer {

    private val images = mutableMapOf<Image, Int>()

    var nvg = MemoryUtil.NULL
    var nsvg = MemoryUtil.NULL

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
        val width = x2 - x1
        val height = y2 - y1

        NanoVG.nvgImagePattern(nvg, x1, y1, width, height, 0f, getOrPopulateImage(image, width, height), 1f, renderer.paint)
        if (!color.isTransparent) {
            renderer.populateNvgColor(color, renderer.paint.innerColor())
        }

        NanoVG.nvgBeginPath(nvg)
        NanoVG.nvgRoundedRectVarying(
            nvg,
            x1,
            y1,
            width,
            height,
            topLeftRadius,
            topRightRadius,
            bottomRightRadius,
            bottomLeftRadius
        )

        NanoVG.nvgFillPaint(nvg, renderer.paint)
        NanoVG.nvgFill(nvg)
    }

    private fun getOrPopulateImage(image: Image, width: Float, height: Float): Int {
        return images.getOrPut(image) {
            val bytes = image.data

            when (image.type) {
                ImageType.RASTER -> {
                    val buffer = run {
                        val buffer = ByteBuffer.allocateDirect(bytes.size)
                            .order(ByteOrder.nativeOrder())
                            .put(bytes)
                            .flip() as ByteBuffer
                        val widthOutput = IntArray(1)
                        val heightOutput = IntArray(1)
                        val result = STBImage.stbi_load_from_memory(buffer, widthOutput, heightOutput, IntArray(1), 4)
                            ?: throw IllegalStateException("Failed to load image from memory")
                        image.width = widthOutput[0].toFloat()
                        image.height = heightOutput[0].toFloat()

                        result
                    }

                    NanoVG.nvgCreateImageRGBA(
                        nvg,
                        image.width.toInt(),
                        image.height.toInt(),
                        NanoVG.NVG_IMAGE_GENERATE_MIPMAPS,
                        buffer
                    )
                }

                ImageType.VECTOR -> {
                    throw UnsupportedOperationException("SVG images are not supported yet")
                }

                else -> throw IllegalArgumentException("Unsupported image type: ${image.type}")
            }
        }
    }

}
