package dev.deftu.componency.tests

import dev.deftu.componency.Density
import dev.deftu.componency.canvas.Brush
import dev.deftu.componency.canvas.DrawList
import dev.deftu.componency.canvas.Paint
import dev.deftu.componency.canvas.RecordingCanvas
import dev.deftu.kolor.Kolor
import kotlin.test.Test

class CanvasRecordingTest {

    @Test
    fun testCanvasRecording() {
        val list = DrawList()
        val canvas = RecordingCanvas(Density(), list)
        val brush = Brush.Solid(Kolor.hsb(0f, 1f, 1f))
        canvas.drawRect(0f, 0f, 100f, 100f, Paint(brush))

        println(list.ops)
    }

}
