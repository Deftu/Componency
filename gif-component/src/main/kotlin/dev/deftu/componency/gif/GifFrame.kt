package dev.deftu.componency.gif

import dev.deftu.componency.image.Image

public data class GifFrame(public val image: Image, public val delay: Int) {

    public companion object {
        public val EMPTY: GifFrame = GifFrame(Image.EMPTY, 0)
    }

}
