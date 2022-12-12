fun main() {

    data class Coordinate(
        val value: Char,
        val x: Int,
        val y: Int,
        var d1: Int = 0,
        var d2: Int = 0
    ) {
        fun canAccess(coordinate: Coordinate): Boolean {
            return coordinate.value - this.value <= 1
        }
    }

    val input = readInput("Day12")
        .map { it.toList() }

    lateinit var start: Coordinate
    lateinit var end: Coordinate
    val width = input[0].size
    val height = input.size

    val grid = MutableList(height) { mutableListOf<Coordinate>() }
    input.forEachIndexed { i, chars ->
        chars.forEachIndexed { j, c ->
            grid[i].add(Coordinate(
                value = when (c) {
                    'S' -> 'a'
                    'E' -> 'z'
                    else -> c
                },
                x = i,
                y = j
            ).also {
                when (c) {
                    'S' -> start = it
                    'E' -> end = it
                }
            })
        }
    }

    fun getAccessibleCoordinates(coordinate: Coordinate): List<Coordinate> {
        val x = coordinate.x
        val y = coordinate.y
        return buildList {
            if (x >= 1) add(grid[x - 1][y])
            if (x < height - 1) add(grid[x + 1][y])
            if (y >= 1) add(grid[x][y - 1])
            if (y < width - 1) add(grid[x][y + 1])
        }.filter { coordinate.canAccess(it) }
    }

    fun search(starts: List<Coordinate>, end: Coordinate, distanceOperation: (current : Coordinate, previous: Coordinate) -> Unit) {
        val queue = ArrayDeque(starts)
        val visited = mutableListOf(*starts.toTypedArray())

        while(queue.isNotEmpty()) {
            val current = queue.removeFirst()
            getAccessibleCoordinates(current)
                .filter { it !in visited }
                .forEach {
                    distanceOperation.invoke(it, current)
                    visited.add(it)
                    if (it == end) {
                        return
                    }
                    queue.addLast(it)
                }
        }
    }

    search(listOf(start), end) { current, previous -> current.d1 = previous.d1 + 1 }
    search(listOf(start) + grid.flatten().filter { it.value == 'a' }, end) { current, previous -> current.d2 = previous.d2 + 1 }
    println(end.d1)
    println(end.d2)
}