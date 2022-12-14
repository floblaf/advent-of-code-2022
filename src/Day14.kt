import kotlin.math.max
import kotlin.math.min

fun main() {

    fun Grid.add(x: Int, y: Int, material: Grid.Material) {
        if (content[x] == null) {
            content[x] = mutableMapOf()
        }
        content[x]!![y] = material
    }

    fun Grid.findDeepestRock(): Int {
        return content.values.maxOf { it.keys.max() }
    }

    fun Grid.nextPosition(x: Int, y: Int): Pair<Int, Int>? {
        return listOf(0, -1, +1)
            .map { x + it }
            .firstOrNull { content[it]?.get(y + 1) == null }
            ?.let { it to y + 1 }
    }

    fun parseGrid(): Grid {
        val grid = Grid()
        readInput("Day14")
            .map { line ->
                line
                    .split("->")
                    .map { it.trim() }
                    .windowed(2)
            }
            .flatten()
            .forEach { (start, end) ->
                val (xStart, yStart) = start.split(",").map { it.toInt() }
                val (xEnd, yEnd) = end.split(",").map { it.toInt() }

                if (xStart == xEnd) {
                    for (i in min(yStart, yEnd)..max(yStart, yEnd)) {
                        grid.add(xStart, i, Grid.Material.ROCK)
                    }
                } else if (yStart == yEnd) {
                    for (i in min(xStart, xEnd)..max(xStart, xEnd)) {
                        grid.add(i, yStart, Grid.Material.ROCK)
                    }
                } else {
                    throw IllegalStateException()
                }
            }

        return grid
    }

    fun part1(grid: Grid): Int {
        val yMax = grid.findDeepestRock()

        infinite@ while (true) {
            var xSand = 500
            var ySand = 0
            while (true) {
                val next = grid.nextPosition(xSand, ySand) ?: break
                if (ySand == yMax) break@infinite
                xSand = next.first
                ySand = next.second
            }
            grid.add(xSand, ySand, Grid.Material.SAND)
        }

        return grid.content.values.sumOf { it.values.count { it == Grid.Material.SAND } }
    }

    fun part2(grid: Grid): Int {
        val yMax = grid.findDeepestRock() + 2
        while (true) {
            if (grid.content[500]?.get(0) == Grid.Material.SAND) break

            var xSand = 500
            var ySand = 0
            while (true) {
                if (ySand == yMax - 1) break
                val next = grid.nextPosition(xSand, ySand) ?: break
                xSand = next.first
                ySand = next.second
            }

            grid.add(xSand, ySand, Grid.Material.SAND)
        }

        return grid.content.values.sumOf { it.values.count { it == Grid.Material.SAND } }
    }

    println(part1(parseGrid()))
    println(part2(parseGrid()))
}

data class Grid(
    val content: MutableMap<Int, MutableMap<Int, Material>> = mutableMapOf()
) {
    enum class Material {
        ROCK, SAND
    }
}