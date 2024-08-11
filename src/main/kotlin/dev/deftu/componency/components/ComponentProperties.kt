package dev.deftu.componency.components

import dev.deftu.componency.dsl.asProperty
import dev.deftu.componency.dsl.degrees
import dev.deftu.componency.dsl.px
import dev.deftu.componency.font.Font
import dev.deftu.componency.properties.*
import dev.deftu.componency.utils.Animateable
import java.awt.Color

public open class ComponentProperties(public val component: Component) : Animateable {

    public var x: XProperty = 0.px

    public var y: YProperty = 0.px

    public var width: WidthProperty = 0.px

    public var height: HeightProperty = 0.px

    public var fill: ColorProperty = Color.WHITE.asProperty

    public var radius: RadialProperty = 0.px

    public var angle: AngleProperty = 0.degrees

    public var font: Font? = null

    override fun frame() {
        x.frame()
        y.frame()
        width.frame()
        height.frame()
        fill.frame()
        radius.frame()
        angle.frame()
    }

    public fun copyFrom(properties: ComponentProperties) {
        x = properties.x
        y = properties.y
        width = properties.width
        height = properties.height
        fill = properties.fill
        radius = properties.radius
        angle = properties.angle
        font = properties.font
    }

}
