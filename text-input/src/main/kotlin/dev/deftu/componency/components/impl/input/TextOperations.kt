package dev.deftu.componency.components.impl.input

public interface TextOperation {

    public val component: AbstractTextInputComponent

    public fun undo()

    public fun redo()

}

public data class AppendTextOperation(
    override val component: AbstractTextInputComponent,
    public val text: String,
    public val position: InputPosition
) : TextOperation {

    public override fun undo() {
        component.removeText(position, position.withColumnOffset(text.length))
        component.cursorPosition = position
    }

    public override fun redo() {
        component.addText(text, position)
    }

}

public data class RemoveTextOperation(
    override val component: AbstractTextInputComponent,
    public val startPos: InputPosition,
    public val endPos: InputPosition,
    public val isSelectingAfterUndo: Boolean
) : TextOperation {

    private val removedText: String = component.getBetween(startPos, endPos)

    public override fun undo() {
        component.addText(removedText, startPos)
        if (isSelectingAfterUndo) {
            component.cursorPosition = startPos
            component.selectionEnd = endPos
        }
    }

    public override fun redo() {
        component.removeText(startPos, endPos)
        component.cursorPosition = startPos
    }

}

public data class ReplaceTextOperation(
    override val component: AbstractTextInputComponent,
    public val appendOperation: AppendTextOperation,
    public val removeOperation: RemoveTextOperation
) : TextOperation {

    public override fun undo() {
        removeOperation.undo()
        appendOperation.undo()
    }

    public override fun redo() {
        appendOperation.redo()
        removeOperation.redo()
    }

}
