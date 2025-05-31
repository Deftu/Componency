package dev.deftu.componency.android.platform.image

import android.graphics.Bitmap
import dev.deftu.componency.platform.image.PlatformImage

public data class AndroidPlatformImage(
    public val value: Bitmap
) : PlatformImage
