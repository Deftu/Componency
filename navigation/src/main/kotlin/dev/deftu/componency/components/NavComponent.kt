package dev.deftu.componency.components

import dev.deftu.componency.nav.NavController
import org.intellij.lang.annotations.Pattern

public class NavComponentProperties(component: NavComponent) : ComponentProperties<NavComponent, NavComponentProperties>(component) {

    public var controller: NavController? = null

}

public class NavComponent : Component<NavComponent, NavComponentProperties>(::NavComponentProperties) {

    private var callback: (() -> Unit)? = null
    private var currentRoute: Component<*, *>? = null

    override fun initialize() {
        if (callback != null) {
            return
        }

        callback = properties.controller?.navStack?.subscribe { _ ->
            val newRoute = properties.controller?.currentRouteComponent
            currentRoute?.let(::removeChild)
            if (newRoute != null) {
                addChild(newRoute)
                currentRoute = newRoute
            }
        }
    }

    // TODO: dispose callback when component is disposed

}

public fun Nav(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: NavComponentProperties.() -> Unit = {}
): NavComponent {
    val component = NavComponent()
    component.properties.name = name
    component.properties.block()
    return component
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.Nav(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: NavComponentProperties.() -> Unit = {}
): T {
    val component = NavComponent()
    component.properties.name = name
    component.properties.block()
    component.attachTo(this@Nav.component)
    return component as T
}
