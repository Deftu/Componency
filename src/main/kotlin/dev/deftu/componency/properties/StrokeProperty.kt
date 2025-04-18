package dev.deftu.componency.properties

import dev.deftu.componency.components.Component
import dev.deftu.componency.stroke.Stroke

public interface StrokeProperty : Property<Stroke> {

    public fun calculateStroke(component: Component<*, *>): Stroke

    public fun getStroke(component: Component<*, *>): Stroke {
        if (this.needsRecalculate) {
            this.cachedValue = this.calculateStroke(component)
            this.needsRecalculate = false
        }

        return this.cachedValue
    }

}
