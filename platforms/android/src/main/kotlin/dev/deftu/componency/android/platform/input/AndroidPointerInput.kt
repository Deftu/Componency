package dev.deftu.componency.android.platform.input

import android.content.Context
import android.view.MotionEvent
import android.view.View
import dev.deftu.componency.input.MouseButton
import dev.deftu.componency.platform.input.PointerInput
import dev.deftu.componency.platform.input.PointerState

public class AndroidPointerInput(
    private val view: View,
    private val context: Context
) : PointerInput {

    private val pointerStates = mutableMapOf<Int, PointerState>()

    override val pointerCount: Int
        get() = pointerStates.size

    override fun getPointer(index: Int): PointerState {
        return pointerStates.values.elementAtOrNull(index) ?: PointerState(index, 0f, 0f, emptySet())
    }

    public fun handleTouchEvent(event: MotionEvent) {
        val pointerIndex = event.actionIndex
        val pointerId = event.getPointerId(pointerIndex)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                val x = event.getX(pointerIndex)
                val y = event.getY(pointerIndex)
                pointerStates[pointerId] = PointerState(pointerId, x, y, setOf(MouseButton.LEFT))
            }

            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    val id = event.getPointerId(i)
                    val x = event.getX(i)
                    val y = event.getY(i)
                    val pressed = pointerStates[id]?.pressedButtons ?: setOf(MouseButton.LEFT)
                    pointerStates[id] = PointerState(id, x, y, pressed)
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_CANCEL -> {
                pointerStates.remove(pointerId)
            }
        }
    }

}
