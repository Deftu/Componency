package dev.deftu.componency.components

import dev.deftu.componency.color.Color
import dev.deftu.componency.dsl.asProperty
import dev.deftu.componency.dsl.degrees
import dev.deftu.componency.dsl.px
import dev.deftu.componency.font.Font
import dev.deftu.componency.properties.*
import dev.deftu.componency.styling.Stroke
import dev.deftu.componency.utils.Animateable

public open class ComponentProperties(public val component: Component) : Animateable {

    public var x: XProperty = 0.px

    public var y: YProperty = 0.px

    public var width: WidthProperty = 0.px

    public var height: HeightProperty = 0.px

    public var fill: ColorProperty = Color.WHITE.asProperty

    public var stroke: StrokeProperty = Stroke.NONE.asProperty

    public var topLeftRadius: RadialProperty = 0.px

    public var topRightRadius: RadialProperty = 0.px

    public var bottomLeftRadius: RadialProperty = 0.px

    public var bottomRightRadius: RadialProperty = 0.px

    public var angle: AngleProperty = 0.degrees

    public var font: Font? = null

    public var fontSize: HeightProperty = 0.px

    override fun frame() {
        x.frame()
        y.frame()
        width.frame()
        height.frame()
        fill.frame()
        topLeftRadius.frame()
        topRightRadius.frame()
        bottomLeftRadius.frame()
        bottomRightRadius.frame()
        angle.frame()
    }

    public fun copyFrom(properties: ComponentProperties): ComponentProperties = apply {
        x = properties.x
        y = properties.y
        width = properties.width
        height = properties.height
        fill = properties.fill
        topLeftRadius = properties.topLeftRadius
        topRightRadius = properties.topRightRadius
        bottomLeftRadius = properties.bottomLeftRadius
        bottomRightRadius = properties.bottomRightRadius
        angle = properties.angle
        font = properties.font
        fontSize = properties.fontSize
    }

}
