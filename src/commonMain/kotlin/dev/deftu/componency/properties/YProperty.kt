package dev.deftu.componency.properties

import dev.deftu.componency.components.Component

public interface YProperty : Property<Float> {

    public fun calculateY(component: Component<*, *>): Float

    public fun getY(component: Component<*, *>): Float {
        if (this.needsRecalculate) {
            this.cachedValue = this.calculateY(component)
            this.needsRecalculate = false
        }

        return this.cachedValue
    }

}
