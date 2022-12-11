fun main() {
    data class Test(val division: Long, val targetSuccess: Int, val targetFail: Int) {
        fun run(x: Long): Int {
            return if (x % division == 0L) targetSuccess else targetFail
        }
    }

    data class Monkey(
        val items: MutableList<Long> = mutableListOf(),
        val operation: (old: Long) -> Long,
        val test: Test,
        var inspections: Long = 0L
    )

    fun playRounds(monkeys: List<Monkey>, round: Int, worryReduce: (Long) -> Long): Long {
        repeat(round) {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val itemValue = monkey.items.removeFirst()
                    val newItemValue = worryReduce(monkey.operation(itemValue))

                    monkeys[monkey.test.run(newItemValue)].items.add(newItemValue)

                    monkey.inspections++
                }

            }
        }

        return monkeys.map { it.inspections }.sortedDescending().let { (first, second) -> first * second }
    }

    fun part1(monkeys: List<Monkey>): Long {
        return playRounds(monkeys, 20) { it / 3 }
    }

    fun part2(monkeys: List<Monkey>): Long {
        val div = monkeys.map { it.test.division }.reduce(Long::times)
        return playRounds(monkeys, 10_000) { it % div }
    }

    fun parseMonkeys(input: List<String>): List<Monkey> {
        return input
            .filter { it.isNotBlank() }
            .windowed(size = 6, step = 6)
            .map { lines ->
                Monkey(
                    items = lines[1].substringAfter(": ").split(", ").map { it.toLong() }.toMutableList(),
                    { x ->
                        lines[2].substringAfter("= old ").split(" ").let { (operator, v) ->
                            val right = if (v == "old") x else v.toLong()
                            when (operator) {
                                "*" -> x * right
                                "+" -> x + right
                                else -> throw IllegalStateException()
                            }
                        }
                    },
                    Test(
                        division = lines[3].split(" ").last().toLong(),
                        targetSuccess = lines[4].split(" ").last().toInt(),
                        targetFail = lines[5].split(" ").last().toInt(),
                    )
                )
            }
    }

    val input = readInput("Day11")
    println(part1(parseMonkeys(input)))
    println(part2(parseMonkeys(input)))
}

