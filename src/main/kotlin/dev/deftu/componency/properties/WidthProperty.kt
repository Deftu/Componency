package dev.deftu.componency.properties

import dev.deftu.componency.components.Component

public interface WidthProperty : Property<Float> {

    public fun calculateWidth(component: Component): Float

    public fun getWidth(component: Component): Float {
        if (this.needsRecalculate) {
            this.cachedValue = this.calculateWidth(component)
            this.needsRecalculate = false
        }

        return this.cachedValue
    }

}
