package dev.deftu.componency.gif

import dev.deftu.componency.image.Image
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import java.nio.file.Path
import javax.imageio.ImageIO
import javax.imageio.metadata.IIOMetadata
import javax.imageio.metadata.IIOMetadataNode
import kotlin.math.roundToInt

public data class GifAnimation(private val frames: List<GifFrame>) {

    public companion object {

        @JvmField
        public val EMPTY: GifAnimation = GifAnimation(emptyList())

        @JvmStatic
        public fun from(inputStream: InputStream): GifAnimation {
            val reader = ImageIO.getImageReadersByFormatName("gif").next()
            reader.input = ImageIO.createImageInputStream(inputStream)

            val frameCount = reader.getNumImages(true)
            val width = reader.getWidth(0)
            val height = reader.getHeight(0)

            val canvas = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            val g = canvas.createGraphics()
            g.background = Color(0, 0, 0, 0)

            val frames = mutableListOf<GifFrame>()

            for (i in 0 until frameCount) {
                val frameImage = reader.read(i)
                val metadata = reader.getImageMetadata(i)

                val delay = extractDelayFromMetadata(metadata) ?: 100
                val (x, y) = extractImagePosition(metadata) ?: (0 to 0)

                g.drawImage(frameImage, x, y, null)

                val copy = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
                val cg = copy.createGraphics()
                cg.drawImage(canvas, 0, 0, null)
                cg.dispose()

                val frame = Image.from(copy).apply {
                    this.width = width.toFloat()
                    this.height = height.toFloat()
                }

                frames += GifFrame(frame, delay)
            }

            g.dispose()
            reader.dispose()
            return GifAnimation(frames)
        }

        @JvmStatic
        public fun from(file: File): GifAnimation {
            return from(file.inputStream())
        }

        @JvmStatic
        public fun from(path: Path): GifAnimation {
            return from(path.toFile().inputStream())
        }

        @JvmStatic
        public fun from(data: ByteArray): GifAnimation {
            return from(data.inputStream())
        }

        private fun extractImagePosition(metadata: IIOMetadata): Pair<Int, Int>? {
            val root = metadata.getAsTree("javax_imageio_gif_image_1.0") as? IIOMetadataNode ?: return null
            val imgDesc = root.getElementsByTagName("ImageDescriptor").item(0) as? IIOMetadataNode ?: return null

            val x = imgDesc.getAttribute("imageLeftPosition").toIntOrNull() ?: 0
            val y = imgDesc.getAttribute("imageTopPosition").toIntOrNull() ?: 0
            return x to y
        }

        private fun extractDelayFromMetadata(metadata: IIOMetadata): Int? {
            val root = metadata.getAsTree("javax_imageio_gif_image_1.0") as? IIOMetadataNode ?: return null
            val gce = root.getElementsByTagName("GraphicControlExtension").item(0) as? IIOMetadataNode
            return gce?.getAttribute("delayTime")?.toIntOrNull()?.let { it * 10 }
        }

    }

    private var currentFrameIndex: Int = 0
    private var accumulatedTime: Int = 0

    public val currentFrame: GifFrame
        get() = frames[currentFrameIndex]

    public fun step(deltaTime: Float) {
        accumulatedTime += (deltaTime * 1_000f).roundToInt()
        while (accumulatedTime >= frames[currentFrameIndex].delay) {
            accumulatedTime -= frames[currentFrameIndex].delay
            currentFrameIndex = (currentFrameIndex + 1) % frames.size
        }
    }

    public fun reset() {
        currentFrameIndex = 0
        accumulatedTime = 0
    }

}
