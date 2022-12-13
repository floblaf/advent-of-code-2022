fun main() {

    fun parse(input: String): Packet.Data {
        if (input.all { it in '0'..'9' }) {
            return Packet.Data.Single(input.toInt())
        }

        if (input == "[]") {
            return Packet.Data.Collection()
        }

        if (input.startsWith("[")) {
            var depth = 0
            var current = ""
            return Packet.Data.Collection(
                *buildList {
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
                }.toTypedArray()
            )
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

data class Packet(
    val data: Data
) : Comparable<Packet> {
    override fun compareTo(other: Packet): Int {
        return data.compareTo(other.data)
    }

    sealed class Data : Comparable<Data> {

        class Collection(
            vararg val items: Data
        ) : Data() {
            override fun compareTo(other: Data): Int {
                return when (other) {
                    is Single -> this.compareTo(Collection(other))
                    is Collection -> {
                        this.items
                            .zip(other.items) { l, r -> l.compareTo(r) }
                            .firstOrNull { it != 0 } ?: items.size.compareTo(other.items.size)
                    }
                }
            }
        }

        class Single(
            val value: Int
        ) : Data() {
            override fun compareTo(other: Data): Int {
                return when (other) {
                    is Single -> value.compareTo(other.value)
                    is Collection -> Collection(this).compareTo(other)
                }
            }
        }
    }
}