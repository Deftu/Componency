package xyz.deftu.componency.dsl

import xyz.deftu.componency.constraints.PixelConstraint
import xyz.deftu.componency.constraints.StaticColorConstraint
import xyz.deftu.state.State
import java.awt.Color

fun State<Float>.pixels(
    alignOpposite: Boolean = false,
    alignOutside: Boolean = false
) = PixelConstraint(0f, alignOpposite, alignOutside).bindValue(this)
@JvmName("pixelsNumber")
fun State<Number>.pixels(
    alignOpposite: Boolean = false,
    alignOutside: Boolean = false
) = PixelConstraint(0f, alignOpposite, alignOutside).bindValue(map {
    it.toFloat()
})
val State<Number>.pixels
    get() = pixels()

fun State<Color>.toConstraint() = StaticColorConstraint(get()).bindValue(this)
val State<Color>.constraint
    get() = toConstraint()
