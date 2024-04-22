package dev.deftu.componency

import dev.deftu.stateful.SimpleState

class ExampleScreen : ComponencyScreen() {
    private val box by BoxComponent().configure {
        constraints {
            x = centered()
            y = centered()
            width = 50.percent()
            height = 50.percent()
        }

        effects {
            blur(radius = 5.px())

            outline {
                thickness = 2.px()
                color = Color(255, 255, 255).toConstraint()
            }
        }

        effects.scissor {
            x = 0.px()
            y = 0.px()
            width = 100.percent()
            height = 100.percent()
        }
    }.attachTo(window)

    private val framerateState = SimpleState("")
    private val framerate by TextComponent().configure {
        constraints {
            x = 0.px()
            y = 0.px()
        }
    }.bindValue(framerateState).attachTo(window)

    init {
        window.onRenderPhase { (phase) ->
            if (phase == RenderPhase.POST_CHILDREN) {
                val fps = TODO()
                framerateState.set(fps)
            }
        }.onMouseClick { (x, y, button) ->
            box.effects.blur(radius = 4.px()) // can be reassigned like this at any point
            box.animate {
                box.effects.get<BlurEffect>().radius.animate(
                    easing = Animations.IN_OUT_CUBIC,
                    duration = 500.millis(),
                    to = 10.px()
                )

                width.animate(
                    easing = Animations.IN_OUT_CUBIC,
                    duration = 500.millis(),
                    to = 100.percent()
                )
            }
        }.onMouseHover { event ->

        }
    }

    override fun supportsFramePausing() = true
}
