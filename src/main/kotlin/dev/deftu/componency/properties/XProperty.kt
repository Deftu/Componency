package dev.deftu.componency.properties

import dev.deftu.componency.components.Component

public interface XProperty : Property<Float> {

    public fun calculateX(component: Component<*, *>): Float

    public fun getX(component: Component<*, *>): Float {
        if (this.needsRecalculate) {
            this.cachedValue = this.calculateX(component)
            this.needsRecalculate = false
        }

        return this.cachedValue
    }

}
