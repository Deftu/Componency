package dev.deftu.componency.nav

import dev.deftu.componency.components.Component
import dev.deftu.stateful.utils.mutableStateOf

public class NavController {

    private val routes = mutableMapOf<String, () -> Component<*, *>>()

    internal val navStack = mutableStateOf<String?>(null)

    public val currentRoute: String?
        get() = navStack.get()

    public val currentRouteComponent: Component<*, *>?
        get() = currentRoute?.let { routes[it]?.invoke() }

    public fun register(route: String, component: () -> Component<*, *>) {
        routes[route] = component
    }

    public fun navigate(route: String) {
        val component = routes[route]?.invoke()
        if (component != null) {
            navStack.set(route)
        } else {
            throw IllegalArgumentException("Route $route not found")
        }
    }

}
