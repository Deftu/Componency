package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.gif.GifAnimation
import org.intellij.lang.annotations.Pattern

public class GifComponentProperties(component: GifComponent) : ComponentProperties<GifComponent, GifComponentProperties>(component) {

    public var animation: GifAnimation = GifAnimation.EMPTY

    override fun copyFrom(properties: ComponentProperties<*, *>): ComponentProperties<GifComponent, GifComponentProperties> {
        if (properties is GifComponentProperties) {
            animation = properties.animation
        }

        return super.copyFrom(properties)
    }

}

public class GifComponent : Component<GifComponent, GifComponentProperties>(::GifComponentProperties) {

    override fun render() {
        val platform = findPlatform(this)

        val image = properties.animation.currentFrame.image
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

    override fun animationFrame(deltaTime: Float) {
        super.animationFrame(deltaTime)
        properties.animation.step(deltaTime)
    }

}

public fun Gif(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: GifComponentProperties.() -> Unit = {}
): GifComponent {
    val component = GifComponent()
    component.properties.name = name
    component.properties.block()
    return component
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.Gif(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: GifComponentProperties.() -> Unit = {}
): T {
    val component = GifComponent()
    component.properties.name = name
    component.properties.block()
    component.attachTo(this@Gif.component)
    return component as T
}
