fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { part1Scoring[it] ?: throw IllegalStateException() }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { part2Scoring[it] ?: throw IllegalStateException() }
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}


val part1Scoring = mapOf(
    "A X" to 1+3,
    "A Y" to 2+6,
    "A Z" to 3+0,
    "B X" to 1+0,
    "B Y" to 2+3,
    "B Z" to 3+6,
    "C X" to 1+6,
    "C Y" to 2+0,
    "C Z" to 3+3,
)

val part2Scoring = mapOf(
    "A X" to 3+0,
    "A Y" to 1+3,
    "A Z" to 2+6,
    "B X" to 1+0,
    "B Y" to 2+3,
    "B Z" to 3+6,
    "C X" to 2+0,
    "C Y" to 3+3,
    "C Z" to 1+6,
)