@file:Suppress("UNCHECKED_CAST")

package dev.deftu.componency.components

import dev.deftu.componency.components.traits.Trait
import java.util.function.Consumer

public actual interface ComponentPlatformAware<T : Component<T, C>, C : ComponentProperties<T, C>> {

    public actual val self: T

    public fun configure(block: Consumer<C>): T = apply {
        block.accept(self.properties)
    } as T

    public fun <T : Trait> getTrait(type: Class<T>): T? {
        return self.getTraits().firstOrNull { type.isInstance(it) } as? T
    }

    public fun hasTrait(type: Class<out Trait>): Boolean {
        return self.getTraits().any { type.isInstance(it) }
    }

    public fun <T : Trait> findChildrenByTrait(
        type: Class<T>,
        recursive: Boolean = false,
        predicate: (Component<*, *>, T) -> Boolean = { _, _ -> true }
    ): List<Component<*, *>> {
        val result = mutableListOf<Component<*, *>>()
        fun findChildren(component: Component<*, *>) {
            if ((component as ComponentPlatformAware<*, *>).getTrait(type)?.let { predicate(component, it) } == true) {
                result.add(component)
            }

            if (recursive) {
                component.getChildren().forEach(::findChildren)
            }
        }

        findChildren(self)
        return result
    }

}
