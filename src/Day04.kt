fun main() {
    data class Chore(val firstStart: Int, val firstEnd: Int, val secondStart: Int, val secondEnd: Int) {
        val firstRange = firstStart..firstEnd
        val secondRange = secondStart..secondEnd
    }

    fun part1(input: List<Chore>): Int {
        return input.count {
            it.secondRange.contains(it.firstStart) && it.secondRange.contains(it.firstEnd)
                    || it.firstRange.contains(it.secondStart) && it.firstRange.contains(it.secondEnd)
        }
    }

    fun part2(input: List<Chore>): Int {
        return input.count {
            it.secondRange.contains(it.firstStart) || it.secondRange.contains(it.firstEnd)
                    || it.firstRange.contains(it.secondStart) || it.firstRange.contains(it.secondEnd)
        }
    }

    val input = readInput("Day04").map {
        val match = Regex("(\\d+)-(\\d+),(\\d+)-(\\d+)").find(it)!!
        val (firstStart, firstEnd, secondStart, secondEnd) = match.destructured
        Chore(firstStart.toInt(), firstEnd.toInt(), secondStart.toInt(), secondEnd.toInt())
    }

    println(part1(input))
    println(part2(input))
}