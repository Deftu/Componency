package dev.deftu.componency.android.platform.input

import android.content.Context
import android.view.KeyEvent
import android.view.View
import dev.deftu.componency.components.events.KeyboardModifiers
import dev.deftu.componency.input.Key
import dev.deftu.componency.platform.input.KeyboardInput
import java.util.concurrent.ConcurrentHashMap

public class AndroidKeyboardInput(
    private val view: View,
    private val context: Context
) : KeyboardInput {

    private companion object {

        private val keyMappings = mapOf(
            // Modifiers / control keys
            Key.KEY_ESCAPE      to KeyEvent.KEYCODE_ESCAPE,
            Key.KEY_LMETA       to KeyEvent.KEYCODE_META_LEFT,
            Key.KEY_RMETA       to KeyEvent.KEYCODE_META_RIGHT,
            Key.KEY_LCONTROL    to KeyEvent.KEYCODE_CTRL_LEFT,
            Key.KEY_RCONTROL    to KeyEvent.KEYCODE_CTRL_RIGHT,
            Key.KEY_LSHIFT      to KeyEvent.KEYCODE_SHIFT_LEFT,
            Key.KEY_RSHIFT      to KeyEvent.KEYCODE_SHIFT_RIGHT,
            Key.KEY_LMENU       to KeyEvent.KEYCODE_ALT_LEFT,
            Key.KEY_RMENU       to KeyEvent.KEYCODE_ALT_RIGHT,
            Key.KEY_TAB         to KeyEvent.KEYCODE_TAB,
            Key.KEY_BACKSPACE   to KeyEvent.KEYCODE_DEL,
            Key.KEY_ENTER       to KeyEvent.KEYCODE_ENTER,

            // Letters
            Key.KEY_A           to KeyEvent.KEYCODE_A,
            Key.KEY_B           to KeyEvent.KEYCODE_B,
            Key.KEY_C           to KeyEvent.KEYCODE_C,
            Key.KEY_D           to KeyEvent.KEYCODE_D,
            Key.KEY_E           to KeyEvent.KEYCODE_E,
            Key.KEY_F           to KeyEvent.KEYCODE_F,
            Key.KEY_G           to KeyEvent.KEYCODE_G,
            Key.KEY_H           to KeyEvent.KEYCODE_H,
            Key.KEY_I           to KeyEvent.KEYCODE_I,
            Key.KEY_J           to KeyEvent.KEYCODE_J,
            Key.KEY_K           to KeyEvent.KEYCODE_K,
            Key.KEY_L           to KeyEvent.KEYCODE_L,
            Key.KEY_M           to KeyEvent.KEYCODE_M,
            Key.KEY_N           to KeyEvent.KEYCODE_N,
            Key.KEY_O           to KeyEvent.KEYCODE_O,
            Key.KEY_P           to KeyEvent.KEYCODE_P,
            Key.KEY_Q           to KeyEvent.KEYCODE_Q,
            Key.KEY_R           to KeyEvent.KEYCODE_R,
            Key.KEY_S           to KeyEvent.KEYCODE_S,
            Key.KEY_T           to KeyEvent.KEYCODE_T,
            Key.KEY_U           to KeyEvent.KEYCODE_U,
            Key.KEY_V           to KeyEvent.KEYCODE_V,
            Key.KEY_W           to KeyEvent.KEYCODE_W,
            Key.KEY_X           to KeyEvent.KEYCODE_X,
            Key.KEY_Y           to KeyEvent.KEYCODE_Y,
            Key.KEY_Z           to KeyEvent.KEYCODE_Z,

            // Numbers
            Key.KEY_0           to KeyEvent.KEYCODE_0,
            Key.KEY_1           to KeyEvent.KEYCODE_1,
            Key.KEY_2           to KeyEvent.KEYCODE_2,
            Key.KEY_3           to KeyEvent.KEYCODE_3,
            Key.KEY_4           to KeyEvent.KEYCODE_4,
            Key.KEY_5           to KeyEvent.KEYCODE_5,
            Key.KEY_6           to KeyEvent.KEYCODE_6,
            Key.KEY_7           to KeyEvent.KEYCODE_7,
            Key.KEY_8           to KeyEvent.KEYCODE_8,
            Key.KEY_9           to KeyEvent.KEYCODE_9,

            // Symbols
            Key.KEY_GRAVE       to KeyEvent.KEYCODE_GRAVE,
            Key.KEY_MINUS       to KeyEvent.KEYCODE_MINUS,
            Key.KEY_EQUALS      to KeyEvent.KEYCODE_EQUALS,
            Key.KEY_LBRACKET    to KeyEvent.KEYCODE_LEFT_BRACKET,
            Key.KEY_RBRACKET    to KeyEvent.KEYCODE_RIGHT_BRACKET,
            Key.KEY_BACKSLASH   to KeyEvent.KEYCODE_BACKSLASH,
            Key.KEY_SEMICOLON   to KeyEvent.KEYCODE_SEMICOLON,
            Key.KEY_APOSTROPHE  to KeyEvent.KEYCODE_APOSTROPHE,
            Key.KEY_COMMA       to KeyEvent.KEYCODE_COMMA,
            Key.KEY_PERIOD      to KeyEvent.KEYCODE_PERIOD,
            Key.KEY_SLASH       to KeyEvent.KEYCODE_SLASH,
            Key.KEY_SPACE       to KeyEvent.KEYCODE_SPACE,

            // Arrows
            Key.KEY_LEFT        to KeyEvent.KEYCODE_DPAD_LEFT,
            Key.KEY_UP          to KeyEvent.KEYCODE_DPAD_UP,
            Key.KEY_RIGHT       to KeyEvent.KEYCODE_DPAD_RIGHT,
            Key.KEY_DOWN        to KeyEvent.KEYCODE_DPAD_DOWN,

            // Locks
            Key.KEY_CAPS_LOCK   to KeyEvent.KEYCODE_CAPS_LOCK,
            Key.KEY_NUM_LOCK    to KeyEvent.KEYCODE_NUM_LOCK,
            Key.KEY_SCROLL_LOCK to KeyEvent.KEYCODE_SCROLL_LOCK,

            // Special keys
            Key.KEY_INSERT      to KeyEvent.KEYCODE_INSERT,
            Key.KEY_DELETE      to KeyEvent.KEYCODE_FORWARD_DEL,
            Key.KEY_HOME        to KeyEvent.KEYCODE_MOVE_HOME,
            Key.KEY_END         to KeyEvent.KEYCODE_MOVE_END,
            Key.KEY_PAGE_UP     to KeyEvent.KEYCODE_PAGE_UP,
            Key.KEY_PAGE_DOWN   to KeyEvent.KEYCODE_PAGE_DOWN,
            Key.KEY_PRINT_SCREEN to KeyEvent.KEYCODE_SYSRQ,
            Key.KEY_PAUSE_BREAK to KeyEvent.KEYCODE_BREAK,

            // Function keys
            Key.KEY_F1          to KeyEvent.KEYCODE_F1,
            Key.KEY_F2          to KeyEvent.KEYCODE_F2,
            Key.KEY_F3          to KeyEvent.KEYCODE_F3,
            Key.KEY_F4          to KeyEvent.KEYCODE_F4,
            Key.KEY_F5          to KeyEvent.KEYCODE_F5,
            Key.KEY_F6          to KeyEvent.KEYCODE_F6,
            Key.KEY_F7          to KeyEvent.KEYCODE_F7,
            Key.KEY_F8          to KeyEvent.KEYCODE_F8,
            Key.KEY_F9          to KeyEvent.KEYCODE_F9,
            Key.KEY_F10         to KeyEvent.KEYCODE_F10,
            Key.KEY_F11         to KeyEvent.KEYCODE_F11,
            Key.KEY_F12         to KeyEvent.KEYCODE_F12,

            // Numpad
            Key.KEY_NUMPAD_DIVIDE    to KeyEvent.KEYCODE_NUMPAD_DIVIDE,
            Key.KEY_NUMPAD_MULTIPLY  to KeyEvent.KEYCODE_NUMPAD_MULTIPLY,
            Key.KEY_NUMPAD_ADD       to KeyEvent.KEYCODE_NUMPAD_ADD,
            Key.KEY_NUMPAD_SUBTRACT  to KeyEvent.KEYCODE_NUMPAD_SUBTRACT,
            Key.KEY_NUMPAD_DECIMAL   to KeyEvent.KEYCODE_NUMPAD_DOT,
            Key.KEY_NUMPAD0          to KeyEvent.KEYCODE_NUMPAD_0,
            Key.KEY_NUMPAD1          to KeyEvent.KEYCODE_NUMPAD_1,
            Key.KEY_NUMPAD2          to KeyEvent.KEYCODE_NUMPAD_2,
            Key.KEY_NUMPAD3          to KeyEvent.KEYCODE_NUMPAD_3,
            Key.KEY_NUMPAD4          to KeyEvent.KEYCODE_NUMPAD_4,
            Key.KEY_NUMPAD5          to KeyEvent.KEYCODE_NUMPAD_5,
            Key.KEY_NUMPAD6          to KeyEvent.KEYCODE_NUMPAD_6,
            Key.KEY_NUMPAD7          to KeyEvent.KEYCODE_NUMPAD_7,
            Key.KEY_NUMPAD8          to KeyEvent.KEYCODE_NUMPAD_8,
            Key.KEY_NUMPAD9          to KeyEvent.KEYCODE_NUMPAD_9,
        )

    }

    // Internal pressed keys map
    private val keyStates = ConcurrentHashMap<Int, Boolean>()

    override val isShiftKeyDown: Boolean
        get() = keyStates[KeyEvent.KEYCODE_SHIFT_LEFT] == true ||
                keyStates[KeyEvent.KEYCODE_SHIFT_RIGHT] == true

    override val isCtrlKeyDown: Boolean
        get() = keyStates[KeyEvent.KEYCODE_CTRL_LEFT] == true ||
                keyStates[KeyEvent.KEYCODE_CTRL_RIGHT] == true

    override val isAltKeyDown: Boolean
        get() = keyStates[KeyEvent.KEYCODE_ALT_LEFT] == true ||
                keyStates[KeyEvent.KEYCODE_ALT_RIGHT] == true

    override val isSuperKeyDown: Boolean
        get() = keyStates[KeyEvent.KEYCODE_META_LEFT] == true ||
                keyStates[KeyEvent.KEYCODE_META_RIGHT] == true

    override val isCapsLockEnabled: Boolean
        get() = false // Android doesn't expose this easily

    override val isNumLockEnabled: Boolean
        get() = false // Android doesn't expose this either

    public val modifiers: KeyboardModifiers
        get() = KeyboardModifiers(
            isShift = isShiftKeyDown,
            isCtrl = isCtrlKeyDown,
            isAlt = isAltKeyDown,
            isSuper = isSuperKeyDown,
            isCapsLock = isCapsLockEnabled,
            isNumLock = isNumLockEnabled
        )

    public fun handleKeyEvent(event: KeyEvent) {
        val keyCode = event.keyCode
        val action = event.action

        if (action == KeyEvent.ACTION_DOWN) {
            keyStates[keyCode] = true
        } else if (action == KeyEvent.ACTION_UP) {
            keyStates[keyCode] = false
        }
    }

    public fun getKey(code: Int): Key {
        return keyMappings.entries.firstOrNull { (_, keyCode) ->
            keyCode == code
        }?.key ?: Key.KEY_UNKNOWN
    }

    override fun isKeyDown(key: Key): Boolean {
        return keyStates[keyMappings[key] ?: return false] == true
    }

    override fun setKeyDown(key: Key, isDown: Boolean) {
        val keyCode = keyMappings[key] ?: return
        keyStates[keyCode] = isDown
    }

}
