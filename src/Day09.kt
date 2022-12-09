import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

fun main() {

    val moves = readInput("Day09").map {
        val (direction, steps) = it.split(" ")
        Move(
            direction = when (direction) {
                "U" -> Direction.UP
                "D" -> Direction.DOWN
                "L" -> Direction.LEFT
                "R" -> Direction.RIGHT
                else -> throw IllegalStateException()
            },
            steps = steps.toInt()
        )
    }

    fun computePositions(ropeSize: Int, moves: List<Move>): List<Rope> {
        val positions = mutableListOf(Rope(List(ropeSize) { Knot(x = 0, y = 0) }))
        moves.forEach { move ->
            repeat(move.steps) {
                val lastPosition = positions.last()
                val knots = mutableListOf<Knot>()

                knots.add(lastPosition.knots[0].let {
                    when (move.direction) {
                        Direction.LEFT -> it.copy(x = it.x - 1)
                        Direction.RIGHT -> it.copy(x = it.x + 1)
                        Direction.UP -> it.copy(y = it.y + 1)
                        Direction.DOWN -> it.copy(y = it.y - 1)
                    }
                })
                for (i in 1 until lastPosition.knots.size) {
                    val previous = lastPosition.knots[i]
                    knots.add(if (knots[i-1].distance(previous) >= 2.0) {
                        previous.copy(
                            x = previous.x + (knots[i-1].x - previous.x).sign,
                            y = previous.y + (knots[i-1].y - previous.y).sign
                        )
                    } else {
                        previous
                    })
                }

                positions.add(Rope(knots.toList()))
            }
        }

        return positions.toList()
    }

    fun countDistinctPositionOfTail(input: List<Rope>): Int {
        return input.map { it.knots.last() }.distinct().count()
    }

    println(countDistinctPositionOfTail(computePositions(2, moves)))
    println(countDistinctPositionOfTail(computePositions(10, moves)))
}

enum class Direction { LEFT, RIGHT, UP, DOWN }
data class Move(val direction: Direction, val steps: Int)
data class Knot(val x: Int, val y: Int) {

    fun distance(other: Knot): Double {
        return sqrt((other.x - x).toDouble().pow(2) + (other.y - y).toDouble().pow(2))
    }
}

data class Rope(
    val knots: List<Knot>
)