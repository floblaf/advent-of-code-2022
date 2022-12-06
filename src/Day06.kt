fun main() {
    fun String.hasRedundancy(): Boolean {
        return this.groupingBy { it }
            .eachCount()
            .any { it.value >= 2 }
    }

    fun String.findIndexOfGroup(groupSize: Int): Int {
        return this
            .windowedSequence(groupSize)
            .indexOfFirst { !it.hasRedundancy() } + groupSize
    }

    fun part1(input: String): Int {
        return input.findIndexOfGroup(4)
    }

    fun part2(input: String): Int {
        return input.findIndexOfGroup(14)
    }

    val input = readInput("Day06").first()
    println(part1(input))
    println(part2(input))
}