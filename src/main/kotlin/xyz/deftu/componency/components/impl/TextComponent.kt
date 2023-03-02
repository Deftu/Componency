package xyz.deftu.componency.components.impl

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.componency.dsl.px
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.state.SimpleState
import xyz.deftu.state.State
import xyz.deftu.text.Text

open class TextComponent constructor(
    valueState: State<Text>,
    shadowState: State<Boolean> = SimpleState(true),
) : BaseComponent() {
    constructor(
        value: Text,
        shadowState: State<Boolean> = SimpleState(true)
    ) : this(SimpleState(value), shadowState)
    constructor(
        valueState: State<Text>,
        shadow: Boolean = true
    ) : this(valueState, SimpleState(shadow))
    constructor(
        value: Text,
        shadow: Boolean = true
    ) : this(SimpleState(value), SimpleState(shadow))
    constructor(
        value: String,
        shadowState: State<Boolean> = SimpleState(true)
    ) : this(Text.create(value), shadowState)
    constructor(
        value: String,
        shadow: Boolean = true
    ) : this(Text.create(value), shadow)
    constructor(
        value: Text
    ) : this(value, true)
    constructor(
        value: String
    ) : this(value, true)

    private val valueState = valueState.map { it }
    private val shadowState = shadowState.map { it }

    val textWidth: Int
        get() = getFontProvider().getWidth(valueState.get(), shadowState.get()).toInt()
    val textHeight: Int
        get() = getFontProvider().getHeight(valueState.get(), shadowState.get()).toInt()

    init {
        setWidth(textWidth.px)
        setHeight(textHeight.px)
    }

    fun bindValue(value: State<Text>) = apply {
        valueState.rebind(value)
    }

    override fun render(
        stack: MultiMatrixStack,
        tickDelta: Float
    ) {
        getFontProvider().render(stack, valueState.get(), getX(), getY(), getWidth() / textWidth, shadowState.get(), getColor())
    }
}
