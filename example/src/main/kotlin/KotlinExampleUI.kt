import dev.deftu.componency.animations.Easings
import dev.deftu.componency.components.Component
import dev.deftu.componency.components.impl.FrameComponent
import dev.deftu.componency.components.impl.RectangleComponent
import dev.deftu.componency.dsl.*
import dev.deftu.componency.engine.Engine
import dev.deftu.componency.properties.VectorProperty
import dev.deftu.componency.properties.impl.LinkedProperty
import java.awt.Color

class KotlinExampleUI(engine: Engine) {

    private val frame = FrameComponent().configure {
        name = "window" // A name is optional. Only visible in debugging tools. Names are limited to [a-z0-9_].
        hidden = false // Default
        clipping = true // Default

        properties {
            x = 0.px // Default
            y = 0.px // Default
            radius = 0.px // Default

            // Sizing properties, both 100% by default
            // Because this frame is the root component, it's full width and height are the size of the window
            width = 100.percent
            height = 100.percent
        }

        // The root component cannot have effects applied to it
    }.makeRoot(engine) // The root component needs to be attached to our window

    private val box = RectangleComponent().configure {
        name = "box"

        properties {
            x = 25.px
            y = 25.px
            width = 25.percent
            height = LinkedProperty(width as VectorProperty) // We can link properties together
        }
    }.whenMouseClick {
        println("Box clicked at $x, $y")
    }.attachTo(frame) // Finally, we attach the component to another; in this case, the root frame, which all components need to have in their hierarchy.

    private val footer = KotlinFooterComponent().configure {
        name = "footer"

        properties {
            width = 100.percent
            height = 50.px
        }
    }.attachTo(frame)

    init {
        // Or, alternatively, we can listen to events on components after they have been created
        box.whenMouseHover {
            println("Mouse entered box at $x, $y")

            // It's possible to animate properties on the fly
            component.animate {
                // Inside here, we have access to all of our properties
                animateWidth(
                    easing = Easings.IN_OUT_CUBIC,
                    duration = 500L.millis,
                    newValue = 50.percent
                )
            }
        }.whenMouseUnhover {
            println("Mouse exited box at $x, $y")
        }
    }

    // You'll want to wrap all of your frame's IO in exposed functions like this

    fun render() {
        frame.render()
    }

}

// If our component is going too complex (requiring a lot of configuration), we can split it into a separate class.
// Alternatively, if you just need to have it be reusable, this is the best means of doing so.
class KotlinFooterComponent : Component() {

    private val background = RectangleComponent().configure {
        properties {
            width = 100.percent
            height = 100.percent
            fill = Color.BLACK.withAlphaPercentage(50f).asProperty
        }
    }.attachTo(this)

    // TODO
    // private val text = TextComponent("Hello, world!").configure {
    //     properties {
    //         x = centered()
    //         y = centered()
    //     }
    // }.attachTo(background)

}
