fun main() {
    data class Operation(val count: Int, val from: Int, val to: Int)

    val input = readInput("Day05")

    val blankLine = input.indexOfFirst { it.isEmpty() }

    val crates = input.take(blankLine - 1)
        .map { line -> line.chunked(4).map { it[1] } }
        .reversed()

    val cargo = buildList {
        for (i in 0 until crates[0].size) {
            add(buildList {
                for (l in crates.indices) {
                    val value = crates[l][i]
                    if (value != ' ') {
                        add(value)
                    }
                }
            })
        }
    }

    val operations = input.drop(blankLine + 1)
        .map {
            val match = Regex("move (\\d+) from (\\d+) to (\\d+)").find(it)!!
            val (count, from, to) = match.destructured
            Operation(count.toInt(), from.toInt() - 1, to.toInt() - 1)
        }

    fun part1(cargo: List<List<Char>>, operations: List<Operation>): String {
        val cargo9000 = cargo.map { ArrayDeque(it) }
        operations.forEach { operation ->
            repeat(operation.count) {
                cargo9000[operation.to].addLast(cargo9000[operation.from].removeLast())
            }
        }

        return cargo9000.map { it.last() }.joinToString("")
    }

    fun part2(cargo: List<List<Char>>, operations: List<Operation>): String {
        val cargo9001 = cargo.map { ArrayDeque(it) }
        operations.forEach { operation ->
            val temp = ArrayDeque<Char>()
            repeat(operation.count) {
                temp.addFirst(cargo9001[operation.from].removeLast())
            }
            cargo9001[operation.to].addAll(temp)
        }

        return cargo9001.map { it.last() }.joinToString("")
    }

    println(part1(cargo, operations))
    println(part2(cargo, operations))
}
