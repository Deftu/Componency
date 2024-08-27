package dev.deftu.componency.properties

import dev.deftu.componency.components.Component

public interface AngleProperty : Property<Float> {

    public fun calculateAngle(component: Component): Float

    public fun getAngle(component: Component): Float {
        if (this.needsRecalculate) {
            this.cachedValue = this.calculateAngle(component)
            this.needsRecalculate = false
        }

        return this.cachedValue
    }

}
