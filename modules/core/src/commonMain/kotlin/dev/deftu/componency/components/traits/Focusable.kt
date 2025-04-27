package dev.deftu.componency.components.traits

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.input.Key
import dev.deftu.componency.input.MouseButton
import dev.deftu.stateful.MutableState
import dev.deftu.stateful.utils.mutableStateOf
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

public data class Focusable(public val isDisabled: MutableState<Boolean> = mutableStateOf(false)) : Trait {

    public companion object {

        @JvmStatic
        public val Default: Focusable = Focusable()

        @JvmStatic
        public val Disabled: Focusable = Focusable(true)

        @JvmStatic
        public fun of(isDisabled: Boolean): Focusable {
            return if (isDisabled) Disabled else Default
        }

        @JvmStatic
        @JvmOverloads
        public fun <T : Component<T, C>, C : ComponentProperties<T, C>> focusable(
            component: Component<T, C>,
            isDisabled: Boolean = false
        ): T {
            component.addTrait(Focusable(isDisabled))
            setupKeyboardNavigation(component)
            return component as T
        }

        @JvmStatic
        public fun <T : Component<T, C>, C : ComponentProperties<T, C>> isFocusable(component: Component<T, C>): Boolean {
            return component.hasTrait(Focusable::class)
        }

        @JvmStatic
        public fun <T : Component<T, C>, C : ComponentProperties<T, C>> isDisabled(component: Component<T, C>): Boolean {
            return component.getTrait(Focusable::class)?.isDisabled?.get() ?: false
        }

        @JvmStatic
        public fun <T : Component<T, C>, C : ComponentProperties<T, C>> enableFocusable(component: Component<T, C>): T {
            val current = component.getTrait(Focusable::class)
            return if (current != null) {
                current.isDisabled.set(false)
                component as T
            } else {
                focusable(component, false)
            }
        }

        @JvmStatic
        public fun <T : Component<T, C>, C : ComponentProperties<T, C>> disableFocusable(component: Component<T, C>): T {
            val current = component.getTrait(Focusable::class)
            return if (current != null) {
                current.isDisabled.set(true)
                component as T
            } else {
                focusable(component, true)
            }
        }

        @JvmStatic
        public fun <T : Component<T, C>, C : ComponentProperties<T, C>> toggleFocusable(component: Component<T, C>): T {
            val current = component.getTrait(Focusable::class)
            return if (current != null) {
                current.isDisabled.set(!current.isDisabled.get())
                component as T
            } else {
                focusable(component, false)
            }
        }

        @JvmStatic
        public fun <T : Component<T, C>, C : ComponentProperties<T, C>> setFocusableDisabled(
            component: Component<T, C>,
            isDisabled: Boolean
        ): T {
            val current = component.getTrait(Focusable::class)
            return if (current != null) {
                current.isDisabled.set(isDisabled)
                component as T
            } else {
                focusable(component, isDisabled)
            }
        }

    }

    public constructor(isDisabled: Boolean) : this(mutableStateOf(isDisabled))

}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> T.focusable(isDisabled: Boolean = false): T {
    return Focusable.focusable(this, isDisabled)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> T.isFocusable(): Boolean {
    return Focusable.isFocusable(this)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> T.isDisabled(): Boolean {
    return Focusable.isDisabled(this)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> T.enableFocusable(): T {
    return Focusable.enableFocusable(this)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> T.disableFocusable(): T {
    return Focusable.disableFocusable(this)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> T.toggleFocusable(): T {
    return Focusable.toggleFocusable(this)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> T.setFocusableDisabled(isDisabled: Boolean): T {
    return Focusable.setFocusableDisabled(this, isDisabled)
}

private fun <T : Component<T, C>, C : ComponentProperties<T, C>> setupKeyboardNavigation(component: Component<T, C>) {
    component.onKeyPress {
        if (!component.isFocused) {
            return@onKeyPress
        }

        when (key) {
            Key.KEY_TAB -> {
                findNextFocusableComponent(component, backwards = modifiers.isShift)
                stopBubbling()
            }

            Key.KEY_ENTER -> {
                simulateCenteredInteraction(component)
                stopBubbling()
            }

            else -> {} // no-op
        }
    }
}

private fun <T : Component<T, C>, C : ComponentProperties<T, C>> findNextFocusableComponent(component: Component<T, C>, backwards: Boolean = false) {
    val root = Component.findRoot(component)
    val focusableComponents = root.findChildrenByTrait<Focusable>(recursive = true) { _, trait -> !trait.isDisabled.get() }
    val currentIndex = focusableComponents.indexOf(component)
    if (currentIndex == -1 || focusableComponents.isEmpty()) {
        return
    }

    val direction = if (backwards) -1 else 1
    val nextIndex = (focusableComponents.size + currentIndex + direction) % focusableComponents.size
    val nextComponent = focusableComponents[nextIndex]

    nextComponent.requestFocus()
}

private fun <T : Component<T, C>, C : ComponentProperties<T, C>> simulateCenteredInteraction(component: Component<T, C>) {
    if (!component.hasParent) {
        return
    }

    val x = (component.left + component.width / 2).toDouble()
    val y = (component.top + component.height / 2).toDouble()
    component.handlePointerClick(x, y, MouseButton.LEFT)
}
