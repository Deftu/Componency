package dev.deftu.componency.minecraft.impl

import dev.deftu.componency.minecraft.api.StbApi
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer

public class StbImpl : StbApi {

    override fun loadFromMemory(
        buffer: ByteBuffer,
        widthOutput: IntArray,
        heightOutput: IntArray,
        channelsOutput: IntArray,
        desiredChannels: Int
    ): ByteBuffer {
        return STBImage.stbi_load_from_memory(buffer, widthOutput, heightOutput, channelsOutput, desiredChannels) ?: throw IllegalStateException("Failed to load image from memory")
    }

}
