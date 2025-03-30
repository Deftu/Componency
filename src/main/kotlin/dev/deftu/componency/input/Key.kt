package dev.deftu.componency.input

public enum class Key(
    public val char: Char = ' ',
    public val shiftedChar: Char = char.uppercaseChar(),
) {
    KEY_UNKNOWN,

    // Modifiers / control keys
    KEY_ESCAPE,
    KEY_LMETA,
    KEY_RMETA,
    KEY_LCONTROL,
    KEY_RCONTROL,
    KEY_LSHIFT,
    KEY_RSHIFT,
    KEY_LMENU,
    KEY_RMENU,
    KEY_TAB,
    KEY_BACKSPACE,
    KEY_ENTER,

    // Letters
    KEY_A('a'),
    KEY_B('b'),
    KEY_C('c'),
    KEY_D('d'),
    KEY_E('e'),
    KEY_F('f'),
    KEY_G('g'),
    KEY_H('h'),
    KEY_I('i'),
    KEY_J('j'),
    KEY_K('k'),
    KEY_L('l'),
    KEY_M('m'),
    KEY_N('n'),
    KEY_O('o'),
    KEY_P('p'),
    KEY_Q('q'),
    KEY_R('r'),
    KEY_S('s'),
    KEY_T('t'),
    KEY_U('u'),
    KEY_V('v'),
    KEY_W('w'),
    KEY_X('x'),
    KEY_Y('y'),
    KEY_Z('z'),

    // Numbers
    KEY_0('0', ')'),
    KEY_1('1', '!'),
    KEY_2('2', '@'),
    KEY_3('3', '#'),
    KEY_4('4', '$'),
    KEY_5('5', '%'),
    KEY_6('6', '^'),
    KEY_7('7', '&'),
    KEY_8('8', '*'),
    KEY_9('9', '('),

    // Symbols
    KEY_GRAVE('`', '~'),
    KEY_MINUS('-', '_'),
    KEY_EQUALS('=', '+'),
    KEY_LBRACKET('[', '{'),
    KEY_RBRACKET(']', '}'),
    KEY_BACKSLASH('\\', '|'),
    KEY_SEMICOLON(';', ':'),
    KEY_APOSTROPHE('\'', '"'),
    KEY_COMMA(',', '<'),
    KEY_PERIOD('.', '>'),
    KEY_SLASH('/', '?'),
    KEY_SPACE(' '),

    // Arrows
    KEY_LEFT,
    KEY_UP,
    KEY_RIGHT,
    KEY_DOWN,

    // Locks
    KEY_CAPS_LOCK,
    KEY_NUM_LOCK,
    KEY_SCROLL_LOCK,

    // Special keys
    KEY_INSERT,
    KEY_DELETE,
    KEY_HOME,
    KEY_END,
    KEY_PAGE_UP,
    KEY_PAGE_DOWN,
    KEY_PRINT_SCREEN,
    KEY_PAUSE_BREAK,

    // Function keys
    KEY_F1,
    KEY_F2,
    KEY_F3,
    KEY_F4,
    KEY_F5,
    KEY_F6,
    KEY_F7,
    KEY_F8,
    KEY_F9,
    KEY_F10,
    KEY_F11,
    KEY_F12,
    KEY_F13,
    KEY_F14,
    KEY_F15,
    KEY_F16,
    KEY_F17,
    KEY_F18,
    KEY_F19,

    // Numpad
    KEY_NUMPAD_DIVIDE('/'),
    KEY_NUMPAD_MULTIPLY('*'),
    KEY_NUMPAD_ADD('+'),
    KEY_NUMPAD_SUBTRACT('-'),
    KEY_NUMPAD_DECIMAL('.'),
    KEY_NUMPAD0,
    KEY_NUMPAD1,
    KEY_NUMPAD2,
    KEY_NUMPAD3,
    KEY_NUMPAD4,
    KEY_NUMPAD5,
    KEY_NUMPAD6,
    KEY_NUMPAD7,
    KEY_NUMPAD8,
    KEY_NUMPAD9;

    public val isTextual: Boolean
        get() = when (this) {
            KEY_SPACE -> true
            else  -> !this.char.isISOControl() && !this.char.isWhitespace()
        }

    public val isLetter: Boolean
        get() = this.char.isLetter() || this.shiftedChar.isLetter()

    public companion object {

        public fun fromChar(char: Char): Key? {
            return values().firstOrNull { key ->
                key.char == char
            }
        }

    }

}
