package xyz.deftu.componency.events

data class ComponentMouseClickEvent(
    val x: Double,
    val y: Double,
    val button: Int
) {
    var isCancelled = false
}

data class ComponentMouseReleaseEvent(
    val x: Double,
    val y: Double,
    val button: Int
) {
    var isCancelled = false
}

data class ComponentMouseDragEvent(
    val x: Double,
    val y: Double,
    val button: Int
) {
    var isCancelled = false
}

data class ComponentMouseScrollEvent(
    val x: Double,
    val y: Double,
    val delta: Double
) {
    var isCancelled = false
}
