import kotlin.math.max

fun main() {
    data class Tree(
        val height: Int,
        val left: List<Int>,
        val right: List<Int>,
        val top: List<Int>,
        val bottom: List<Int>
    )

    fun Tree.findDistance(list: List<Int>): Int {
        return list.indexOfFirst { it >= this.height }
            .let {
                if (it == -1) {
                    list.size
                } else {
                    it + 1
                }
            }
    }

    fun part1(grid: List<Tree>): Int {
        return grid.count { tree ->
            tree.left.all { it < tree.height } ||
                    tree.right.all { it < tree.height } ||
                    tree.top.all { it < tree.height } ||
                    tree.bottom.all { it < tree.height }
        }
    }

    fun part2(grid: List<Tree>): Int {
        return grid.maxOfOrNull {
            it.findDistance(it.left) * it.findDistance(it.right) *
                    it.findDistance(it.top) * it.findDistance(it.bottom)
        } ?: 0
    }

    val input = readInput("Day08")

    val grid = buildList {
        input.forEach { row ->
            add(buildList {
                row.forEach { add(it.digitToInt()) }
            })
        }
    }

    val flattenGrid = buildList {
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                add(
                    Tree(
                        height = grid[i][j],
                        left = grid[i].take(j).reversed(),
                        right = grid[i].drop(j + 1),
                        top = grid.map { it[j] }.take(i).reversed(),
                        bottom = grid.map { it[j] }.drop(i + 1)
                    )
                )
            }
        }
    }

    println(part1(flattenGrid))
    println(part2(flattenGrid))
}