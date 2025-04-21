package dev.deftu.componency.platform.audio

public class WavAudioSource(
    stream: AudioByteStream,
    sampleRate: Int,
    channelCount: Int,
) : StreamingAudioSource(identifyWavStream(stream), sampleRate, channelCount) {

    public companion object {

        @JvmStatic
        public fun identifyWavStream(stream: AudioByteStream): AudioByteStream {
            val header = stream

            val riff = ByteArray(4).also(header::readFully)
            if (!riff.contentEquals("RIFF".toByteArray())) {
                throw IllegalArgumentException("Not a valid WAV file")
            }

            header.skip(4)

            val wave = ByteArray(4).also(header::readFully)
            if (!wave.contentEquals("WAVE".toByteArray())) {
                throw IllegalArgumentException("Not a valid WAV file")
            }

            while (true) {
                val chunkId = ByteArray(4).also(header::readFully)
                val chunkSize = ByteArray(4).also(header::readFully).toIntLE()

                if (String(chunkId) == "data") {
                    return header
                } else {
                    header.skip(chunkSize)
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
