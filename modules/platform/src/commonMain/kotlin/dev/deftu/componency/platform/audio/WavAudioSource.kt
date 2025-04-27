package dev.deftu.componency.platform.audio

import dev.deftu.componency.ByteStream
import kotlin.jvm.JvmStatic

public class WavAudioSource(
    stream: ByteStream,
    sampleRate: Int,
    channelCount: Int,
) : StreamingAudioSource(identifyWavStream(stream), sampleRate, channelCount) {

    public companion object {

        @JvmStatic
        public fun identifyWavStream(stream: ByteStream): ByteStream {
            val riff = ByteArray(4).also(stream::readFully)
            if (!riff.contentEquals("RIFF".encodeToByteArray())) {
                throw IllegalArgumentException("Not a valid WAV file")
            }

            stream.skip(4)

            val wave = ByteArray(4).also(stream::readFully)
            if (!wave.contentEquals("WAVE".encodeToByteArray())) {
                throw IllegalArgumentException("Not a valid WAV file")
            }

            while (true) {
                val chunkId = ByteArray(4).also(stream::readFully)
                val chunkSize = ByteArray(4).also(stream::readFully).toIntLE()

                if (chunkId.decodeToString() == "data") {
                    return stream
                } else {
                    stream.skip(chunkSize)
                }
            }
        }

        private fun ByteArray.toIntLE(): Int {
            return (this[0].toInt() and 0xFF) or
                    ((this[1].toInt() and 0xFF) shl 8) or
                    ((this[2].toInt() and 0xFF) shl 16) or
                    ((this[3].toInt() and 0xFF) shl 24)
        }

    }

}
