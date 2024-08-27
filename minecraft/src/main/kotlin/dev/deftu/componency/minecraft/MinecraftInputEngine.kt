package dev.deftu.componency.minecraft

import dev.deftu.componency.engine.InputEngine
import dev.deftu.componency.input.Key
import dev.deftu.componency.input.MouseButton
import dev.deftu.omnicore.client.OmniKeyboard
import dev.deftu.omnicore.client.OmniMouse

public object MinecraftInputEngine : InputEngine {

    override val mouseX: Float
        get() = OmniMouse.scaledX.toFloat()

    override val mouseY: Float
        get() = OmniMouse.scaledY.toFloat()

    private val mouseMappings = mapOf(
        MouseButton.LEFT to OmniMouse.LEFT,
        MouseButton.RIGHT to OmniMouse.RIGHT,
        MouseButton.MIDDLE to OmniMouse.MIDDLE,
        MouseButton.BACK to OmniMouse.BACK,
        MouseButton.FORWARD to OmniMouse.FORWARD,
        MouseButton.BUTTON6 to OmniMouse.BUTTON6,
        MouseButton.BUTTON7 to OmniMouse.BUTTON7,
        MouseButton.BUTTON8 to OmniMouse.BUTTON8
    )

    private val keyMappings = mapOf(
        Key.KEY_ESCAPE to OmniKeyboard.KEY_ESCAPE,
        Key.KEY_LMETA to OmniKeyboard.KEY_LMETA,
        Key.KEY_RMETA to OmniKeyboard.KEY_RMETA,
        Key.KEY_LCONTROL to OmniKeyboard.KEY_LCONTROL,
        Key.KEY_RCONTROL to OmniKeyboard.KEY_RCONTROL,
        Key.KEY_LSHIFT to OmniKeyboard.KEY_LSHIFT,
        Key.KEY_RSHIFT to OmniKeyboard.KEY_RSHIFT,
        Key.KEY_LMENU to OmniKeyboard.KEY_LMENU,
        Key.KEY_RMENU to OmniKeyboard.KEY_RMENU,
        Key.KEY_MINUS to OmniKeyboard.KEY_MINUS,
        Key.KEY_EQUALS to OmniKeyboard.KEY_EQUALS,
        Key.KEY_BACKSPACE to OmniKeyboard.KEY_BACKSPACE,
        Key.KEY_ENTER to OmniKeyboard.KEY_ENTER,
        Key.KEY_TAB to OmniKeyboard.KEY_TAB,
        Key.KEY_LBRACKET to OmniKeyboard.KEY_LBRACKET,
        Key.KEY_RBRACKET to OmniKeyboard.KEY_RBRACKET,
        Key.KEY_SEMICOLON to OmniKeyboard.KEY_SEMICOLON,
        Key.KEY_APOSTROPHE to OmniKeyboard.KEY_APOSTROPHE,
        Key.KEY_GRAVE to OmniKeyboard.KEY_GRAVE,
        Key.KEY_BACKSLASH to OmniKeyboard.KEY_BACKSLASH,
        Key.KEY_COMMA to OmniKeyboard.KEY_COMMA,
        Key.KEY_PERIOD to OmniKeyboard.KEY_PERIOD,
        Key.KEY_SLASH to OmniKeyboard.KEY_SLASH,
        Key.KEY_MULTIPLY to OmniKeyboard.KEY_MULTIPLY,
        Key.KEY_SPACE to OmniKeyboard.KEY_SPACE,
        Key.KEY_CAPITAL to OmniKeyboard.KEY_CAPITAL,
        Key.KEY_LEFT to OmniKeyboard.KEY_LEFT,
        Key.KEY_UP to OmniKeyboard.KEY_UP,
        Key.KEY_RIGHT to OmniKeyboard.KEY_RIGHT,
        Key.KEY_DOWN to OmniKeyboard.KEY_DOWN,
        Key.KEY_NUMLOCK to OmniKeyboard.KEY_NUMLOCK,
        Key.KEY_SCROLL to OmniKeyboard.KEY_SCROLL,
        Key.KEY_SUBTRACT to OmniKeyboard.KEY_SUBTRACT,
        Key.KEY_ADD to OmniKeyboard.KEY_ADD,
        Key.KEY_DIVIDE to OmniKeyboard.KEY_DIVIDE,
        Key.KEY_DECIMAL to OmniKeyboard.KEY_DECIMAL,
        Key.KEY_NUMPAD0 to OmniKeyboard.KEY_NUMPAD0,
        Key.KEY_NUMPAD1 to OmniKeyboard.KEY_NUMPAD1,
        Key.KEY_NUMPAD2 to OmniKeyboard.KEY_NUMPAD2,
        Key.KEY_NUMPAD3 to OmniKeyboard.KEY_NUMPAD3,
        Key.KEY_NUMPAD4 to OmniKeyboard.KEY_NUMPAD4,
        Key.KEY_NUMPAD5 to OmniKeyboard.KEY_NUMPAD5,
        Key.KEY_NUMPAD6 to OmniKeyboard.KEY_NUMPAD6,
        Key.KEY_NUMPAD7 to OmniKeyboard.KEY_NUMPAD7,
        Key.KEY_NUMPAD8 to OmniKeyboard.KEY_NUMPAD8,
        Key.KEY_NUMPAD9 to OmniKeyboard.KEY_NUMPAD9,
        Key.KEY_A to OmniKeyboard.KEY_A,
        Key.KEY_B to OmniKeyboard.KEY_B,
        Key.KEY_C to OmniKeyboard.KEY_C,
        Key.KEY_D to OmniKeyboard.KEY_D,
        Key.KEY_E to OmniKeyboard.KEY_E,
        Key.KEY_F to OmniKeyboard.KEY_F,
        Key.KEY_G to OmniKeyboard.KEY_G,
        Key.KEY_H to OmniKeyboard.KEY_H,
        Key.KEY_I to OmniKeyboard.KEY_I,
        Key.KEY_J to OmniKeyboard.KEY_J,
        Key.KEY_K to OmniKeyboard.KEY_K,
        Key.KEY_L to OmniKeyboard.KEY_L,
        Key.KEY_M to OmniKeyboard.KEY_M,
        Key.KEY_N to OmniKeyboard.KEY_N,
        Key.KEY_O to OmniKeyboard.KEY_O,
        Key.KEY_P to OmniKeyboard.KEY_P,
        Key.KEY_Q to OmniKeyboard.KEY_Q,
        Key.KEY_R to OmniKeyboard.KEY_R,
        Key.KEY_S to OmniKeyboard.KEY_S,
        Key.KEY_T to OmniKeyboard.KEY_T,
        Key.KEY_U to OmniKeyboard.KEY_U,
        Key.KEY_V to OmniKeyboard.KEY_V,
        Key.KEY_W to OmniKeyboard.KEY_W,
        Key.KEY_X to OmniKeyboard.KEY_X,
        Key.KEY_Y to OmniKeyboard.KEY_Y,
        Key.KEY_Z to OmniKeyboard.KEY_Z,
        Key.KEY_0 to OmniKeyboard.KEY_0,
        Key.KEY_1 to OmniKeyboard.KEY_1,
        Key.KEY_2 to OmniKeyboard.KEY_2,
        Key.KEY_3 to OmniKeyboard.KEY_3,
        Key.KEY_4 to OmniKeyboard.KEY_4,
        Key.KEY_5 to OmniKeyboard.KEY_5,
        Key.KEY_6 to OmniKeyboard.KEY_6,
        Key.KEY_7 to OmniKeyboard.KEY_7,
        Key.KEY_8 to OmniKeyboard.KEY_8,
        Key.KEY_9 to OmniKeyboard.KEY_9,
        Key.KEY_F1 to OmniKeyboard.KEY_F1,
        Key.KEY_F2 to OmniKeyboard.KEY_F2,
        Key.KEY_F3 to OmniKeyboard.KEY_F3,
        Key.KEY_F4 to OmniKeyboard.KEY_F4,
        Key.KEY_F5 to OmniKeyboard.KEY_F5,
        Key.KEY_F6 to OmniKeyboard.KEY_F6,
        Key.KEY_F7 to OmniKeyboard.KEY_F7,
        Key.KEY_F8 to OmniKeyboard.KEY_F8,
        Key.KEY_F9 to OmniKeyboard.KEY_F9,
        Key.KEY_F10 to OmniKeyboard.KEY_F10,
        Key.KEY_F11 to OmniKeyboard.KEY_F11,
        Key.KEY_F12 to OmniKeyboard.KEY_F12,
        Key.KEY_F13 to OmniKeyboard.KEY_F13,
        Key.KEY_F14 to OmniKeyboard.KEY_F14,
        Key.KEY_F15 to OmniKeyboard.KEY_F15,
        Key.KEY_F16 to OmniKeyboard.KEY_F16,
        Key.KEY_F17 to OmniKeyboard.KEY_F17,
        Key.KEY_F18 to OmniKeyboard.KEY_F18,
        Key.KEY_F19 to OmniKeyboard.KEY_F19,
        Key.KEY_DELETE to OmniKeyboard.KEY_DELETE,
        Key.KEY_HOME to OmniKeyboard.KEY_HOME,
        Key.KEY_END to OmniKeyboard.KEY_END,
    )

    override fun isMouseButtonDown(button: MouseButton): Boolean {
        return OmniMouse.isPressed(mouseMappings.getValue(button))
    }

    override fun isKeyDown(key: Key): Boolean {
        return OmniKeyboard.isPressed(keyMappings.getValue(key))
    }

    public fun getMouseButtonMapping(code: Int): MouseButton {
        return mouseMappings.entries.firstOrNull { entry ->
            entry.value == code
        }?.key ?: throw IllegalArgumentException("Unknown mouse button code: $code")
    }

    public fun getKeyMapping(keyCode: Int, typedChar: Char): Key {
        return keyMappings.entries.firstOrNull { entry ->
            entry.value == keyCode
        }?.key ?: Key.fromChar(typedChar) ?: Key.KEY_UNKNOWN
    }

}
