package dev.deftu.componency.components

public class FocusManager {

    public var currentlyFocused: Component<*, *>? = null
        private set

    public var requestingFocus: Component<*, *>? = null
        private set

    public val isFocused: Boolean
        get() = currentlyFocused != null

    public fun handleRequests() {
        if (requestingFocus != currentlyFocused) {
            forceFocus(requestingFocus)
        } else if (requestingFocus == null) {
            releaseFocus()
        }

        requestingFocus = null
    }

    public fun requestFocus(component: Component<*, *>) {
        this.requestingFocus = component
    }

    public fun forceFocus(component: Component<*, *>?) {
        currentlyFocused?.fireUnfocusEvent()
        currentlyFocused = component
        component?.fireFocusEvent()
    }

    public fun releaseFocus() {
        currentlyFocused?.fireUnfocusEvent()
        currentlyFocused = null
    }

}
