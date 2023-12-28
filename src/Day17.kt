import Direction.*
import java.util.*

fun main() {

    data class Node(val row: Int, val col: Int, val direction: Direction, val seq: Int) {
    }

    data class Path(
        val node: Node,
        val pathWeight: Int,
        val depth: Int
    ) : Comparable<Path> {
        override fun compareTo(other: Path): Int {
            return pathWeight.compareTo(other.pathWeight)
        }
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray().map { c -> c.digitToInt() }.toList() }
        val queue = PriorityQueue<Path>()
        queue.add(Path(Node(0, 0, RIGHT, 1), 0, 0))
        val visited = mutableMapOf<Node, Int>()
        while (queue.isNotEmpty()) {
            val path = queue.poll()
            if (path.node.row == grid.size - 1 && path.node.col == grid[0].size - 1) {
                return path.pathWeight
            }
            val pathDirection = path.node.direction
            listOf(pathDirection.turn90().first, pathDirection.turn90().second, pathDirection)
                .asSequence()
                .filter { direction -> (path.node.row + direction.row) in grid.indices && (path.node.col + direction.col) in grid[0].indices }
                .map { direction ->
                    val row = path.node.row + direction.row
                    val col = path.node.col + direction.col
                    Node(
                        row, col,
                        direction,
                        if (direction == pathDirection) path.node.seq + 1 else 1
                    )
                }
                .filter { it.seq <= 3 }
                .map { node ->
                    Path(
                        node,
                        path.pathWeight + grid[node.row][node.col],
                        path.depth + 1,
                    )
                }
                .forEach { childPath ->
                    val existingWeight = visited.getOrDefault(childPath.node, Int.MAX_VALUE)
                    if (childPath.pathWeight < existingWeight) {
                        visited[childPath.node] = childPath.pathWeight
                        queue.add(childPath)
                    }
                }
        }
        throw RuntimeException("Can't reach the destination.")
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray().map { c -> c.digitToInt() }.toList() }
        val queue = PriorityQueue<Path>()
        queue.add(Path(Node(0, 0, RIGHT, 1), 0, 0))
        val visited = mutableMapOf<Node, Int>()
        while (queue.isNotEmpty()) {
            val path = queue.poll()
            if (path.node.row == grid.size - 1 && path.node.col == grid[0].size - 1 && path.node.seq == 4) {
                return path.pathWeight
            }
            val pathDirection = path.node.direction
            val directions = when (path.node.seq) {
                in 1..3 -> listOf(pathDirection)
                in 4..9 -> listOf(pathDirection.turn90().first, pathDirection.turn90().second, pathDirection)
                in 10 until Int.MAX_VALUE -> listOf(pathDirection.turn90().first, pathDirection.turn90().second)
                else -> throw IllegalArgumentException("Invalid sequence")
            }
            directions
                .asSequence()
                .filter { direction -> (path.node.row + direction.row) in grid.indices && (path.node.col + direction.col) in grid[0].indices }
                .map { direction ->
                    val row = path.node.row + direction.row
                    val col = path.node.col + direction.col
                    Node(
                        row, col,
                        direction,
                        if (direction == pathDirection) path.node.seq + 1 else 1
                    )
                }
                .map { node ->
                    Path(
                        node,
                        path.pathWeight + grid[node.row][node.col],
                        path.depth + 1,
                    )
                }
                .forEach { childPath ->
                    val existingWeight = visited.getOrDefault(childPath.node, Int.MAX_VALUE)
                    if (childPath.pathWeight < existingWeight) {
                        visited[childPath.node] = childPath.pathWeight
                        queue.add(childPath)
                    }
                }
        }
        throw RuntimeException("Can't reach the destination.")
    }

    val testInput = readInput("Day17_test")
    val result = part1(testInput)
    check(result == 102)

    val testPart2 = part2(testInput)
    check(testPart2 == 94)

    val testInput2 = readInput("Day17_test_2")
    val result2 = part2(testInput2)
    check(result2 == 71)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
