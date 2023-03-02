//#if MC>=11900
package test

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import xyz.deftu.componency.animations.Animations
import xyz.deftu.componency.components.WindowComponent
import xyz.deftu.componency.components.impl.*
import xyz.deftu.componency.dsl.*
import xyz.deftu.componency.font.impl.StandardFontProvider
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.text.Text
import xyz.deftu.text.utils.toVanilla
import java.awt.Color
import java.util.*
import kotlin.concurrent.schedule

class TestScreen : Screen(Text.create("Test Screen").toVanilla()) {
    private val window = WindowComponent()
    private val robotoFontProvider = StandardFontProvider("fonts/Roboto-Regular.ttf")

    val box = BoxComponent().configure {
        constraints {
            x = 25.px
            y = 25.px
            width = 250.px
            height = 100.px
            color = Color.GREEN.withAlpha(50).toColorConstraint()
            // radius = 15.px
        }
    }.onMouseLeftClick { x, y ->
        println("Clicked at $x, $y")

        true
    }.attachTo(window)
    val textRoboto = TextComponent(Text.create("Hello, World!\nI am on a new line!")).configure {
        constraints {
            x = 5.px
            y = 5.px
            width *= 2f
            fontProvider = robotoFontProvider
        }
    }.attachTo(box)

    init {
        Timer().schedule(5000) {
            startAnimation()
        }
    }

    private fun startAnimation() {
        textRoboto.animate {
            animateWidth(Animations.IN_OUT_CUBIC, 2.5f, width / 2f)
        }

        box.animate {
            val animation = Animations.IN_OUT_CUBIC
            animateX(animation, 2.5f, 25.px(alignOpposite = true))
            animateY(animation, 2.5f, 25.px(alignOpposite = true))
            animateWidth(animation, 2.5f, width / 2f)
            animateHeight(animation, 2.5f, height * 2f)
            animateColor(animation, 2.5f, Color.RED.withAlpha(80).toColorConstraint())

            whenComplete {
                println("hi")
                textRoboto.animate {
                    animateWidth(Animations.IN_OUT_CUBIC, 2.5f, width * 1.25f)
                    whenComplete {
                        println("hi2")
                    }
                }
            }
        }
    }

    override fun init() {
        window.onWindowResize()
        super.init()
    }

    override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
        window.mouseClick(d.toFloat(), e.toFloat(), i)
        return super.mouseClicked(d, e, i)
    }

    override fun mouseReleased(d: Double, e: Double, i: Int): Boolean {
        window.mouseRelease(d.toFloat(), e.toFloat(), i)
        return super.mouseReleased(d, e, i)
    }

    override fun mouseScrolled(d: Double, e: Double, f: Double): Boolean {
        window.mouseScroll(d.toFloat(), e.toFloat(), f)
        return super.mouseScrolled(d, e, f)
    }

    override fun keyPressed(i: Int, j: Int, k: Int): Boolean {
        window.keyType(i, 0.toChar())
        return super.keyPressed(i, j, k)
    }

    override fun close() {
        window.destroy()
        super.close()
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.renderBackground(matrices)
        window.doRender(MultiMatrixStack(matrices), tickDelta)
        super.render(matrices, mouseX, mouseY, tickDelta)
    }
}
//#endif
