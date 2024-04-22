@file:Suppress("MemberVisibilityCanBePrivate", "FunctionName")

package dev.deftu.componency.utils

public sealed interface ListEvent<T>
public data class ListAddEvent<T>(val element: IndexedValue<T>) : ListEvent<T>
public data class ListRemoveEvent<T>(val element: IndexedValue<T>) : ListEvent<T>
public data class ListClearEvent<T>(val elements: List<T>) : ListEvent<T>

/**
 * A subscribe-able list of components that is optimized for rendering.
 *
 * @since 0.1.0
 * @author Deftu
 */
public class SubscribeableLinkedList<T>() : MutableList<T> {
    public data class Node<T>(
        var value: T,
        var next: Node<T>? = null,
        var prev: Node<T>? = null
    )

    public constructor(elements: Collection<T>) : this() {
        addAll(elements)
    }

    public constructor(vararg elements: T) : this() {
        addAll(elements)
    }

    private var dirty = false
    private val subscribers = mutableListOf<ListSubscriber<T>>()

    public var start: Node<T>? = null
        private set
    public var end: Node<T>? = null
        private set

    public override var size: Int = 0
        private set

    override operator fun get(index: Int): T = find(index).value

    override operator fun set(index: Int, element: T): T {
        val node = find(index)
        val old = node.value
        node.value = element
        update(ListRemoveEvent(IndexedValue(index, old)))
        update(ListAddEvent(IndexedValue(index, element)))
        return old
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val current = find(index)
        for (element in elements) {
            val node = Node(element, current, current.prev)
            current.prev?.next = node
            current.prev = node
            size++
            update(ListAddEvent(IndexedValue(index, element)))
        }

        return true
    }

    override fun addAll(elements: Collection<T>): Boolean {
        if (elements is SubscribeableLinkedList) {
            if (elements.isEmpty()) return false

            if (isEmpty()) {
                start = elements.start
                end = elements.end
            } else {
                end?.next = elements.start
                elements.start?.prev = end
                end = elements.end
            }

            size += elements.size
            elements.forEach { update(ListAddEvent(IndexedValue(elements.indexOf(it), it))) }
        } else {
            elements.forEach { add(it) }
        }

        return true
    }

    override fun add(index: Int, element: T) {
        when (index) {
            0 -> {
                val node = Node(element, start)
                start?.prev = node
                start = node
                size++
            }

            size -> {
                val node = Node(element, null, end)
                end?.next = node
                end = node
                size++
            }

            else -> {
                val node = find(index)
                val newNode = Node(element, node, node.prev)
                node.prev?.next = newNode
                node.prev = newNode
                size++
            }
        }

        update(ListAddEvent(IndexedValue(index, element)))
    }

    override fun add(element: T): Boolean {
        val node = Node(element, null, end)
        if (start == null) {
            start = node
            end = node
        } else {
            end?.next = node
            node.prev = end
            end = node
        }

        size++
        update(ListAddEvent(IndexedValue(size - 1, element)))
        return true
    }

    override fun remove(element: T): Boolean {
        var current = start
        while (current != null) {
            if (current.value == element) {
                _removeInternal(current)
                return true
            }

            current = current.next
        }

        return false
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var output = false
        var current = start
        while (current != null) {
            if (current.value in elements) {
                _removeInternal(current)
                output = true
            }

            current = current.next
        }

        return output
    }

    override fun removeAt(index: Int): T {
        val node = find(index)
        _removeInternal(node)
        return node.value
    }

    override fun clear() {
        val elements = toList()
        start = null
        end = null
        size = 0
        update(ListClearEvent(elements))
    }

    override fun contains(element: T): Boolean {
        var current = start
        while (current != null) {
            if (current.value == element) return true
            current = current.next
        }

        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        for (element in elements) {
            if (element !in this) return false
        }

        return true
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        val output = SubscribeableLinkedList<T>()
        output.start = find(fromIndex)
        output.end = find(toIndex)
        output.size = toIndex - fromIndex
        return output
    }

    override fun indexOf(element: T): Int {
        var current = start
        var i = 0
        while (current != null) {
            if (current.value == element) return i
            current = current.next
            i++
        }

        return -1
    }

