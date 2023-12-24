package dev.deftu.componency.constraints

import dev.deftu.componency.components.BaseComponent
import java.awt.Color

interface Constraint<T> {
    var cached: T
    var recalculate: Boolean
    var attachedTo: BaseComponent?

    fun attach(component: BaseComponent?) = apply {
        attachedTo = component
    }

    fun handleAnimate() {
        recalculate = true
    }

    fun handleDestroy() {
    }
}

interface PaddingConstraint {
    fun getVerticalPadding(component: BaseComponent): Float
    fun getHorizontalPadding(component: BaseComponent): Float
}

interface XConstraint : Constraint<Float> {
    fun getImplValueForX(component: BaseComponent): Float

    fun getValueForX(component: BaseComponent): Float {
        if (recalculate) {
            cached = getImplValueForX(component)
            recalculate = false
        }

        return cached
    }
}

interface YConstraint : Constraint<Float> {
    fun getImplValueForY(component: BaseComponent): Float

    fun getValueForY(component: BaseComponent): Float {
        if (recalculate) {
            cached = getImplValueForY(component)
            recalculate = false
        }

        return cached
    }
}

interface PositionalConstraint : XConstraint, YConstraint

interface WidthConstraint : Constraint<Float> {
    fun getImplValueForWidth(component: BaseComponent): Float

    fun getValueForWidth(component: BaseComponent): Float {
        if (recalculate) {
            cached = getImplValueForWidth(component)
            recalculate = false
        }

        return cached
    }
}

interface HeightConstraint : Constraint<Float> {
    fun getImplValueForHeight(component: BaseComponent): Float

    fun getValueForHeight(component: BaseComponent): Float {
        if (recalculate) {
            cached = getImplValueForHeight(component)
            recalculate = false
        }

        return cached
    }
}

interface RadiusConstraint : Constraint<Float> {
    fun getImplValueForRadius(component: BaseComponent): Float

    fun getValueForRadius(component: BaseComponent): Float {
        if (recalculate) {
            cached = getImplValueForRadius(component)
            recalculate = false
        }

        return cached
    }
}

interface SizeConstraint : WidthConstraint, HeightConstraint, RadiusConstraint

interface ColorConstraint : Constraint<Color> {
    fun getImplValueForColor(component: BaseComponent): Color

    fun getValueForColor(component: BaseComponent): Color {
        if (recalculate) {
            cached = getImplValueForColor(component)
            recalculate = false
        }

        return cached
    }
}

interface BoxConstraint : PositionalConstraint, SizeConstraint
