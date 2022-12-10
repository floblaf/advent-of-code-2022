fun main() {

    fun part1(cycles: List<Int>): Int {
        return listOf(20, 60, 100, 140, 180, 220).sumOf { it * cycles[it-1] }
    }

    fun part2(cycles: List<Int>): String {
        return buildString {
            cycles.forEachIndexed { index, i ->
                if (index >= 40 && index % 40 == 0) {
                    append('\n')
                }
                if ((index % 40) in i - 1..i + 1) {
                    append('#')
                } else {
                    append('.')
                }
            }
        }
    }

    val input = readInput("Day10")
    var x = 1
    val cycles = buildList {
        input.forEach {
            when (it) {
                "noop" -> add(x)
                else -> {
                    add(x)
                    add(x)
                    x += it.split(" ")[1].toInt()
                }
            }
        }
    }

    println(part1(cycles))
    println(part2(cycles))
}