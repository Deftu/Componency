package dev.deftu.componency.platform.input

public interface InputHandler {

    public val pointerInput: PointerInput

    public val keyboardInput: KeyboardInput

    public val clipboard: ClipboardAccess

}
