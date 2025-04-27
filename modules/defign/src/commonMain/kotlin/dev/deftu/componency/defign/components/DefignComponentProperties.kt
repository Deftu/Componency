package dev.deftu.componency.defign.components

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.stateful.MutableState
import dev.deftu.stateful.utils.mutableStateOf

public open class DefignComponentProperties<T : Component<T, C>, C : ComponentProperties<T, C>>(
    component: Component<T, C>
) : ComponentProperties<T, C>(component) {

    public var isDark: MutableState<Boolean> = mutableStateOf(true)

    override fun inheritFrom(parent: ComponentProperties<*, *>) {
        super.inheritFrom(parent)
        if (parent is DefignComponentProperties<*, *>) {
            isDark.set(parent.isDark.get())
        }
    }

    override fun copyFrom(properties: ComponentProperties<*, *>): ComponentProperties<T, C> {
        if (properties is DefignComponentProperties<*, *>) {
            isDark.set(properties.isDark.get())
        }

        return super.copyFrom(properties)
    }

}
