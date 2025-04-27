@file:Suppress("FunctionName")

package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.image.Image

public open class ImageComponentProperties(component: ImageComponent) : ComponentProperties<ImageComponent, ImageComponentProperties>(component) {

    public var image: Image = Image.EMPTY

    override fun copyFrom(properties: ComponentProperties<*, *>): ComponentProperties<ImageComponent, ImageComponentProperties> {
        if (properties is ImageComponentProperties) {
            image = properties.image
        }

        return super.copyFrom(properties)
    }

}

public open class ImageComponent : Component<ImageComponent, ImageComponentProperties>(::ImageComponentProperties) {

    override fun render() {
        val platform = findPlatform(this)

        val image = properties.image
        val fill = properties.fill.getColor(this)
        val topLeftRadius = properties.topLeftRadius.getRadius(this)
        val topRightRadius = properties.topRightRadius.getRadius(this)
        val bottomRightRadius = properties.bottomRightRadius.getRadius(this)
        val bottomLeftRadius = properties.bottomLeftRadius.getRadius(this)

        platform.renderer.imageRenderer.image(
            image = image,
            x1 = left,
            y1 = top,
            x2 = right,
            y2 = bottom,
            color = fill,
            topLeftRadius = topLeftRadius,
            topRightRadius = topRightRadius,
            bottomRightRadius = bottomRightRadius,
            bottomLeftRadius = bottomLeftRadius
        )
    }

}

public fun Image(
    name: String? = null,
    block: ImageComponentProperties.() -> Unit = {}
): ImageComponent {
    val component = ImageComponent()
    component.properties.name = name
    component.properties.block()
    return component
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.Image(
    name: String? = null,
    block: ImageComponentProperties.() -> Unit = {}
): T {
    val component = ImageComponent()
    component.properties.name = name
    component.properties.block()
    component.attachTo(this@Image.component)
    return component as T
}
