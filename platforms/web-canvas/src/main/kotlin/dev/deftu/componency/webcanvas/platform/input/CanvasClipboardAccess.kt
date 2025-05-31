package dev.deftu.componency.webcanvas.platform.input

import dev.deftu.componency.image.Image
import dev.deftu.componency.platform.input.ClipboardAccess
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.files.Blob

public class CanvasClipboardAccess : ClipboardAccess {

    override var clipboardString: String?
        get() = throw UnsupportedOperationException("Use getClipboardStringAsynchronously() instead")
        set(value) {
            if (value == null) {
                return
            }

            window.navigator.clipboard.writeText(value)
                .catch { console.error("Failed to write clipboard text: $it") }
        }

    override var clipboardImage: Image?
        get() = throw UnsupportedOperationException("Use getClipboardImageAsynchronously() instead")
        set(value) {
            if (value == null) {
                return
            }
        }

    override fun clearClipboard() {
        window.navigator.clipboard.writeText("")
            .catch { console.error("Failed to clear clipboard: $it") }
    }

    override suspend fun getClipboardStringAsynchronously(): String? {
        return try {
            window.navigator.clipboard.readText().await()
        } catch (e: Throwable) {
            console.error("Failed to read clipboard text: $e")
            null
        }
    }

    override suspend fun getClipboardImageAsynchronously(): Image? {
        return null
    }

    override suspend fun getClipboardAsynchronously(): ClipboardAccess.ClipboardContent? {
        return null
    }

    private fun convertImageToBlob(image: Image): Blob {
        // TODO: Implement this conversion based on your Image type
        error("convertImageToBlob not implemented")
    }

    private fun convertBlobToImage(blob: Blob): Image {
        // TODO: Implement this conversion based on your Image type
        error("convertBlobToImage not implemented")
    }

}
