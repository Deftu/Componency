package dev.deftu.componency.components.impl.input

public data class InputPosition(
    public val line: Int,
    public val column: Int
) : Comparable<InputPosition> {

    public companion object {

        @JvmField
        public val START: InputPosition = noInline { InputPosition(0, 0) }

        @JvmStatic
        public fun sorted(first: InputPosition, second: InputPosition): Pair<InputPosition, InputPosition> {
            return if (first < second) {
                first to second
            } else {
                second to first
            }
        }

        @JvmStatic
        public fun endOf(lines: List<String>): InputPosition {
            val lastLine = lines.last()
            return InputPosition(lines.size - 1, lastLine.length)
        }

        private fun <T> noInline(block: () -> T): T = block()

    }

    public fun withColumnOffset(offset: Int): InputPosition {
        return InputPosition(line, column + offset)
    }

    public fun withLineOffset(offset: Int): InputPosition {
        return InputPosition(line + offset, column)
    }

    override fun compareTo(other: InputPosition): Int {
        val lineComparison = line.compareTo(other.line)
        return if (lineComparison != 0) {
            lineComparison
        } else {
            column.compareTo(other.column)
        }
    }

}
