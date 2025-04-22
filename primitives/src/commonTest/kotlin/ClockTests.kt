import dev.deftu.componency.time.DefaultClock
import kotlin.test.Test
import kotlin.test.assertTrue

class ClockTests {

    @Test
    fun testNowMillisIsPositive() {
        println("Testing nowMillis()")

        val now = DefaultClock.nowMillis()
        println("Now: $now")
        assertTrue(now > 0)
    }

}