    override fun lastIndexOf(element: T): Int {
        var current = end
        var i = size - 1
        while (current != null) {
            if (current.value == element) return i
            current = current.prev
            i--
        }

        return -1
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        var output = false
        var current = start
        while (current != null) {
            if (current.value !in elements) {
                _removeInternal(current)
                output = true
            }

            current = current.next
        }

        return output
    }

    public override fun isEmpty(): Boolean = start == null && end == null

    override fun listIterator(index: Int): MutableListIterator<T> {
        return object : MutableListIterator<T> {
            var current: Node<T>? = find(index)
            var idx = index - 1

            override fun next(): T {
                val output = current ?: throw NoSuchElementException()
                current = output.next
                idx++
                return output.value
            }

            override fun previous(): T {
                val output = current?.prev ?: throw NoSuchElementException()
                current = output
                idx--
                return output.value
            }

            override fun add(element: T) {
                val node = Node(element, current, current?.prev)
                current?.prev?.next = node
                current?.prev = node
                size++
                update(ListAddEvent(IndexedValue(idx, element)))
            }

            override fun remove() {
                val node = current ?: throw NoSuchElementException()
                if (current === end) end = node.prev
                else if (current === start) start = node.next
                current = node.next
                node.prev?.next = node.next
                node.next?.prev = node.prev
                size--
            }

            override fun set(element: T) {
                val node = current?.prev ?: end ?: throw NoSuchElementException()
                node.value = element
            }

            override fun nextIndex() = idx + 1
            override fun previousIndex() = idx - 1

            override fun hasNext() = current != null
            override fun hasPrevious() = current?.prev != null
        }
    }

    @Deprecated(
        message = "Not supported",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("linkedEach(action)")
    ) public override fun listIterator(): MutableListIterator<T> = listIterator(0)

    @Deprecated(
        message = "Not supported",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("linkedEach(action)")
    ) public override fun iterator(): MutableListIterator<T> = listIterator(0)

    // Replacement functions

    public inline fun linkedEach(action: (T) -> Unit) {
        var current = start
        while (current != null) {
            action(current.value)
            current = current.next
        }
    }

    public inline fun linkedEachIndexed(action: (Int, T) -> Unit) {
        var current = start
        var i = 0
        while (current != null) {
            action(i, current.value)
            current = current.next
            i++
        }
    }

    public inline fun linkedRemoveIf(predicate: (T) -> Boolean): Boolean {
        var output = false
        var current = start
        while (current != null) {
            if (predicate(current.value)) {
                _removeInternal(current)
                output = true
            }

            current = current.next
        }

        return output
    }

    public inline fun linkedRemoveIfIndexed(predicate: (Int, T) -> Boolean): Boolean {
        var output = false
        var current = start
        var i = 0
        while (current != null) {
            if (predicate(i, current.value)) {
                _removeInternal(current)
                output = true
            }

            current = current.next
            i++
        }

        return output
    }

    public fun subscribe(subscriber: ListSubscriber<T>): () -> Unit {
        subscribers.add(subscriber)
        return {
            subscribers.remove(subscriber)
        }
    }

    public fun subscribe(subscriber: (ListEvent<T>) -> Unit): () -> Unit {
        return subscribe(object : ListSubscriber<T> {
            override fun update(event: ListEvent<T>) {
                subscriber(event)
            }
        })
    }

    private fun find(index: Int): Node<T> {
        val diff = size - index
        if (diff < index) {
            var current = end
            var i = size - 1
            while (current != null) {
                if (i == index) return current
                current = current.prev
                i--
            }

            throw IndexOutOfBoundsException("Index: $index, Size: $size")
        } else {
            var current = start
            var i = 0
            while (current != null) {
                if (i == index) return current
                current = current.next
                i++
            }

            throw IndexOutOfBoundsException("Index: $index, Size: $size")
        }
    }

    public fun _removeInternal(node: Node<T>) {
        if (node === start) start = node.next
        if (node === end) end = node.prev
        node.prev?.next = node.next
        node.next?.prev = node.prev
        size--
    }

    private fun update(event: ListEvent<T>) {
        dirty = true
        subscribers.forEach { it.update(event) }
    }
}

public interface ListSubscriber<T> {
    public fun update(event: ListEvent<T>)
}
