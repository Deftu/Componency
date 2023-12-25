package dev.deftu.componency.dsl

import dev.deftu.componency.constraints.PixelConstraint
import dev.deftu.componency.constraints.StaticColorConstraint
import dev.deftu.stateful.State
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
    get() = this.toConstraint()
