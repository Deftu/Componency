package dev.deftu.componency.webcanvas.platform.input

import dev.deftu.componency.input.Key
import dev.deftu.componency.platform.input.KeyboardInput
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.KeyboardEvent

public class CanvasKeyboardInput : KeyboardInput {

    // Track keys pressed
    private val pressedKeys = mutableSetOf<Key>()

    // Track modifier states - update on every key event for accuracy
    private var shiftDown = false
    private var ctrlDown = false
    private var altDown = false
    private var superDown = false

    // CapsLock and NumLock can be tracked via event.getModifierState
    private var capsLockEnabled = false
    private var numLockEnabled = false

    init {
        // Listen on the canvas or window to catch keyboard input
        // window preferred to catch events even if canvas not focused exactly
        window.addEventListener("keydown", { event ->
            event as KeyboardEvent
            updateModifierState(event)
            val key = mapKey(event)
            if (key != null) {
                pressedKeys.add(key)
            }
        })

        window.addEventListener("keyup", { event ->
            event as KeyboardEvent
            updateModifierState(event)
            val key = mapKey(event)
            if (key != null) {
                pressedKeys.remove(key)
            }
        })
    }

    private fun updateModifierState(event: KeyboardEvent) {
        shiftDown = event.shiftKey
        ctrlDown = event.ctrlKey
        altDown = event.altKey
        superDown = event.metaKey
        capsLockEnabled = event.getModifierState("CapsLock")
        numLockEnabled = event.getModifierState("NumLock")
    }

    // Map from KeyboardEvent.key to your Key enum (you'll need to implement this)
    private fun mapKey(event: KeyboardEvent): Key? {
        // Example mapping - implement your own full mapping here
        return when (event.key) {
            "Shift" -> Key.KEY_LSHIFT
            "Control" -> Key.KEY_LCONTROL
//            "Alt" -> Key.
//            "Meta" -> Key.SUPER
            "CapsLock" -> Key.KEY_CAPS_LOCK
            "NumLock" -> Key.KEY_NUM_LOCK
            else -> {
                // Map alphanumeric keys and others here:
                // You have to implement this based on your Key enum values
                null
            }
        }
    }

    override val isShiftKeyDown: Boolean get() = shiftDown
    override val isCtrlKeyDown: Boolean get() = ctrlDown
    override val isAltKeyDown: Boolean get() = altDown
    override val isSuperKeyDown: Boolean get() = superDown
    override val isCapsLockEnabled: Boolean get() = capsLockEnabled
    override val isNumLockEnabled: Boolean get() = numLockEnabled

    override fun isKeyDown(key: Key): Boolean = key in pressedKeys

    override fun setKeyDown(key: Key, isDown: Boolean) {
        if (isDown) pressedKeys.add(key) else pressedKeys.remove(key)
    }

}
