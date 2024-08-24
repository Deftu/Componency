package dev.deftu.componency.minecraft.api

import java.nio.ByteBuffer

public interface StbApi {

    public fun loadFromMemory(buffer: ByteBuffer, widthOutput: IntArray, heightOutput: IntArray, channelsOutput: IntArray, desiredChannels: Int): ByteBuffer

}
