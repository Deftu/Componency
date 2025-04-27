package dev.deftu.componency.gif

import dev.deftu.componency.ByteStream
import dev.deftu.componency.image.Image
import dev.deftu.componency.image.ImageType
import kotlin.jvm.JvmField

public class GifDecoder(private val byteStream: ByteStream) {

    public companion object {

        public const val HEADER_SIZE: Int = 6

        @JvmField
        public val HEADERS: Array<String> = arrayOf("GIF87a", "GIF89a")

    }

    private var width: Int = 0
    private var height: Int = 0
    private var loopCount: Int = 0

    private lateinit var globalColorTable: Array<Int>
    private lateinit var canvas: IntArray
    private var prevCanvas: IntArray? = null

    private var currentDelay: Int = 100
    private var currentDisposalMethod: Int = 0

    private var transparencyFlag: Boolean = false
    private var transparentColorIndex: Int = -1

    public fun decode(): GifAnimation {
        expectHeader()
        expectLogicalScreenDescriptor()

        canvas = IntArray(width * height) { 0x00000000 } // transparent canvas

        val frames = mutableListOf<GifFrame>()

        while (true) {
            when (val blockType = byteStream.readByte().toInt() and 0xFF) {
                0x21 -> parseExtension()
                0x2C -> frames += parseFrame()
                0x3B -> break // Trailer: end of file
                else -> throw IllegalStateException("Unknown GIF block type: $blockType")
            }
        }

        return GifAnimation(frames, loopCount)
    }

    private fun expectHeader() {
        val header = byteStream.readBytes(HEADER_SIZE).decodeToString()
        if (header !in HEADERS) {
            throw IllegalArgumentException("Not a valid GIF file: found '$header'")
        }
    }

    private fun expectLogicalScreenDescriptor() {
        width = byteStream.readUShortLE()
        height = byteStream.readUShortLE()

        val packed = byteStream.readByte().toInt() and 0xFF
        val hasGlobalColorTable = (packed and 0b1000_0000) != 0
        val globalColorTableSize = 2 shl (packed and 0b0000_0111)

        byteStream.skip(2) // background color index + pixel aspect ratio

        globalColorTable = if (hasGlobalColorTable) {
            readColorTable(globalColorTableSize)
        } else {
            emptyArray()
        }
    }

    private fun parseExtension() {
        when (byteStream.readByte().toInt() and 0xFF) {
            0xF9 -> { // Graphic Control Extension
                byteStream.skip(1) // block size (should be 4)
                val packedFields = byteStream.readByte()
                val delay = byteStream.readUShortLE()
                transparentColorIndex = byteStream.readByte().toInt() and 0xFF
                byteStream.skip(1) // block terminator

                transparencyFlag = (packedFields.toInt() and 0b00000001) != 0
                currentDisposalMethod = (packedFields.toInt() shr 2) and 0b00000111
                currentDelay = if (delay == 0) 10 else delay * 10
            }

            0xFF -> { // Application Extension
                val blockSize = byteStream.readByte().toInt() and 0xFF
                val applicationIdentifier = byteStream.readBytes(8).decodeToString()
                val applicationAuthCode = byteStream.readBytes(3).decodeToString()

                if (applicationIdentifier != "NETSCAPE" || applicationAuthCode != "2.0") {
                    skipSubBlocks()
                    return
                }

                parseNetscapeExtension()
            }

            else -> skipSubBlocks()
        }
    }

    private fun parseNetscapeExtension() {
        byteStream.skip(1) // Sub-block size
        val subBlockId = byteStream.readByte().toInt() and 0xFF
        if (subBlockId == 1) {
            loopCount = byteStream.readUShortLE()
        }

        byteStream.skip(1) // block terminator
    }


