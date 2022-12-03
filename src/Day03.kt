fun main() {
    val priorities = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    fun part1(input: List<String>): Int {
        return input
            .mapNotNull { backpack ->
                backpack.chunked(backpack.length / 2)
                    .map { it.toSet() }
                    .let { it.first().intersect(it.last()) }
                    .firstOrNull()
            }
            .sumOf { priorities.indexOf(it) + 1 }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { it.toSet() }
            .chunked(3)
            .mapNotNull { group -> group
                .fold(group.first()) { inter, next -> inter.intersect(next) }
                .firstOrNull()
            }
            .sumOf { priorities.indexOf(it) + 1 }
    }

    val input = readInput("Day03")

    println(part1(input))
    println(part2(input))
}