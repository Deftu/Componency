@file:Suppress("FunctionName")

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.components.impl.*
import dev.deftu.componency.dsl.*
import dev.deftu.componency.platform.Platform
import dev.deftu.textile.SimpleTextHolder

class KotlinExampleUI(platform: Platform) {

    private val frame = Frame("window") { // A name is optional. Only visible in debugging tools. Names are limited to [a-z0-9_].
        position(0.px, 0.px) // Defaults both to 0 already
        size(100.percent, 100.percent) // Sizing properties. You likely want both to be 100% for your root component so that it fills the window

        // Nesting this component inside our frame tells the backend to create a new component
        Rectangle("box") {
            position(25.px, 25.px)
            width = 25.percent
            height = linked(width) // We can link properties together when we aren't already grouping together / want to maintain a reference to the property

            onPointerClick {
                println("Box clicked at $x, $y")
            }

            onHover {
                println("Box hovered at $x, $y")

                // TODO: Animate
            }

            onUnhover {
                println("Box unhovered at $x, $y")
            }
        }

        Footer()
    }.makeRoot(platform) // Inform our root frame that it is the root component and provide it with our platform-specific implementation

}

// If our component is going too complex (requiring a lot of configuration), we can split it into a sub-component function.
// Alternatively, if you just need to have it be reusable, this is the best means of doing so.
fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.Footer(
    name: String? = null,
    block: RectangleComponentProperties.() -> Unit = {}
): C = apply {
    Rectangle(name) {
        size(100.percent, 50.px)
        y = 0.px(isInverse = true)
        fill = Color.BLACK.withAlphaPercentage(50f).asProperty

        Text("text") {
            position(centered, centered)
            fill = Color.WHITE.asProperty
            text = SimpleTextHolder("Hello, world!")
        }

        block()
    }
}
