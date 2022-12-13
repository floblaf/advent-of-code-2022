fun main() {

    fun compare(left: Any, right: Any): Int {
        return if (left is Int && right is Int) {
            return left.compareTo(right)
        } else if (left is List<*> && right is List<*>) {
            left
                .zip(right) { l, r -> compare(l!!, r!!) }
                .firstOrNull { it != 0 } ?: left.size.compareTo(right.size)
        } else if (left is List<*>) {
            compare(left, listOf(right))
        } else if (right is List<*>) {
            compare(listOf(left), right)
        } else {
            throw IllegalStateException()
        }
    }
    data class Packet(
        val content: Any
    ): Comparable<Packet> {
        override fun compareTo(other: Packet): Int {
            return compare(this.content, other.content)
        }
    }

    fun parse(input: String): Any {
        if (input.all { it in '0'..'9' }) {
            return input.toInt()
        }

        if (input == "[]") {
            return emptyList<Any>()
        }

        if (input.startsWith("[")) {
            var depth = 0
            var current = ""
            return buildList {
                input.removePrefix("[").forEach { char ->
                    when {
                        char == ',' && depth == 0 -> {
                            add(parse(current))
                            current = ""
                        }

                        char == '[' -> {
                            depth++
                            current += char
                        }

                        char == ']' -> {
                            depth--
                            if (depth == -1) {
                                add(parse(current))
                            } else {
                                current += char
                            }
                        }

                        else -> current += char
                    }
                }
            }
        }
        throw IllegalStateException()
    }

    fun part1(input: List<Packet>): Int {
        return input
            .chunked(2)
            .mapIndexed { index, (left, right) ->
                if (left < right) {
                    index + 1
                } else {
                    0
                }
            }
            .sum()
    }

    fun part2(input: List<Packet>): Int {
        val dividerA = Packet(parse("[[2]]"))
        val dividerB = Packet(parse("[[6]]"))
        val sorted = (input + listOf(dividerA, dividerB))
            .sorted()

        return (sorted.indexOf(dividerA) + 1) * (sorted.indexOf(dividerB) + 1)
    }



    val input = readInput("Day13")
        .filter { it.isNotBlank() }
        .map {
            Packet(parse(it))
        }

    println(input.chunked(2))

    println(part1(input))
    println(part2(input))
}