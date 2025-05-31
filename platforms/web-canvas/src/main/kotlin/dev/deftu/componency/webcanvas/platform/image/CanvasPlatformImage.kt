package dev.deftu.componency.webcanvas.platform.image

import dev.deftu.componency.platform.image.PlatformImage
import org.w3c.dom.HTMLImageElement

public class CanvasPlatformImage(
    public val element: HTMLImageElement
) : PlatformImage
