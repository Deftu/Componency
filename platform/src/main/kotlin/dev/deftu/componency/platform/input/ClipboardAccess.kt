package dev.deftu.componency.platform.input

import dev.deftu.componency.image.Image

public interface ClipboardAccess {

    public var clipboard: ClipboardContent?

    public var clipboardString: String?

    public var clipboardImage: Image?

    public sealed interface ClipboardContent {
        public data class StringContent(val content: String) : ClipboardContent
        public data class ImageContent(val content: Image) : ClipboardContent
    }

}
