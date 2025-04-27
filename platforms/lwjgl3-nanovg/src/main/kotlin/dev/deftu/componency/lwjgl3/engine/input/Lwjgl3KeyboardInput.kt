package dev.deftu.componency.lwjgl3.engine.input

import dev.deftu.componency.input.Key
import dev.deftu.componency.platform.input.KeyboardInput
import org.lwjgl.glfw.GLFW

class Lwjgl3KeyboardInput(private val handle: Long) : KeyboardInput {

    companion object {

        private val keyMappings = mapOf(
            // Unknown
            Key.KEY_UNKNOWN         to GLFW.GLFW_KEY_UNKNOWN,

            // Modifiers / Control Keys
            Key.KEY_ESCAPE          to GLFW.GLFW_KEY_ESCAPE,
            Key.KEY_LMETA           to GLFW.GLFW_KEY_LEFT_SUPER,
            Key.KEY_RMETA           to GLFW.GLFW_KEY_RIGHT_SUPER,
            Key.KEY_LCONTROL        to GLFW.GLFW_KEY_LEFT_CONTROL,
            Key.KEY_RCONTROL        to GLFW.GLFW_KEY_RIGHT_CONTROL,
            Key.KEY_LSHIFT          to GLFW.GLFW_KEY_LEFT_SHIFT,
            Key.KEY_RSHIFT          to GLFW.GLFW_KEY_RIGHT_SHIFT,
            Key.KEY_LMENU           to GLFW.GLFW_KEY_LEFT_ALT,
            Key.KEY_RMENU           to GLFW.GLFW_KEY_RIGHT_ALT,
            Key.KEY_TAB             to GLFW.GLFW_KEY_TAB,
            Key.KEY_BACKSPACE       to GLFW.GLFW_KEY_BACKSPACE,
            Key.KEY_ENTER           to GLFW.GLFW_KEY_ENTER,

            // Letters
            Key.KEY_A               to GLFW.GLFW_KEY_A,
            Key.KEY_B               to GLFW.GLFW_KEY_B,
            Key.KEY_C               to GLFW.GLFW_KEY_C,
            Key.KEY_D               to GLFW.GLFW_KEY_D,
            Key.KEY_E               to GLFW.GLFW_KEY_E,
            Key.KEY_F               to GLFW.GLFW_KEY_F,
            Key.KEY_G               to GLFW.GLFW_KEY_G,
            Key.KEY_H               to GLFW.GLFW_KEY_H,
            Key.KEY_I               to GLFW.GLFW_KEY_I,
            Key.KEY_J               to GLFW.GLFW_KEY_J,
            Key.KEY_K               to GLFW.GLFW_KEY_K,
            Key.KEY_L               to GLFW.GLFW_KEY_L,
            Key.KEY_M               to GLFW.GLFW_KEY_M,
            Key.KEY_N               to GLFW.GLFW_KEY_N,
            Key.KEY_O               to GLFW.GLFW_KEY_O,
            Key.KEY_P               to GLFW.GLFW_KEY_P,
            Key.KEY_Q               to GLFW.GLFW_KEY_Q,
            Key.KEY_R               to GLFW.GLFW_KEY_R,
            Key.KEY_S               to GLFW.GLFW_KEY_S,
            Key.KEY_T               to GLFW.GLFW_KEY_T,
            Key.KEY_U               to GLFW.GLFW_KEY_U,
            Key.KEY_V               to GLFW.GLFW_KEY_V,
            Key.KEY_W               to GLFW.GLFW_KEY_W,
            Key.KEY_X               to GLFW.GLFW_KEY_X,
            Key.KEY_Y               to GLFW.GLFW_KEY_Y,
            Key.KEY_Z               to GLFW.GLFW_KEY_Z,

            // Numbers
            Key.KEY_0               to GLFW.GLFW_KEY_0,
            Key.KEY_1               to GLFW.GLFW_KEY_1,
            Key.KEY_2               to GLFW.GLFW_KEY_2,
            Key.KEY_3               to GLFW.GLFW_KEY_3,
            Key.KEY_4               to GLFW.GLFW_KEY_4,
            Key.KEY_5               to GLFW.GLFW_KEY_5,
            Key.KEY_6               to GLFW.GLFW_KEY_6,
            Key.KEY_7               to GLFW.GLFW_KEY_7,
            Key.KEY_8               to GLFW.GLFW_KEY_8,
            Key.KEY_9               to GLFW.GLFW_KEY_9,

            // Symbols
            Key.KEY_GRAVE           to GLFW.GLFW_KEY_GRAVE_ACCENT,
            Key.KEY_MINUS           to GLFW.GLFW_KEY_MINUS,
            Key.KEY_EQUALS          to GLFW.GLFW_KEY_EQUAL,
            Key.KEY_LBRACKET        to GLFW.GLFW_KEY_LEFT_BRACKET,
            Key.KEY_RBRACKET        to GLFW.GLFW_KEY_RIGHT_BRACKET,
            Key.KEY_BACKSLASH       to GLFW.GLFW_KEY_BACKSLASH,
            Key.KEY_SEMICOLON       to GLFW.GLFW_KEY_SEMICOLON,
            Key.KEY_APOSTROPHE      to GLFW.GLFW_KEY_APOSTROPHE,
            Key.KEY_COMMA           to GLFW.GLFW_KEY_COMMA,
            Key.KEY_PERIOD          to GLFW.GLFW_KEY_PERIOD,
            Key.KEY_SLASH           to GLFW.GLFW_KEY_SLASH,
            Key.KEY_SPACE           to GLFW.GLFW_KEY_SPACE,

            // Arrows
            Key.KEY_LEFT            to GLFW.GLFW_KEY_LEFT,
            Key.KEY_UP              to GLFW.GLFW_KEY_UP,
            Key.KEY_RIGHT           to GLFW.GLFW_KEY_RIGHT,
            Key.KEY_DOWN            to GLFW.GLFW_KEY_DOWN,

            // Locks
            Key.KEY_CAPS_LOCK       to GLFW.GLFW_KEY_CAPS_LOCK,
            Key.KEY_NUM_LOCK        to GLFW.GLFW_KEY_NUM_LOCK,
            Key.KEY_SCROLL_LOCK     to GLFW.GLFW_KEY_SCROLL_LOCK,

            // Special Keys
            Key.KEY_INSERT          to GLFW.GLFW_KEY_INSERT,
            Key.KEY_DELETE          to GLFW.GLFW_KEY_DELETE,
            Key.KEY_HOME            to GLFW.GLFW_KEY_HOME,
            Key.KEY_END             to GLFW.GLFW_KEY_END,
            Key.KEY_PAGE_UP         to GLFW.GLFW_KEY_PAGE_UP,
            Key.KEY_PAGE_DOWN       to GLFW.GLFW_KEY_PAGE_DOWN,
            Key.KEY_PRINT_SCREEN    to GLFW.GLFW_KEY_PRINT_SCREEN,
            Key.KEY_PAUSE_BREAK     to GLFW.GLFW_KEY_PAUSE,

            // Function Keys
            Key.KEY_F1              to GLFW.GLFW_KEY_F1,
            Key.KEY_F2              to GLFW.GLFW_KEY_F2,
            Key.KEY_F3              to GLFW.GLFW_KEY_F3,
            Key.KEY_F4              to GLFW.GLFW_KEY_F4,
            Key.KEY_F5              to GLFW.GLFW_KEY_F5,
            Key.KEY_F6              to GLFW.GLFW_KEY_F6,
            Key.KEY_F7              to GLFW.GLFW_KEY_F7,
            Key.KEY_F8              to GLFW.GLFW_KEY_F8,
            Key.KEY_F9              to GLFW.GLFW_KEY_F9,
            Key.KEY_F10             to GLFW.GLFW_KEY_F10,
            Key.KEY_F11             to GLFW.GLFW_KEY_F11,
            Key.KEY_F12             to GLFW.GLFW_KEY_F12,
            Key.KEY_F13             to GLFW.GLFW_KEY_F13,
            Key.KEY_F14             to GLFW.GLFW_KEY_F14,
            Key.KEY_F15             to GLFW.GLFW_KEY_F15,
            Key.KEY_F16             to GLFW.GLFW_KEY_F16,
            Key.KEY_F17             to GLFW.GLFW_KEY_F17,
            Key.KEY_F18             to GLFW.GLFW_KEY_F18,
            Key.KEY_F19             to GLFW.GLFW_KEY_F19,

            // Numpad
            Key.KEY_NUMPAD_DIVIDE   to GLFW.GLFW_KEY_KP_DIVIDE,
            Key.KEY_NUMPAD_MULTIPLY to GLFW.GLFW_KEY_KP_MULTIPLY,
            Key.KEY_NUMPAD_ADD      to GLFW.GLFW_KEY_KP_ADD,
            Key.KEY_NUMPAD_SUBTRACT to GLFW.GLFW_KEY_KP_SUBTRACT,
            Key.KEY_NUMPAD_DECIMAL  to GLFW.GLFW_KEY_KP_DECIMAL,
            Key.KEY_NUMPAD0         to GLFW.GLFW_KEY_KP_0,
            Key.KEY_NUMPAD1         to GLFW.GLFW_KEY_KP_1,
            Key.KEY_NUMPAD2         to GLFW.GLFW_KEY_KP_2,
            Key.KEY_NUMPAD3         to GLFW.GLFW_KEY_KP_3,
            Key.KEY_NUMPAD4         to GLFW.GLFW_KEY_KP_4,
            Key.KEY_NUMPAD5         to GLFW.GLFW_KEY_KP_5,
            Key.KEY_NUMPAD6         to GLFW.GLFW_KEY_KP_6,
            Key.KEY_NUMPAD7         to GLFW.GLFW_KEY_KP_7,
            Key.KEY_NUMPAD8         to GLFW.GLFW_KEY_KP_8,
            Key.KEY_NUMPAD9         to GLFW.GLFW_KEY_KP_9
        )

    }

