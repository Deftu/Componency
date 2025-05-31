package dev.deftu.componency.android

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import dev.deftu.componency.android.platform.AndroidPlatform
import dev.deftu.componency.components.Component
import dev.deftu.componency.input.MouseButton

public class ComponencyView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    internal lateinit var platform: AndroidPlatform
    private var rootComponent: Component<*, *>? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!::platform.isInitialized) return

        platform.currentCanvas = canvas
        rootComponent?.handleRender()
        platform.currentCanvas = null
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false

        if (::platform.isInitialized) {
            platform.inputHandler.pointerInput.handleTouchEvent(event)
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x.toDouble()
                val y = event.y.toDouble()
                rootComponent?.handlePointerClick(x, y, platform.inputHandler.pointerInput.getPointer(0).pressedButtons.firstOrNull() ?: MouseButton.LEFT)
                performClick()
            }

            MotionEvent.ACTION_MOVE -> {
                val x = event.x.toDouble()
                val y = event.y.toDouble()
                rootComponent?.handlePointerDrag(x, y)
            }

            MotionEvent.ACTION_UP -> {
                rootComponent?.handlePointerRelease()
            }

            MotionEvent.ACTION_SCROLL -> {
                val scrollAmount = event.getAxisValue(MotionEvent.AXIS_VSCROLL)
                rootComponent?.handleScroll(scrollAmount.toDouble())
            }
        }

        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (::platform.isInitialized && event != null) {
            platform.inputHandler.keyboardInput.handleKeyEvent(event)
            val key = platform.inputHandler.keyboardInput.getKey(keyCode)
            rootComponent?.handleKeyPress(key, platform.inputHandler.keyboardInput.modifiers)
        }

        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (::platform.isInitialized && event != null) {
            platform.inputHandler.keyboardInput.handleKeyEvent(event)
            val key = platform.inputHandler.keyboardInput.getKey(keyCode)
            rootComponent?.handleKeyRelease(key, platform.inputHandler.keyboardInput.modifiers)
        }

        return true
    }

    public fun setContent(builder: () -> Component<*, *>) {
        rootComponent = builder()
        invalidate()
    }
}
