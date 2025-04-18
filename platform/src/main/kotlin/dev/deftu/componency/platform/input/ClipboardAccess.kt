package dev.deftu.componency.platform.input

import dev.deftu.componency.image.Image

public interface ClipboardAccess {

    public var clipboardString: String?

    public var clipboardImage: Image?

    public var clipboard: ClipboardContent?
        get() = when {
            clipboardString != null -> ClipboardContent.StringContent(clipboardString!!)
            clipboardImage != null -> ClipboardContent.ImageContent(clipboardImage!!)
            else -> null
        }
        set(value) {
            when (value) {
                is ClipboardContent.StringContent -> clipboardString = value.content
                is ClipboardContent.ImageContent -> clipboardImage = value.content
                else -> clearClipboard()
            }
        }

    public fun clearClipboard()

    public sealed interface ClipboardContent {
        public data class StringContent(val content: String) : ClipboardContent
        public data class ImageContent(val content: Image) : ClipboardContent
    }

}
