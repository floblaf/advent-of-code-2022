fun main() {
    fun part1(elves: List<Elf>): Int {
        return elves.maxBy { it.calories }.calories
    }

    fun part2(elves: List<Elf>): Int {
        return elves.sortedByDescending { it.calories }
            .take(3)
            .sumOf { it.calories }
    }

    val input = readInput("Day01")

    val elves: MutableList<Elf> = mutableListOf()

    var currentElf = 0
    input.forEach {
        if (it.isBlank()) {
            elves.add(Elf(currentElf))
            currentElf = 0
        } else {
            currentElf += it.toInt()
        }
    }
    elves.add(Elf(currentElf))

    println(part1(elves))
    println(part2(elves))
}
data class Elf(val calories: Int)