package dev.deftu.componency.android.platform.input

import android.content.Context
import android.view.View
import dev.deftu.componency.platform.input.InputHandler

public class AndroidInputHandler(
    private val view: View,
    private val context: Context
) : InputHandler {

    override val pointerInput: AndroidPointerInput = AndroidPointerInput(view, context)

    override val keyboardInput: AndroidKeyboardInput = AndroidKeyboardInput(view, context)

    override val clipboard: AndroidClipboardAccess = AndroidClipboardAccess(view, context)

}
