package dev.deftu.componency.properties

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.Component

public interface ColorProperty : Property<Color> {

    public fun calculateColor(component: Component<*, *>): Color

    public fun getColor(component: Component<*, *>): Color {
        if (this.needsRecalculate) {
            this.cachedValue = this.calculateColor(component)
            this.needsRecalculate = false
        }

        return this.cachedValue
    }

}
