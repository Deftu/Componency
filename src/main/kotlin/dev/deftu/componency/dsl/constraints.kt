package dev.deftu.componency.dsl

import dev.deftu.componency.components.BaseComponent
import dev.deftu.componency.constraints.*
import dev.deftu.stateful.SimpleState
import dev.deftu.stateful.State
import java.awt.Color

infix fun <T, U : Constraint<T>> U.attachTo(component: BaseComponent) = apply { attach(component) }

operator fun Constraint<Float>.plus(other: Constraint<Float>) = AdditiveConstraint(this, other)
operator fun Constraint<Float>.minus(other: Constraint<Float>) = SubtractiveConstraint(this, other)
operator fun Constraint<Float>.times(factor: State<Float>) = ScaleConstraint(this, 0f).bindScale(factor)
operator fun Constraint<Float>.times(factor: Float) = ScaleConstraint(this, factor)
operator fun Constraint<Float>.div(divisor: State<Float>) = ScaleConstraint(this, 1f).bindScale(divisor)
operator fun Constraint<Float>.div(divisor: Float) = ScaleConstraint(this, 1f / divisor)

fun Constraint<Float>.coerceAtLeast(other: Constraint<Float>) = CoerceAtLeastConstraint(this, other)
fun Constraint<Float>.coerceAtMost(other: Constraint<Float>) = CoerceAtMostConstraint(this, other)
fun Constraint<Float>.coerceIn(min: Constraint<Float>, max: Constraint<Float>) = CoerceInConstraint(this, min, max)

fun min(
    a: Constraint<Float>,
    b: Constraint<Float>
) = MinConstraint(a, b)

fun max(
    a: Constraint<Float>,
    b: Constraint<Float>
) = MaxConstraint(a, b)

fun Number.percent() = RelativeConstraint(this.toFloat())
val Number.percent
    get() = percent()

fun Number.px(
    alignOpposite: Boolean = false,
    alignOutside: Boolean = false
) = PixelConstraint(this.toFloat(), alignOpposite, alignOutside)
val Number.px
    get() = px()

fun Color.toConstraint() = StaticColorConstraint(this)

// Util functions for defining constraints - This just makes the code look nicer.

fun ComponentConstraints.center() = CenterConstraint()

fun ComponentConstraints.children(
    padding: State<Float> = SimpleState(0f)
) = ChildBasedSizeConstraint().bindPadding(padding)
fun ComponentConstraints.children(
    padding: Float = 0f
) = ChildBasedSizeConstraint(padding)
fun ComponentConstraints.children() = ChildBasedSizeConstraint()

fun ComponentConstraints.fill(
    useSiblings: State<Boolean> = SimpleState(true)
) = FillConstraint(false).bindUseSiblings(useSiblings)
fun ComponentConstraints.fill(
    useSiblings: Boolean = true
) = FillConstraint(useSiblings)
fun ComponentConstraints.fill() = FillConstraint(true)

fun ComponentConstraints.mouse(
    alignCentered: State<Boolean> = SimpleState(false)
) = MouseBasedConstraint(false).bindAlignCentered(alignCentered)
fun ComponentConstraints.mouse(
    alignCentered: Boolean = false
) = MouseBasedConstraint(alignCentered)
fun ComponentConstraints.mouse() = MouseBasedConstraint(false)
