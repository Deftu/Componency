package dev.deftu.componency.properties

import dev.deftu.componency.components.Component

public interface RadialProperty : Property<Float> {

    public fun calculateRadius(component: Component): Float

    public fun getRadius(component: Component): Float {
        if (this.needsRecalculate) {
            this.cachedValue = this.calculateRadius(component)
            this.needsRecalculate = false
        }

        return this.cachedValue
    }

}
