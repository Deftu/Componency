import kotlin.test.Test
import kotlin.test.assertEquals

class JsTestSanity {

    @Test
    fun testPrint() {
        println("JS println working!")
        assertEquals(2, 1 + 1)
    }

}
