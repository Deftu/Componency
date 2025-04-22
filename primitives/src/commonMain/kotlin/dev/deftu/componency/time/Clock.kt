package dev.deftu.componency.time

public interface Clock {

    public fun nowSeconds(): Double {
        return nowMillis() / 1_000.0
    }

    public fun nowMillis(): Long

}

public expect object DefaultClock : Clock {
    override fun nowMillis(): Long
}