    override val isShiftKeyDown: Boolean
        get() {
            return GLFW.glfwGetKey(handle, keyMappings[Key.KEY_LSHIFT] ?: return false) == GLFW.GLFW_PRESS ||
                    GLFW.glfwGetKey(handle, keyMappings[Key.KEY_RSHIFT] ?: return false) == GLFW.GLFW_PRESS
        }

    override val isCtrlKeyDown: Boolean
        get() {
            return GLFW.glfwGetKey(handle, keyMappings[Key.KEY_LCONTROL] ?: return false) == GLFW.GLFW_PRESS ||
                    GLFW.glfwGetKey(handle, keyMappings[Key.KEY_RCONTROL] ?: return false) == GLFW.GLFW_PRESS
        }

    override val isAltKeyDown: Boolean
        get() {
            return GLFW.glfwGetKey(handle, keyMappings[Key.KEY_LMENU] ?: return false) == GLFW.GLFW_PRESS ||
                    GLFW.glfwGetKey(handle, keyMappings[Key.KEY_RMENU] ?: return false) == GLFW.GLFW_PRESS
        }

    override val isSuperKeyDown: Boolean
        get() {
            return GLFW.glfwGetKey(handle, keyMappings[Key.KEY_LMETA] ?: return false) == GLFW.GLFW_PRESS ||
                    GLFW.glfwGetKey(handle, keyMappings[Key.KEY_RMETA] ?: return false) == GLFW.GLFW_PRESS
        }

    override val isCapsLockEnabled: Boolean
        get() {
            return GLFW.glfwGetKey(handle, keyMappings[Key.KEY_CAPS_LOCK] ?: return false) == GLFW.GLFW_PRESS
        }

    override val isNumLockEnabled: Boolean
        get() {
            return GLFW.glfwGetKey(handle, keyMappings[Key.KEY_NUM_LOCK] ?: return false) == GLFW.GLFW_PRESS
        }

    fun getKey(code: Int): Key {
        return keyMappings.entries.firstOrNull { (_, keyCode) ->
            keyCode == code
        }?.key ?: Key.KEY_UNKNOWN
    }

    override fun isKeyDown(key: Key): Boolean {
        return GLFW.glfwGetKey(handle, keyMappings[key] ?: return false) == GLFW.GLFW_PRESS
    }

    override fun setKeyDown(key: Key, isDown: Boolean) {
        throw UnsupportedOperationException("Setting key state is not supported in GLFW.")
    }

}
