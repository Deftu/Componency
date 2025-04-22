import kotlin.test.Test
import kotlin.test.assertEquals

class JsTestSanity {

    @Test
    fun testPrint() {
        println("WASM JS println working!")
        assertEquals(2, 1 + 1)
    }

}
