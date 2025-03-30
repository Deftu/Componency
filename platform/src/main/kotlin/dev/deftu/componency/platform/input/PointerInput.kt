package dev.deftu.componency.platform.input

public interface PointerInput {

    public val pointerCount: Int

    public val pointers: List<PointerState>
        get() = (0 until pointerCount).map(::getPointer)

    public val primaryPointer: PointerState
        get() = getPointer(0)

    public val pointerX: Float
        get() = primaryPointer.x

    public val pointerY: Float
        get() = primaryPointer.y

    public val isPointerDown: Boolean
        get() = primaryPointer.isDown

    public fun getPointer(index: Int): PointerState

}
