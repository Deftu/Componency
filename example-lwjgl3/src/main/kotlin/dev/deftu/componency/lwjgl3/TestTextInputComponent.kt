package dev.deftu.componency.lwjgl3

import dev.deftu.componency.components.impl.input.AbstractTextInputComponent
import dev.deftu.stateful.utils.stateOf
import dev.deftu.textile.SimpleTextHolder

class TestTextInputComponent : AbstractTextInputComponent(
    SimpleTextHolder("Hello, user!"),
    stateOf(true),
    stateOf(32)
) {
    private var value: String = ""

    override fun createLines(text: String): List<String> {
        return listOf(text.replace('\n', ' '))
    }

    override fun getText0(): String {
        return value
    }

    override fun setText0(text: String) {
        value = text
    }
}