    private fun parseFrame(): GifFrame {
        val imageLeft = byteStream.readUShortLE()
        val imageTop = byteStream.readUShortLE()
        val imageWidth = byteStream.readUShortLE()
        val imageHeight = byteStream.readUShortLE()

        val localPacked = byteStream.readByte().toInt() and 0xFF
        val hasLocalColorTable = (localPacked and 0b1000_0000) != 0
        val localColorTableSize = 2 shl (localPacked and 0b0000_0111)

        val colorTable = if (hasLocalColorTable) {
            readColorTable(localColorTableSize)
        } else {
            globalColorTable
        }

        val lzwMinCodeSize = byteStream.readByte().toInt() and 0xFF
        val imageData = readSubBlocks()

        val pixelIndices = LzwDecoder.decode(imageWidth, imageHeight, lzwMinCodeSize, imageData)
        prevCanvas = canvas.copyOf() // Save previous canvas for disposal method 3

        // Composite onto canvas
        for (py in 0 until imageHeight) {
            for (px in 0 until imageWidth) {
                val patchIndex = py * imageWidth + px
                val canvasX = imageLeft + px
                val canvasY = imageTop + py

                if (canvasX !in 0 until width || canvasY !in 0 until height) {
                    continue // Skip pixels outside the canvas
                }

                val canvasIndex = canvasY * width + canvasX
                val paletteIndex = pixelIndices[patchIndex]

                if (transparencyFlag && paletteIndex == transparentColorIndex) {
                    continue // Skip transparent pixels
                }

                if (paletteIndex in colorTable.indices) {
                    canvas[canvasIndex] = colorTable[paletteIndex]
                }
            }
        }

        val rgbaBytes = decompressToRgba(canvas)

        val frame = GifFrame(
            image = Image(
                type = ImageType.RASTER,
                data = rgbaBytes
            ).apply {
                this@apply.width = this@GifDecoder.width.toFloat()
                this@apply.height = this@GifDecoder.height.toFloat()
            },
            delay = currentDelay
        )

        applyDisposal(imageLeft, imageTop, imageWidth, imageHeight)

        // Reset transparency flag for next frame
        transparencyFlag = false
        transparentColorIndex = -1

        return frame
    }

    private fun applyDisposal(x: Int, y: Int, w: Int, h: Int) {
        when (currentDisposalMethod) {
            // 0 and 1: No disposal (leave as-is)

            2 -> { // Restore to background
                for (py in 0 until h) {
                    for (px in 0 until w) {
                        val cx = x + px
                        val cy = y + py
                        if (cx in 0 until width && cy in 0 until height) {
                            val canvasIndex = cy * width + cx
                            canvas[canvasIndex] = 0x00000000 // fully transparent
                        }
                    }
                }
            }

            3 -> { // Restore to previous frame
                prevCanvas?.copyInto(canvas)
            }
        }
    }

    private fun readColorTable(size: Int): Array<Int> {
        return Array(size) {
            val r = byteStream.readByte().toInt() and 0xFF
            val g = byteStream.readByte().toInt() and 0xFF
            val b = byteStream.readByte().toInt() and 0xFF
            (0xFF shl 24) or (r shl 16) or (g shl 8) or b
        }
    }

    private fun skipSubBlocks() {
        while (true) {
            val blockSize = byteStream.readByte().toInt() and 0xFF
            if (blockSize == 0) break
            byteStream.skip(blockSize)
        }
    }

    private fun readSubBlocks(): ByteArray {
        val output = mutableListOf<Byte>()
        while (true) {
            val blockSize = byteStream.readByte().toInt() and 0xFF
            if (blockSize == 0) break
            val chunk = byteStream.readBytes(blockSize)
            output.addAll(chunk.toList())
        }

        return output.toByteArray()
    }

    private fun decompressToRgba(pixels: IntArray): ByteArray {
        val output = ByteArray(pixels.size * 4)
        for (i in pixels.indices) {
            val pixel = pixels[i]
            val offset = i * 4
            output[offset + 0] = ((pixel shr 16) and 0xFF).toByte() // R
            output[offset + 1] = ((pixel shr 8) and 0xFF).toByte()  // G
            output[offset + 2] = (pixel and 0xFF).toByte()          // B
            output[offset + 3] = ((pixel shr 24) and 0xFF).toByte() // A
        }

        return output
    }
}
