package dev.deftu.componency.platform.audio

import java.io.DataInputStream
import java.io.File
import java.io.InputStream
import java.nio.file.Path

public class WavAudioSource(
    stream: InputStream,
    sampleRate: Int,
    channelCount: Int,
) : StreamingAudioSource(
    identifyWavStream(stream),
    sampleRate,
    channelCount
) {

    private companion object {

        private fun identifyWavStream(stream: InputStream): InputStream {
            val header = DataInputStream(stream.buffered())

            // Read the RIFF header
            val riff = ByteArray(4).apply(header::readFully)
            if (String(riff) != "RIFF") {
                throw IllegalArgumentException("Not a valid WAV file")
            }

            header.skipBytes(4)

            // Read the WAVE header
            val wave = ByteArray(4).apply(header::readFully)
            if (String(wave) != "WAVE") {
                throw IllegalArgumentException("Not a valid WAV file")
            }

            // Scan until we find the data chunk
            while (true) {
                val chunkId = ByteArray(4).apply(header::readFully)
                val chunkSize = Integer.reverseBytes(header.readInt())

                if (String(chunkId) == "data") {
                    return header
                } else {
                    header.skipBytes(chunkSize)
                }
            }
        }

    }

    public constructor(
        file: File,
        sampleRate: Int,
        channelCount: Int,
    ) : this(file.inputStream(), sampleRate, channelCount)

    public constructor(
        path: Path,
        sampleRate: Int,
        channelCount: Int,
    ) : this(path.toFile(), sampleRate, channelCount)

}
