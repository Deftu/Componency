package dev.deftu.componency.components

import dev.deftu.componency.properties.Property
import java.lang.reflect.Field

public actual interface ComponentPropertiesPlatformAware<T : Component<T, C>, C : ComponentProperties<T, C>> {

    public actual val self: C

    public fun defineAnimatingProperty(field: Field) {
        field.isAccessible = true
        self.animatingProperties.add({ newValue: Property<*> ->
            field.set(this, newValue)
        } to {
            field.get(this) as Property<*>
        })
    }

}
