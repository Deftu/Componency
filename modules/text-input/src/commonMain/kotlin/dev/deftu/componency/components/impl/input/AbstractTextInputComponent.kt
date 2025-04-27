package dev.deftu.componency.components.impl.input

import dev.deftu.componency.animations.Easings
import dev.deftu.componency.color.Color
import dev.deftu.componency.components.Component
import dev.deftu.componency.components.events.KeyboardModifiers
import dev.deftu.componency.components.impl.RectangleComponent
import dev.deftu.componency.dsl.*
import dev.deftu.componency.effects.impl.ScissorEffect
import dev.deftu.componency.engine.Engine
import dev.deftu.componency.input.Key
import dev.deftu.componency.input.MouseButton
import dev.deftu.stateful.State
import dev.deftu.textile.SimpleTextHolder
import dev.deftu.textile.TextHolder
import java.util.function.Consumer

public abstract class AbstractTextInputComponent(
    public val placeholder: TextHolder<*, *>,
    enabledState: State<Boolean>,
    maxLengthState: State<Int>
) : Component() {

    public companion object {

        @JvmField
        public val DEFAULT_SELECTION_FOREGROUND_COLOR: Color = noInline { Color.BLUE }

        @JvmField
        public val DEFAULT_INACTIVE_SELECTION_FOREGROUND_COLOR: Color = noInline { Color.BLUE }

        @JvmField
        public val DEFAULT_SELECTION_BACKGROUND_COLOR: Color = noInline { Color.WHITE }

        @JvmField
        public val DEFAULT_INACTIVE_SELECTION_BACKGROUND_COLOR: Color = noInline { Color.WHITE }

        @JvmStatic
        public fun isAllowedCharacter(char: Char): Boolean {
            return char.code != 167 // Section symbol
                    && char >= ' '
                    && char.code != 127 // Delete
        }

        private fun <T> noInline(block: () -> T): T = block()

    }

    internal val submitListeners = mutableListOf<(String) -> Unit>()

    @JvmField
    protected var isActive: Boolean = false

    protected val lines: List<String>
        get() = getText0().lines()

    protected var lineHeight: Float = 0f
        set(value) {
            this.height = value
            field = value
        }

    // Default to a bright blue, it's the sanest possible choice we have here as it's the most commonly used color for this in modern browsers.
    public var selectionForegroundColor: Color = DEFAULT_SELECTION_FOREGROUND_COLOR
    public var inactiveSelectionForegroundColor: Color = DEFAULT_INACTIVE_SELECTION_FOREGROUND_COLOR
    private val currentSelectionForegroundColor: Color
        get() = if (isActive) selectionForegroundColor else inactiveSelectionForegroundColor

    public var selectionBackgroundColor: Color = DEFAULT_SELECTION_BACKGROUND_COLOR
    public var inactiveSelectionBackgroundColor: Color = DEFAULT_INACTIVE_SELECTION_BACKGROUND_COLOR
    private val currentSelectionBackgroundColor: Color
        get() = if (isActive) selectionBackgroundColor else inactiveSelectionBackgroundColor

    public var cursorColor: Color = Color.WHITE

    // Selections
    protected var isSelectionActive: Boolean = false
        private set
    protected var currentSelectionMode: SelectionMode = SelectionMode.NONE
    public var selectionStart: InputPosition? = null
    public var selectionEnd: InputPosition? = null
    protected val selectionInputScope: InputScope
        get() = if (isSelectionActive) InputScope.SELECTION else InputScope.ALL

    // Scrolling
    protected var targetVerticalScrollOffset: Float = 0f
    protected var verticalScrollOffset: Float = 0f

    // Operations
    private val redoStack = ArrayDeque<TextOperation>()
    private val undoStack = ArrayDeque<TextOperation>()

    // Cursor
    protected var cursor: Component = RectangleComponent().configure {
        properties {
            y = centered
            width = 1.px
            height = 100.percent
            fill = Color.TRANSPARENT.asProperty
        }
    }.attachTo(this)
    public var cursorPosition: InputPosition = InputPosition(0, 0)
        set(value) {
            val font = this.config.properties.font
                ?: throw IllegalStateException("Cannot render text without a font")
            val fontSize = this.config.properties.fontSize.getFontSize(this)

            field = value
            if (value == InputPosition.START) {
                cursor.configure {
                    properties {
                        x = 0.px
                    }
                }

                return
            }

            val lineText = lines[value.line]
            val textBeforeCursor = if (value.column <= lineText.length) {
                lineText.substring(0, value.column)
            } else {
                lineText
            }

            val (width, _) = Engine.get(this).renderEngine.textSize(font, SimpleTextHolder(textBeforeCursor), fontSize)
            cursor.configure {
                properties {
                    x = width.px
                }
            }
        }

    init {
        whenKeyPress {
            if (!isActive) {
                return@whenKeyPress
            }

            if (modifiers.isCtrl) {
                when (key) {
                    Key.KEY_A -> {
                        selectAll()
                        return@whenKeyPress
                    }

                    Key.KEY_C -> {
                        if (isSelectionActive) {
                            copySelection()
                        } else {
                            copyAll()
                        }

                        return@whenKeyPress
                    }

                    Key.KEY_X -> {
                        if (isSelectionActive) {
                            cutSelection()
                        } else {
                            cutAll()
                        }

                        return@whenKeyPress
                    }

                    Key.KEY_V -> {
                        pasteOrReplace()
                        return@whenKeyPress
                    }

                    Key.KEY_Y -> {
                        redo()
                        return@whenKeyPress
                    }

                    Key.KEY_Z -> {
                        if (modifiers.isShift) {
                            redo()
                        } else {
                            undo()
                        }

                        return@whenKeyPress
                    }

                    else -> {} // no-op
                }
            }

            when (key) {
                Key.KEY_LEFT -> {
                    if (cursorPosition == InputPosition.START) {
                        return@whenKeyPress
                    }

                    if (modifiers.isShift) {
                        if (!isSelectionActive) {
                            selectionStart = cursorPosition
                            isSelectionActive = true
                        }

                        cursorPosition = if (modifiers.isCtrl) getNextSkipPosition(cursorPosition, Direction.LEFT) else cursorPosition.withColumnOffset(-1)
                        selectionEnd = cursorPosition
                        return@whenKeyPress
                    }

                    if (isSelectionActive) {
                        cursorPosition = selectionStart!!
                        selectionStart = null
                        selectionEnd = null
                        isSelectionActive = false
                        return@whenKeyPress
                    }

                    cursorPosition = cursorPosition.withColumnOffset(-1)
                }

                Key.KEY_RIGHT -> {
                    if (cursorPosition == InputPosition.endOf(lines)) {
                        return@whenKeyPress
                    }

                    if (modifiers.isShift) {
                        if (!isSelectionActive) {
                            selectionStart = cursorPosition
                            isSelectionActive = true
                        }

                        cursorPosition = if (modifiers.isCtrl) getNextSkipPosition(cursorPosition, Direction.RIGHT) else cursorPosition.withColumnOffset(1)
                        selectionEnd = cursorPosition
                        return@whenKeyPress
                    }

                    if (isSelectionActive) {
                        cursorPosition = selectionEnd!!
                        selectionStart = null
                        selectionEnd = null
                        isSelectionActive = false
                        return@whenKeyPress
                    }

                    cursorPosition = cursorPosition.withColumnOffset(1)
                }

                Key.KEY_UP -> {
                    if (modifiers.isShift) {
                        if (!isSelectionActive) {
                            selectionStart = cursorPosition
                            isSelectionActive = true
                        }

                        cursorPosition = if (cursorPosition.line == 0) InputPosition.START else cursorPosition.withLineOffset(-1)
                        selectionEnd = cursorPosition
                        return@whenKeyPress
                    }

                    if (isSelectionActive) {
                        cursorPosition = selectionStart!!
                        selectionStart = null
                        selectionEnd = null
                        isSelectionActive = false
                        return@whenKeyPress
                    }

                    cursorPosition = if (cursorPosition.line == 0) InputPosition.START else cursorPosition.withLineOffset(-1)
                }

                Key.KEY_DOWN -> {
                    if (modifiers.isShift) {
                        if (!isSelectionActive) {
                            selectionStart = cursorPosition
                            isSelectionActive = true
                        }

                        cursorPosition = if (lines.size == 1 || cursorPosition == InputPosition.endOf(lines)) InputPosition.endOf(lines) else cursorPosition.withLineOffset(1)
                        selectionEnd = cursorPosition
                        return@whenKeyPress
                    }

                    if (isSelectionActive) {
                        cursorPosition = selectionEnd!!
                        selectionStart = null
                        selectionEnd = null
                        isSelectionActive = false
                        return@whenKeyPress
                    }

                    cursorPosition = if (lines.size == 1 || cursorPosition == InputPosition.endOf(lines)) InputPosition.endOf(lines) else cursorPosition.withLineOffset(1)
                }

                Key.KEY_BACKSPACE -> {
                    if (!isSelectionActive) {
                        val startPos = cursorPosition.withColumnOffset(-1)
                        if (cursorPosition == InputPosition.START) {
                            return@whenKeyPress
                        }

                        commitRemove(startPos, cursorPosition, false)
                    } else {
                        deleteSelection()
                    }
                }

                Key.KEY_DELETE -> {
                    if (!isSelectionActive) {
                        val endPos = cursorPosition.withColumnOffset(1)
                        if (cursorPosition == InputPosition.endOf(lines)) {
                            return@whenKeyPress
                        }

                        commitRemove(cursorPosition, endPos, false)
                    } else {
                        deleteSelection()
                    }
                }

                Key.KEY_ENTER -> {
                    handleSubmit(getText0(), modifiers)
                }

                Key.KEY_HOME -> {
                    if (modifiers.isShift) {
                        if (!isSelectionActive) {
                            selectionStart = cursorPosition
                            isSelectionActive = true
                        }

                        cursorPosition = InputPosition(cursorPosition.line, 0)
                        selectionEnd = cursorPosition
                        return@whenKeyPress
                    }

                    if (isSelectionActive) {
                        cursorPosition = selectionStart!!
                        selectionStart = null
                        selectionEnd = null
                        isSelectionActive = false
                        return@whenKeyPress
                    }

                    cursorPosition = InputPosition(cursorPosition.line, 0)
                }

                Key.KEY_END -> {
                    if (modifiers.isShift) {
                        if (!isSelectionActive) {
                            selectionStart = cursorPosition
                            isSelectionActive = true
                        }

                        cursorPosition = InputPosition(cursorPosition.line, lines[cursorPosition.line].length)
                        selectionEnd = cursorPosition
                        return@whenKeyPress
                    }

                    if (isSelectionActive) {
                        cursorPosition = selectionEnd!!
                        selectionStart = null
                        selectionEnd = null
                        isSelectionActive = false
                        return@whenKeyPress
                    }

                    cursorPosition = InputPosition(cursorPosition.line, lines[cursorPosition.line].length)
                }

                else -> {} // no-op
            }
        }.whenCharType {
            if (isAllowedCharacter(char)) {
                commitAppend(char.toString())
            }
        }.whenMouseScroll {
            val heightDiff = height - lines.size * lineHeight
            println("Height diff: $heightDiff")
            if (heightDiff > 0) {
                return@whenMouseScroll
            }

            targetVerticalScrollOffset = (verticalScrollOffset + delta.toFloat() * lineHeight).coerceIn(0f, heightDiff)
            cancel()
        }.whenMouseClick {
            println("Mouse click: $x, $y")
            if (!isActive || button != MouseButton.LEFT) {
                return@whenMouseClick
            }

            TODO()
        }.whenMouseDrag {
            if (button != MouseButton.LEFT) {
                return@whenMouseDrag
            }

            TODO()
        }.whenMouseRelease {
        }.whenFocus {
            println("Focus gained")
            setActive(true)
        }.whenUnfocus {
            println("Focus lost")
            setActive(false)
        }.effect(ScissorEffect())
    }

    override fun render() {
        val font = this.config.properties.font ?: throw IllegalStateException("Cannot render text without a font")
        val fill = this.config.properties.fill.getColor(this)
        val fontSize = this.config.properties.fontSize.getFontSize(this)
        val engine = Engine.get(this)

        if (lineHeight == 0f) {
            lineHeight = engine.renderEngine.textSize(font, SimpleTextHolder(" "), fontSize).second
        }

        // Render text
        val lines = lines
        if (lines.isEmpty() || lines.first().isEmpty()) {
            engine.renderEngine.text(
                x = this.left,
                y = this.top,
                text = placeholder,
                color = fill,
                font = font,
                fontSize = fontSize
            )
        } else {
            for (line in lines) {
                if (line.isEmpty()) {
                    continue
                }

                engine.renderEngine.text(
                    x = this.left,
                    y = this.top,
                    text = SimpleTextHolder(line),
                    color = fill,
                    font = font,
                    fontSize = fontSize
                )
            }
        }

        // Render selection
        if (isSelectionActive) {
            val (startPos, endPos) = InputPosition.sorted(selectionStart!!, selectionEnd!!)
            if (startPos.line != endPos.line) {
                val startLine = lines[startPos.line]
                val endLine = lines[endPos.line]

                val startText = startLine.substring(startPos.column)
                val endText = endLine.substring(0, endPos.column)

                val (startWidth, startHeight) = engine.renderEngine.textSize(font, SimpleTextHolder(startText), fontSize)
                val (endWidth, endHeight) = engine.renderEngine.textSize(font, SimpleTextHolder(endText), fontSize)

                val startX = this.left + startWidth
                val startY = this.top + startPos.line * startHeight
                val endX = this.left + endWidth
                val endY = this.top + endPos.line * endHeight

                engine.renderEngine.fill(
                    x1 = startX,
                    y1 = startY,
                    x2 = this.right,
                    y2 = startY + startHeight,
                    color = currentSelectionBackgroundColor
                )

                engine.renderEngine.fill(
                    x1 = this.left,
                    y1 = endY,
                    x2 = endX,
                    y2 = endY + endHeight,
                    color = currentSelectionBackgroundColor
                )
            } else {
                val startLine = lines[startPos.line]
                val selectedText = getBetween(startPos, endPos)
                val (textWidth, textHeight) = engine.renderEngine.textSize(font, SimpleTextHolder(selectedText), fontSize)

                val beforeSelection = startLine.substring(0, startPos.column)
                val (beforeWidth, _) = engine.renderEngine.textSize(font, SimpleTextHolder(beforeSelection), fontSize)

                val startX = this.left + beforeWidth
                val startY = this.top + startPos.line * textHeight
                val endX = startX + textWidth
                val endY = this.top + endPos.line * textHeight

                engine.renderEngine.fill(
                    x1 = startX,
                    y1 = startY,
                    x2 = endX,
                    y2 = endY + textHeight,
                    color = currentSelectionBackgroundColor
                )

                engine.renderEngine.text(
                    x = startX,
                    y = startY,
                    text = SimpleTextHolder(selectedText),
                    color = currentSelectionForegroundColor,
                    font = font,
                    fontSize = fontSize
                )
            }
        }
    }

    public fun commit(operation: TextOperation) {
        operation.redo()
        undoStack.add(operation)
        redoStack.clear()
    }

    protected fun commitAppend(text: String) {
        val appendOperation = AppendTextOperation(this, text, cursorPosition)
        if (isSelectionActive) {
            val (startPos, endPos) = InputPosition.sorted(selectionStart!!, selectionEnd!!)
            val removeOperation = RemoveTextOperation(this, startPos, endPos, true)
            val replaceOperation = ReplaceTextOperation(this, appendOperation, removeOperation)
            commit(replaceOperation)
            return
        }

        commit(appendOperation)
    }

    protected fun commitRemove(start: InputPosition, end: InputPosition, isSelectingAfterUndo: Boolean) {
        val (startPos, endPos) = InputPosition.sorted(start, end)
        val removeOperation = RemoveTextOperation(this, startPos, endPos, isSelectingAfterUndo)
        commit(removeOperation)

        selectionStart = null
        selectionEnd = null
        isSelectionActive = false
    }

    public open fun selectAll() {
        val end = InputPosition.endOf(lines)
        selectionStart = InputPosition.START
        selectionEnd = end
        cursorPosition = end
        isSelectionActive = true
    }

    public open fun copyAll() {
        Engine.get(this).inputEngine.clipboard = getText0()
    }

    public open fun copySelection() {
        if (!isSelectionActive) {
            return
        }

        val startPos = selectionStart ?: return
        val endPos = selectionEnd ?: return
        val text = getBetween(startPos, endPos)
        Engine.get(this).inputEngine.clipboard = text
    }

    public open fun cutAll() {
        Engine.get(this).inputEngine.clipboard = getText0()
        commitRemove(InputPosition.START, InputPosition.endOf(lines), false)
    }

    public open fun cutSelection() {
        if (!isSelectionActive) {
            return
        }

        val startPos = selectionStart ?: return
        val endPos = selectionEnd ?: return
        Engine.get(this).inputEngine.clipboard = getBetween(startPos, endPos)
        commitRemove(startPos, endPos, false)
    }

    public open fun pasteOrReplace() {
        val clipboard = Engine.get(this).inputEngine.clipboard ?: return
        if (clipboard.isEmpty()) {
            return
        }

        if (isSelectionActive) {
            commitRemove(selectionStart!!, selectionEnd!!, false)
        }

        commitAppend(clipboard)
    }

    public open fun undo() {
        if (undoStack.isEmpty()) {
            return
        }

        val operation = undoStack.removeLast()
        operation.undo()
        redoStack.add(operation)
    }

    public open fun redo() {
        if (redoStack.isEmpty()) {
            return
        }

        val operation = redoStack.removeLast()
        operation.redo()
        undoStack.add(operation)
    }

    public open fun deleteAll() {
        commitRemove(InputPosition.START, InputPosition.endOf(lines), false)
    }

    public open fun deleteSelection() {
        if (!isSelectionActive) {
            return
        }

        commitRemove(selectionStart!!, selectionEnd!!, false)
    }

    public open fun deleteBetween(start: InputPosition, end: InputPosition) {
        commitRemove(start, end, false)
    }

    public open fun handleSubmit(text: String, modifiers: KeyboardModifiers) {
        submitListeners.forEach { it(text) }
    }

    public abstract fun createLines(text: String): List<String>

    public abstract fun getText0(): String

    protected abstract fun setText0(text: String)

    public fun setText(text: String) {
        val replaceOperation = ReplaceTextOperation(
            this,
            AppendTextOperation(this, text, InputPosition.START),
            RemoveTextOperation(this, InputPosition.START, InputPosition.endOf(lines), true)
        )

        commit(replaceOperation)
    }

    public fun addText(text: String, position: InputPosition) {
        val textLinesines = createLines(text)
        when {
            textLinesines.isEmpty() -> return

            textLinesines.size == 1 -> {
                val line = lines[position.line]
                val newLine = line.substring(0, position.column) + text + line.substring(position.column)
                val newLines = lines.toMutableList()
                newLines[position.line] = newLine
                setText0(newLines.joinToString("\n"))
            }

            else -> {
                val newLines = lines.toMutableList()
                val firstLine = newLines[position.line]
                val lastLine = newLines[position.line + textLinesines.size - 1]

                newLines[position.line] = firstLine.substring(0, position.column) + textLinesines.first()
                newLines[position.line + textLinesines.size - 1] = textLinesines.last() + lastLine.substring(position.column)
                newLines.addAll(position.line + 1, textLinesines.subList(1, textLinesines.size - 1))

                setText0(newLines.joinToString("\n"))
            }
        }

        cursorPosition = position.withColumnOffset(text.length)
    }

    public fun removeText(startPosition: InputPosition, endPosition: InputPosition) {
        val newLines = lines.toMutableList()
        val startLine = newLines[startPosition.line]
        val endLine = newLines[endPosition.line]

        when {
            startPosition.line == endPosition.line -> {
                val newLine = startLine.substring(0, startPosition.column) + endLine.substring(endPosition.column)
                newLines[startPosition.line] = newLine
            }

            else -> {
                val firstLine = startLine.substring(0, startPosition.column) + endLine.substring(endPosition.column)
                newLines[startPosition.line] = firstLine
                newLines.subList(startPosition.line + 1, endPosition.line + 1).clear()
            }
        }

        setText0(newLines.joinToString("\n"))
        cursorPosition = startPosition
    }

    public fun getBetween(start: InputPosition, end: InputPosition): String {
        val (startPos, endPos) = InputPosition.sorted(start, end)
        if (startPos == endPos) {
            return ""
        }

        val startLine = lines[startPos.line]
        val endLine = lines[endPos.line]

        return when {
            startPos.line == endPos.line -> startLine.substring(startPos.column, endPos.column)
            else -> {
                val lines = mutableListOf<String>()
                lines.add(startLine.substring(startPos.column))
                lines.addAll(lines.subList(startPos.line + 1, endPos.line))
                lines.add(endLine.substring(0, endPos.column))
                lines.joinToString("\n")
            }
        }
    }

    public fun <T : AbstractTextInputComponent> onSubmit(listener: Consumer<String>): T = apply {
        submitListeners.add(listener::accept)
    } as T

    public fun getNextSkipPosition(position: InputPosition, direction: Direction): InputPosition {
        if (position == InputPosition.START || position == InputPosition.endOf(lines)) {
            return position
        }

        return when (direction) {
            Direction.LEFT -> skipLeft(position)
            Direction.RIGHT -> skipRight(position)
        }
    }

    private fun skipLeft(position: InputPosition): InputPosition {
        var lineIndex = position.line
        var colIndex = position.column

        while (lineIndex >= 0) {
            // If at the very start of the document, return the START position.
            if (lineIndex == 0 && colIndex == 0) {
                return InputPosition.START
            }

            // If at the beginning of a line, move to the end of the previous line.
            if (colIndex == 0) {
                lineIndex--
                if (lineIndex >= 0) {
                    colIndex = lines[lineIndex].length
                } else {
                    return InputPosition.START
                }
                continue
            }

            // First, skip any whitespace going backward.
            while (colIndex > 0 && lines[lineIndex][colIndex - 1].isWhitespace()) {
                colIndex--
            }
            // Then, skip backwards over the word.
            while (colIndex > 0 && !lines[lineIndex][colIndex - 1].isWhitespace()) {
                colIndex--
            }
            return InputPosition(lineIndex, colIndex)
        }
        return InputPosition.START
    }

    private fun skipRight(position: InputPosition): InputPosition {
        var lineIndex = position.line
        var colIndex = position.column

        while (lineIndex < lines.size) {
            val currentLine = lines[lineIndex]
            val lineLength = currentLine.length

            // If we're beyond the current line, jump to the next line.
            if (colIndex >= lineLength) {
                lineIndex++
                colIndex = 0
                continue
            }

            // If current char is non-whitespace, skip to the end of the word.
            if (!currentLine[colIndex].isWhitespace()) {
                while (colIndex < lineLength && !currentLine[colIndex].isWhitespace()) {
                    colIndex++
                }
            }
            // Now skip any consecutive whitespace.
            while (colIndex < lineLength && currentLine[colIndex].isWhitespace()) {
                colIndex++
            }

            // If we have landed within the current line, return the position.
            if (colIndex < lineLength) {
                return InputPosition(lineIndex, colIndex)
            } else {
                // Otherwise, move to the next line and reset the column.
                lineIndex++
                colIndex = 0
            }
        }
        // If we run out of lines, return the end position.
        return InputPosition.endOf(lines)
    }

    public fun activate() {
        requestFocus()
    }

    public fun charAt(position: InputPosition): Char {
        return lines[position.line][position.column]
    }

    public fun charBefore(position: InputPosition): Char? {
        if (position == InputPosition.START) {
            return null
        }

        return charAt(position.withColumnOffset(-1))
    }

    public fun charAfter(position: InputPosition): Char? {
        if (position == InputPosition.endOf(lines)) {
            return null
        }

        return charAt(position.withColumnOffset(1))
    }

    protected fun setActive(isActive: Boolean) {
        this.isActive = isActive

        if (isActive) {
            cursor.show()
            beginCursorBlink()
        } else {
            cursor.configure {
                properties {
                    fill = Color.WHITE.asProperty
                }
            }
        }
    }

    protected fun beginCursorBlink() {
        if (!isActive) {
            return
        }

        cursor.animate {
            animateFill(Easings.OUT_CIRC, 500.millis, Color.WHITE.asProperty)

            whenComplete {
                cursor.animate {
                    animateFill(Easings.OUT_CIRC, 500.millis, Color.TRANSPARENT.asProperty)

                    whenComplete {
                        if (isActive) {
                            beginCursorBlink()
                        }
                    }
                }
            }
        }
    }

    public enum class Direction {
        LEFT,
        RIGHT
    }

}
