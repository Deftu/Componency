package dev.deftu.componency.dsl

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.SimpleProperty
import dev.deftu.componency.properties.*
import dev.deftu.componency.styling.Stroke

public fun simpleXProperty(block: (component: Component) -> Float): XProperty {
    return object : SimpleProperty<Float>(0f), XProperty {

        override fun calculateX(component: Component): Float {
            return block(component)
        }

    }
}

public fun simpleYProperty(block: (component: Component) -> Float): YProperty {
    return object : SimpleProperty<Float>(0f), YProperty {

        override fun calculateY(component: Component): Float {
            return block(component)
        }

    }
}

public fun simpleWidthProperty(block: (component: Component) -> Float): WidthProperty {
    return object : SimpleProperty<Float>(0f), WidthProperty {

        override fun calculateWidth(component: Component): Float {
            return block(component)
        }

    }
}

public fun simpleHeightProperty(block: (component: Component) -> Float): HeightProperty {
    return object : SimpleProperty<Float>(0f), HeightProperty {

        override fun calculateHeight(component: Component): Float {
            return block(component)
        }

    }
}

public fun simpleColorProperty(block: (component: Component) -> Color): ColorProperty {
    return object : SimpleProperty<Color>(Color.WHITE), ColorProperty {

        override fun calculateColor(component: Component): Color {
            return block(component)
        }

    }
}

public fun simpleStrokeProperty(block: (component: Component) -> Stroke): StrokeProperty {
    return object : SimpleProperty<Stroke>(Stroke.NONE), StrokeProperty {

        override fun calculateStroke(component: Component): Stroke {
            return block(component)
        }

    }
}

public fun simpleRadialProperty(block: (component: Component) -> Float): RadialProperty {
    return object : SimpleProperty<Float>(0f), RadialProperty {

        override fun calculateRadius(component: Component): Float {
            return block(component)
        }

    }
}

public fun simpleAngleProperty(block: (component: Component) -> Float): AngleProperty {
    return object : SimpleProperty<Float>(0f), AngleProperty {

        override fun calculateAngle(component: Component): Float {
            return block(component)
        }

    }
}
