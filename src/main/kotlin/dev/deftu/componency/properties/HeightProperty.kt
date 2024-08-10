package dev.deftu.componency.properties

import dev.deftu.componency.components.Component

public interface HeightProperty : Property<Float> {

    public fun calculateHeight(component: Component): Float

    public fun getHeight(component: Component): Float {
        if (this.needsRecalculate) {
            this.cachedValue = this.calculateHeight(component)
            this.needsRecalculate = false
        }

        return this.cachedValue
    }

}
